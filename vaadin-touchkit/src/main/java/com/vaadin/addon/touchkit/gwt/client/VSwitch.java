package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusWidget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VTooltip;
import com.vaadin.terminal.gwt.client.ui.Icon;

/**
 * VSwitch is the client-side implementation of the Switch component.
 * 
 * Derived from implementations by Teemu Pöntelin Vaadin Ltd.
 * 
 */
public class VSwitch extends FocusWidget implements Paintable, KeyUpHandler,
		MouseDownHandler, MouseUpHandler, MouseMoveHandler, TouchStartHandler,
		TouchMoveHandler, TouchEndHandler, TouchCancelHandler, FocusHandler,
		BlurHandler {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-tk-switch";
	private final int DRAG_THRESHOLD_PIXELS = 10;
	private final int ANIMATION_DURATION_MS = 300;

	/** The client side widget identifier */
	protected String paintableId;

	/** Reference to the server connection object. */
	ApplicationConnection client;

	private Element slider;
	private boolean value;
	private com.google.gwt.user.client.Element errorIndicatorElement;
	private Icon icon;
	private boolean immediate;

	private boolean mouseDown;
	private int unvisiblePartWidth;

	private int tabIndex;
	private int dragStart;
	private int sliderOffsetLeft;
	private boolean dragging;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VSwitch() {
		TouchKitResources.INSTANCE.css().ensureInjected();
		// Change to proper element or remove if extending another widget
		setElement(Document.get().createDivElement());

		// This method call of the Paintable interface sets the component
		// style name in DOM tree
		setStyleName(CLASSNAME);

		// build the DOM
		slider = Document.get().createDivElement();
		slider.setClassName(CLASSNAME + "-slider");
		getElement().appendChild(slider);

		addHandlers();
	}

	private void addHandlers() {
		addDomHandler(this, KeyUpEvent.getType());
		addDomHandler(this, MouseDownEvent.getType());
		addDomHandler(this, MouseUpEvent.getType());
		addDomHandler(this, MouseMoveEvent.getType());
		addDomHandler(this, FocusEvent.getType());
		addDomHandler(this, BlurEvent.getType());
		addDomHandler(this, TouchStartEvent.getType());
		addDomHandler(this, TouchMoveEvent.getType());
		addDomHandler(this, TouchEndEvent.getType());
		addDomHandler(this, TouchCancelEvent.getType());
		addDomHandler(this, KeyUpEvent.getType());
	}

	/**
	 * Called whenever an update is received from the server
	 */
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		// This call should be made first.
		// It handles sizes, captions, tooltips, etc. automatically.
		if (client.updateComponent(this, uidl, true)) {
			// If client.updateComponent returns true there has been no changes
			// and we do not need to update anything.
			return;
		}

		boolean skipAnimation = true;
		updateVisibleState(skipAnimation);

		// Save reference to server connection object to be able to send
		// user interaction later
		this.client = client;

		// Save the client side identifier (paintable id) for the widget
		paintableId = uidl.getId();

		// All the following is mostly just copied from VCheckBox
		if (uidl.hasAttribute("error")) {
			if (errorIndicatorElement == null) {
				errorIndicatorElement = DOM.createSpan();
				errorIndicatorElement.setInnerHTML("&nbsp;");
				DOM.setElementProperty(errorIndicatorElement, "className",
						"v-errorindicator");
				DOM.appendChild(getElement(), errorIndicatorElement);
				DOM.sinkEvents(errorIndicatorElement, VTooltip.TOOLTIP_EVENTS
						| Event.ONCLICK);
			}
		} else if (errorIndicatorElement != null) {
			DOM.setStyleAttribute(errorIndicatorElement, "display", "none");
		}

		if (uidl.hasAttribute("readonly")) {
			setEnabled(false);
		}

		if (uidl.hasAttribute("icon")) {
			if (icon == null) {
				icon = new Icon(client);
				DOM.insertChild(getElement(), icon.getElement(), 1);
				icon.sinkEvents(VTooltip.TOOLTIP_EVENTS);
				icon.sinkEvents(Event.ONCLICK);
			}
			icon.setUri(uidl.getStringAttribute("icon"));
		} else if (icon != null) {
			// detach icon
			DOM.removeChild(getElement(), icon.getElement());
			icon = null;
		}

		setValue(uidl.getBooleanVariable("state"));
		immediate = uidl.getBooleanAttribute("immediate");

	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (!enabled) {
			super.setTabIndex(-1);
		} else {
			super.setTabIndex(tabIndex);
		}
	}

	@Override
	public void setTabIndex(int index) {
		super.setTabIndex(index);
		tabIndex = index;
	}

	private void setValue(boolean value) {
		if (this.value == value) {
			return;
		}

		// update the server state
		if (paintableId == null || client == null) {
			return;
		}
		this.value = value;
		client.updateVariable(paintableId, "state", this.value, immediate);

		// update the UI
		updateVisibleState();
	}

	private void updateVisibleState() {
		updateVisibleState(false);
	}

	public int getUnvisiblePartWidth() {
		if (unvisiblePartWidth == 0) {
			// Calculate the width of the part that is not currently visible
			// and init the state on the first rendering.
			int width = getElement().getClientWidth();
			int sliderWidth = this.slider.getClientWidth();
			unvisiblePartWidth = sliderWidth - width;
			if(unvisiblePartWidth < 3) {
				// CSS not loaded yet
				unvisiblePartWidth = 0;
			}
		}
		return unvisiblePartWidth;
	}

	private void updateVisibleState(final boolean skipAnimation) {
		ScheduledCommand command = new ScheduledCommand() {

			public void execute() {
				final int targetLeft = (value ? 0 : -getUnvisiblePartWidth());

				if (skipAnimation) {
					slider.getStyle().setProperty("left", targetLeft + "px");
				} else {
					Animation a = new Animation() {

						@Override
						protected void onUpdate(double progress) {
							int currentLeft = slider.getOffsetLeft();
							int newLeft = (int) (currentLeft + (progress * (targetLeft - currentLeft)));
							slider.getStyle().setProperty("left",
									newLeft + "px");
						}
					};
					a.run(ANIMATION_DURATION_MS);
				}
			}
		};
		if (getUnvisiblePartWidth() == 0) {
			// Css not properly injected yet
			Scheduler.get().scheduleDeferred(command);
		} else {
			command.execute();
		}
	}

	public void onKeyUp(KeyUpEvent event) {
		if (isEnabled() && event.getNativeKeyCode() == 32) {
			// 32 = space bar
			setValue(!value);
		}
	}

	public void onMouseDown(MouseDownEvent event) {
		if (isEnabled()) {
			handleMouseDown(event.getScreenX());
			event.preventDefault();
		}
	}

	private void handleMouseDown(int clientX) {
		mouseDown = true;
		dragStart = clientX;
		sliderOffsetLeft = slider.getOffsetLeft();
	}

	public void onMouseUp(MouseUpEvent event) {
		if (isEnabled()) {
			handleMouseUp();
		}
	}

	private void handleMouseUp() {
		if (!dragging) {
			setValue(!value);
		} else {
			if (slider.getOffsetLeft() < (-getUnvisiblePartWidth() / 2)) {
				setValue(false);
			} else {
				setValue(true);
			}
			updateVisibleState();
			DOM.releaseCapture(getElement());
		}

		mouseDown = false;
		dragging = false; // not dragging anymore
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (isEnabled()) {
			handleMouseMove(event.getScreenX());
		}
	}

	private void handleMouseMove(int clientX) {

		if (mouseDown) {
			int dragDistance = clientX - dragStart;
			if (Math.abs(dragDistance) > DRAG_THRESHOLD_PIXELS) {

				dragging = true;
				// Use capture to catch mouse events even if user
				// drags the mouse cursor out of the widget area.
				DOM.setCapture(getElement());
			}

			if (dragging) {
				// calculate new left position and
				// check for boundaries
				int left = sliderOffsetLeft + dragDistance;
				if (left < -getUnvisiblePartWidth()) {
					left = -getUnvisiblePartWidth();
				} else if (left > 0) {
					left = 0;
				}

				// set the CSS left
				slider.getStyle().setProperty("left", left + "px");
			}
		}
	}

	public void onFocus(FocusEvent event) {
		addStyleDependentName("focus");
	}

	public void onBlur(BlurEvent event) {
		removeStyleDependentName("focus");
	}

	public void onTouchEnd(TouchEndEvent event) {
		if (isEnabled()) {
			handleMouseUp();
		}
	}

	public void onTouchMove(TouchMoveEvent event) {
		if (isEnabled()) {
			Touch touch = event.getTouches().get(0).cast();
			handleMouseMove(touch.getPageX());
			event.preventDefault();
		}
	}

	public void onTouchStart(TouchStartEvent event) {
		if (isEnabled()) {
			Touch touch = event.getTouches().get(0).cast();
			handleMouseDown(touch.getPageX());
			event.preventDefault();
		}
	}

	public void onTouchCancel(TouchCancelEvent event) {
		if (isEnabled()) {
			handleMouseUp();
		}
	}

}