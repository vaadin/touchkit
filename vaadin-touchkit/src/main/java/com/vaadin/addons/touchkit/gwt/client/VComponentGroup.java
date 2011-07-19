package com.vaadin.addons.touchkit.gwt.client;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VCaption;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ui.VCssLayout;

public class VComponentGroup extends VCssLayout {

	static {
		TouchKitResources.INSTANCE.css().ensureInjected();
	}

	private DivElement breakElement;
	
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		super.updateFromUIDL(uidl, client);
		ensureBreakElement();
	}

	/**
	 * TODO make this with generated content
	 */
	private void ensureBreakElement() {
		if(breakElement == null) {
			breakElement = Document.get().createDivElement();
			breakElement.getStyle().setProperty("clear", "both");
			breakElement.getStyle().setProperty("height", "0");
		}
		if(getContainerElement().getLastChild() != breakElement) {
			getContainerElement().appendChild(breakElement);
		}
	}

	@Override
	public void updateCaption(Paintable component, UIDL uidl) {
		super.updateCaption(component, uidl);
		((Widget) component).setStyleName("v-tk-has-caption",
				VCaption.isNeeded(uidl));
	}

	@Override
	public RenderSpace getAllocatedSpace(Widget child) {
		/*
		 * 100% wide components use layout width - caption (positioned on the
		 * left side).
		 * 
		 * TODO figure out if this makes sense in the "horizontal" mode
		 */
		RenderSpace allocatedSpace = super.getAllocatedSpace(child);
		int width = allocatedSpace.getWidth();
		FlowPanel parent2 = (FlowPanel) child.getParent();
		int widgetIndex = parent2.getWidgetIndex(child);
		if (widgetIndex > 0) {
			Widget widget2 = parent2.getWidget(widgetIndex - 1);
			if (widget2 instanceof VCaption) {
				VCaption caption = (VCaption) widget2;
				width -= caption.getRequiredWidth();
			}
		}
		return new RenderSpace(width, allocatedSpace.getHeight());
	}
}
