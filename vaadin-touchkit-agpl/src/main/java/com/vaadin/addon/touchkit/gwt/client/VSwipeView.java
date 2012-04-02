package com.vaadin.addon.touchkit.gwt.client;

import java.util.Date;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ui.TouchScrollDelegate;
import com.vaadin.terminal.gwt.client.ui.VCssLayout;

public class VSwipeView extends VCssLayout implements Container, ScrollHandler {

    private static final double SPEED_THRESHOLD = 0.35;
    private static final String CLASSNAME = "v-touchkit-navview";
    private ApplicationConnection client;
    private boolean rendering;
    private int dragstartX;
    private int dragstartY;
    private boolean dragging;
    private boolean swiping;
    private long lastTs;
    private int lastX;
    private double lastSpeed;
    private int lastY;
    private int scrollStart;
    private boolean touchDrag;
    private VNavigationManager np;
    private Element scrollElement;
    private TouchStartEvent dragStartEvent;
    private TouchScrollDelegate touchScrollDelegate;

    public VSwipeView() {
        setStyleName(CLASSNAME);
        getElement().getStyle().setProperty("webkitUserSelect", "none");

        /*
         * Touch scrolling using one finger handled by TouchScrollDelegate.
         */
        sinkEvents(Event.TOUCHEVENTS);
        scrollElement = getElement();
        Style style = scrollElement.getStyle();
        style.setOverflow(Overflow.AUTO);
        style.setHeight(100, Unit.PCT);
        style.setPosition(Position.ABSOLUTE);
        sinkEvents(Event.MOUSEEVENTS);
        DOM.sinkEvents(scrollElement, Event.ONSCROLL);
        touchScrollDelegate = new TouchScrollDelegate(scrollElement);

        addHandler(this, ScrollEvent.getType());

        addHandler(new TouchStartHandler() {
            public void onTouchStart(TouchStartEvent event) {
                dragStartEvent = event;
                dragStart(event);
            }
        }, TouchStartEvent.getType());

        addHandler(new MouseDownHandler() {

            public void onMouseDown(MouseDownEvent event) {
                if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
                    dragStart(event);
                }
            }
        }, MouseDownEvent.getType());

        addHandler(new MouseMoveHandler() {

            public void onMouseMove(MouseMoveEvent event) {
                dragMove(event);
            }
        }, MouseMoveEvent.getType());

        addHandler(new TouchMoveHandler() {

            public void onTouchMove(TouchMoveEvent event) {
                dragMove(event);
            }
        }, TouchMoveEvent.getType());

        addHandler(new MouseUpHandler() {

            public void onMouseUp(MouseUpEvent event) {
                dragEnd(event);
            }
        }, MouseUpEvent.getType());

        addHandler(new TouchEndHandler() {

            public void onTouchEnd(TouchEndEvent event) {
                dragEnd(event);
            }
        }, TouchEndEvent.getType());
    }

    protected void dragStart(HumanInputEvent event) {
        NativeEvent ne = event.getNativeEvent();
        VConsole.log("Drag start" + ne.getType());
        if (!dragging && np != null && !client.hasActiveRequest()) {
            dragging = true;
            touchDrag = Event.as(ne).getTypeInt() == Event.ONTOUCHSTART;
            dragstartX = Util.getTouchOrMouseClientX(ne);
            dragstartY = Util.getTouchOrMouseClientY(ne);
            scrollStart = scrollElement.getScrollTop();
            if (!BrowserInfo.get().isTouchDevice()) {
                // avoid drag start on images
                // FIXME shouln't be this way, but disables dragstart on images
                // in demo
                Element el = ne.getEventTarget().cast();

                String msg = el.getParentElement().getClassName();
                if (msg.contains("embedded")) {
                    ne.preventDefault();
                }
            }
        }
    }

    protected void dragMove(HumanInputEvent event) {
        if (np != null) {
            NativeEvent ne = event.getNativeEvent();
            if (touchDrag && Event.as(ne).getTypeInt() != Event.ONTOUCHMOVE) {
                return;
            }
            int x = Util.getTouchOrMouseClientX(ne);
            int y = Util.getTouchOrMouseClientY(ne);
            long time = new Date().getTime();
            // screens per second
            double screenwidths = (x - lastX) / (double) getOffsetWidth();
            double seconds = (time - lastTs) / 100d;
            lastSpeed = screenwidths / seconds;
            lastX = x;
            lastY = y;
            lastTs = time;
            int deltaX = x - dragstartX;
            if (swiping) {
                VConsole.log("Swipe move " + deltaX);
                np.setHorizontalOffset(deltaX, false);
            } else if (dragging) {
                Event.setCapture(getElement());
                int dragY = dragstartY - y;
                if (Math.abs(deltaX / (double) dragY) > 2) {
                    swiping = true;
                    np.setHorizontalOffset(deltaX, false);
                }
                if (Math.abs(deltaX / (double) dragY) < 0.5) {
                    if (Event.as(event.getNativeEvent()).getTypeInt() == Event.ONTOUCHMOVE) {
                        /*
                         * We'll "lazyly" activate touchScrollDelegate if the
                         * direction is enough down.
                         */
                        dragStartEvent.setNativeEvent(event.getNativeEvent());
                        touchScrollDelegate.onTouchStart(dragStartEvent);
                        VConsole.log("Lazy started");
                        dragging = false;
                    }
                }
            }
            ne.preventDefault(); // prevent page scroll
        }
    }

    private void setScrollOffset(int scrolloffset) {
        scrollElement.setScrollTop(scrolloffset);
    }

    protected void dragEnd(HumanInputEvent event) {
        if (dragging) {
            Event.releaseCapture(getElement());
            VConsole.log("Drag end");
            dragging = false;
            if (swiping) {
                if (np != null) {
                    NativeEvent ne = event.getNativeEvent();
                    int x = Util.getTouchOrMouseClientX(ne);
                    int deltaX = x - dragstartX;
                    VConsole.log("Speed" + lastSpeed);
                    if (np.getPreviousView() != null
                            && (deltaX > getOffsetWidth() / 2 || lastSpeed > SPEED_THRESHOLD)) {
                        // navigate backward
                        np.navigateBackward(true);
                    } else if (np.getNextView() != null
                            && (deltaX < -getOffsetWidth() / 2 || (lastSpeed < -SPEED_THRESHOLD))) {
                        // navigate forward
                        np.navigateForward(true);
                    } else {
                        np.setHorizontalOffset(0, true);
                    }
                }
                swiping = false;
            }
        }
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        rendering = true;
        this.client = client;
        super.updateFromUIDL(uidl, client);
        Util.runWebkitOverflowAutoFix(getContainerElement());
        rendering = false;
        np = VNavigationButton.findNavigationPanel(this);
    }

    public void onScroll(ScrollEvent event) {
        if (client != null && isAttached()) {
            client.updateVariable(client.getPid(this), "sp",
                    getContainerElement().getScrollTop(), false);
        }
    }

}
