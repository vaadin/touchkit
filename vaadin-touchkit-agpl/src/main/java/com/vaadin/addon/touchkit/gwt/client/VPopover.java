package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VWindow;

/**
 * TouchKit "subwindow". Both for iPad style 'popover' windows, and iPhone style
 * 'fullscreen' (actionsheet) windows.
 * 
 */
public class VPopover extends VWindow {

    private static final int SMALL_SCREEN_WIDTH_THRESHOLD = 500;
    private static final int MIN_EDGE_DISTANCE = 10;
    private static final int MIN_ARROW_EDGE_DISTANCE = 2 * MIN_EDGE_DISTANCE;
    private String relComponentId;
    private boolean fullscreen;
    private DivElement arrowElement;
    protected int zIndex;
    private boolean specialPositioningRunning;

    public VPopover() {
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (!uidl.hasAttribute("cached")) {
            relComponentId = uidl.hasAttribute("rel") ? uidl
                    .getStringAttribute("rel") : null;
            setFullscreen(uidl.hasAttribute("fc"));
        }
        super.updateFromUIDL(uidl, client);
    }

    @Override
    public void setStyleName(String style) {
        setShadowEnabled(false);
        if (fullscreen) {
            // fullscreen window
            style += " v-touchkit-popover v-touchkit-fullscreen";
            getModalityCurtain().addClassName("fullscreen");
        } else if (relComponentId != null) {
            // real popover (black)
            style += " v-touchkit-popover v-touchkit-relative";
            getModalityCurtain().addClassName("relative");
        } else {
            // regular (white)
            style += " v-touchkit-popover v-touchkit-plain";
        }
        super.setStyleName(style);
    }

    @Override
    protected void updateShadowSizeAndPosition() {
        doSpecialPositioning();
        super.updateShadowSizeAndPosition();
    }

    private void doSpecialPositioning() {
        /*
         * FIXME this is currently called twice when the window is opened, from
         * setWidth/height and finally from the super.updateFromUidl
         */
        if (!specialPositioningRunning) {
            specialPositioningRunning = true;
            if (isFullScreen()) {
                setPopupPosition(0, 0);
            } else {
                /*
                 * fade in the modality curtain unless in fullscreen mode.
                 */
                getModalityCurtain().removeClassName(
                        "v-touchkit-opacity-transition");
                DOM.sinkEvents(getModalityCurtain(), Event.TOUCHEVENTS);
                final Style style = getModalityCurtain().getStyle();
                style.setOpacity(0);
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                    public void execute() {
                        getModalityCurtain().addClassName(
                                "v-touchkit-opacity-transition");
                        /* Final value from the theme */
                        style.setProperty("opacity", "");
                    }
                });

                if (isSmallScreenDevice()) {
                    slideIn();
                } else if (relComponentId != null) {
                    Widget paintable = (Widget) client
                            .getPaintable(relComponentId);
                    showNextTo(paintable);
                }
            }
            specialPositioningRunning = false;
        }
    }

    private void slideIn() {
        /*
         * Make full width.
         */
        setWidth((Window.getClientWidth() + "px"));

        int top = 0;
        if (relComponentId != null) {
            Widget paintable = (Widget) client.getPaintable(relComponentId);
            boolean isCloseToBottom = paintable.getAbsoluteTop()
                    - Window.getScrollTop() > Window.getClientHeight() / 2;
            if (isCloseToBottom) {
                top = Window.getClientHeight() - getOffsetHeight();
            }
        }

        setPopupPosition(0, top);

        /*
         * TODO Animation. If relative element is below middle point, slide from
         * bottom else slide from top.
         */
    }

    private void showNextTo(Widget paintable) {
        if (paintable == null || !paintable.isAttached()) {
            // Vaadin may call this via setWidth/setHeight when reference
            // paintable don't exist anymore. The window may actually also be
            // about to be removed too. Thus make a sanity check.
            return;
        }
        // attach arrow, we need to measure it during calculations
        attachReferenceArrow();

        int top, left = 0;

        final int centerOfReferencComponent = paintable.getAbsoluteLeft()
                + paintable.getOffsetWidth() / 2 - Window.getScrollLeft();
        /*
         * We prefer setting the popup on top as users hand does not hide it
         * there
         */
        boolean onTop = true;
        if (alignToTop(paintable)) {
            top = paintable.getAbsoluteTop() - getOffsetHeight();
        } else {
            onTop = false;
            top = paintable.getAbsoluteTop() + paintable.getOffsetHeight();
        }

        /*
         * show arrow that shows related component (similar to ios on ipad),
         * unless we needed to draw on top of the related widget.
         */
        showReferenceArrow(onTop, centerOfReferencComponent, paintable);

        // fix by scroll offset
        top -= Window.getScrollTop();

        if (getOffsetWidth() < Window.getClientWidth()) {
            left = centerOfReferencComponent - getOffsetWidth() / 2;
            /*
             * Ensure the window is totally on screen.
             */
            if (left - MIN_EDGE_DISTANCE < 0) {
                left = MIN_EDGE_DISTANCE;
            } else if (left + getOffsetWidth() + MIN_EDGE_DISTANCE > Window
                    .getClientWidth()) {
                left = Window.getClientWidth() - getOffsetWidth()
                        - MIN_EDGE_DISTANCE;
            }

        }

        if (onTop) {
            top -= arrowElement.getOffsetHeight();
        } else {
            top += arrowElement.getOffsetHeight();
        }
        setPopupPosition(left, top);

    }

    private void showReferenceArrow(boolean onTop,
            int centerOfReferencComponent, Widget ref) {
        UIObject.setStyleName(arrowElement,
                "v-touchkit-popover-pointer-bottom", !onTop);
        UIObject.setStyleName(arrowElement, "v-touchkit-popover-pointer", onTop);

        int horizontalpoint = centerOfReferencComponent
                - arrowElement.getOffsetWidth() / 2;
        if (horizontalpoint - MIN_ARROW_EDGE_DISTANCE < 0) {
            horizontalpoint = MIN_ARROW_EDGE_DISTANCE;
        } else if (horizontalpoint + arrowElement.getOffsetWidth()
                + MIN_ARROW_EDGE_DISTANCE > Window.getClientWidth()) {
            horizontalpoint = Window.getClientWidth()
                    - arrowElement.getOffsetWidth() - MIN_ARROW_EDGE_DISTANCE;
        }

        arrowElement.getStyle().setLeft(horizontalpoint, Unit.PX);
        int arrowPos = ref.getAbsoluteTop();
        if (onTop) {
            arrowPos -= arrowElement.getOffsetHeight();
        } else {
            arrowPos += ref.getOffsetHeight();
        }
        arrowElement.getStyle().setTop(arrowPos, Unit.PX);
        arrowElement.getStyle().setProperty("opacity", "");
        arrowElement.getStyle().setZIndex(zIndex);

    }

    private void attachReferenceArrow() {
        if (arrowElement == null) {
            arrowElement = Document.get().createDivElement();
        }
        arrowElement.getStyle().setOpacity(0);
        RootPanel.getBodyElement().appendChild(arrowElement);
    }

    @Override
    protected void setZIndex(int zIndex) {
        super.setZIndex(zIndex);
        // Store zIndex for later use. set arrow zIndex if available
        this.zIndex = zIndex;
        if (arrowElement != null) {
            arrowElement.getStyle().setZIndex(zIndex);
        }
    }

    /**
     * @param paintable
     *            the reference aside which the window should be positioned to
     * @return true if there is enough on top of the page from the component or
     *         if the component don't fit on below either
     */
    private boolean alignToTop(Widget paintable) {
        final int spaceOntop = paintable.getAbsoluteTop()
                - Window.getScrollTop();
        final int requiredHeight = getOffsetHeight()
                + arrowElement.getOffsetHeight();
        if (spaceOntop > requiredHeight) {
            return true;
        }
        final int spaceBelow = Window.getClientHeight()
                - (paintable.getAbsoluteTop() + paintable.getOffsetHeight() - Window
                        .getScrollTop());
        return spaceBelow < requiredHeight;
    }

    private static boolean isSmallScreenDevice() {
        return Window.getClientWidth() < SMALL_SCREEN_WIDTH_THRESHOLD;
    }

    private boolean isFullScreen() {
        return fullscreen;
    }

    @Override
    public void center() {
        // Ignore centering if the popover is relative to component, or we're on
        // a small screen device
        if (relComponentId == null && !isSmallScreenDevice()) {
            super.center();
        }
    }

    private final static int ACCEPTEDEVENTS = Event.MOUSEEVENTS;

    @Override
    public boolean onEventPreview(Event event) {
        boolean superAccepts = super.onEventPreview(event);
        if (isClosable() && !superAccepts
                && (event.getTypeInt() & ACCEPTEDEVENTS) == 0) {
            /*
             * Close on events outside window. Special handling for mousemove
             * etc to aid compatibility with desktop (testing purposes).
             */
            client.updateVariable(client.getPid(this), "close", true, true);
        }
        return superAccepts;
    }

    @Override
    public void hide() {
        if (arrowElement != null && arrowElement.getParentElement() != null) {
            arrowElement.removeFromParent();
        }
        super.hide();
    }

}
