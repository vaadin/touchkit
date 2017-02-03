package org.vaadin.touchkit.gwt.client.ui;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * TouchKit "subwindow". Both for iPad style 'popover' windows, and iPhone style
 * 'fullscreen' (actionsheet) windows.
 * 
 */
public class VPopover extends com.vaadin.client.ui.VWindow {

    private static final int SMALL_SCREEN_WIDTH_THRESHOLD = 500;
    private static final int MIN_EDGE_DISTANCE = 10;
    private static final int MIN_ARROW_EDGE_DISTANCE = 2 * MIN_EDGE_DISTANCE;
    private Widget relComponent;
    private DivElement arrowElement;
    protected int zIndex;

    public VPopover() {
    }

    public void setRelatedComponent(Widget relComponent) {
        this.relComponent = relComponent;
    }

    public void slideIn() {
        /*
         * Make full width.
         */
        setWidth((Window.getClientWidth() + "px"));

        int top = 0;
        if (relComponent != null) {
            boolean isCloseToBottom = relComponent.getAbsoluteTop()
                    - Window.getScrollTop() > Window.getClientHeight() / 2;
            if (isCloseToBottom) {
                top = Window.getClientHeight() - getOffsetHeight();
            }
        }

        setPopupPosition(0, top);
        hideReferenceArrow();
    }

    private Element getWrapperElement() {
        return getElement().getFirstChild().getFirstChild().cast();
    }

    public void showNextTo(Widget paintable) {
        if (paintable == null || !paintable.isAttached()) {
            // Vaadin may call this via setWidth/setHeight when reference
            // paintable don't exist anymore. The window may actually also be
            // about to be removed too. Thus make a sanity check.
            return;
        }
        // attach arrow, we need to measure it during calculations
        attachReferenceArrow();

        final int centerOfReferencComponent = paintable.getAbsoluteLeft()
                + paintable.getOffsetWidth() / 2 - Window.getScrollLeft();
        /*
         * Arrow below the popup takes precedence, since that way around the
         * user's hand doesn't get in the way.
         */
        boolean arrowBelow = fitsAbove(paintable);
        boolean arrowAbove = !arrowBelow && fitsBelow(paintable);

        /*
         * show arrow that shows related component (similar to ios on ipad),
         * unless we needed to draw on top of the related widget.
         */
        if (arrowAbove || arrowBelow) {
            showReferenceArrow(arrowBelow, centerOfReferencComponent, paintable);
        } else {
            hideReferenceArrow();
        }

        int left = 0;

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

        int top = paintable.getAbsoluteTop() - Window.getScrollTop();

        if (arrowAbove) {
            top += paintable.getOffsetHeight();
            top += arrowElement.getOffsetHeight();
        } else if (arrowBelow) {
            top -= getOffsetHeight();
            top -= arrowElement.getOffsetHeight();
        } else {
            top = 0;
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

    private void hideReferenceArrow() {
        if (arrowElement != null && arrowElement.getParentElement() != null) {
            arrowElement.removeFromParent();
        }
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

    private boolean fitsBelow(Widget paintable) {
        final int spaceBelow = Window.getClientHeight()
                - (paintable.getAbsoluteTop() + paintable.getOffsetHeight() - Window
                        .getScrollTop());
        final int requiredHeight = getOffsetHeight()
                + arrowElement.getOffsetHeight();
        return spaceBelow > requiredHeight;
    }

    private boolean fitsAbove(Widget paintable) {
        final int spaceOntop = paintable.getAbsoluteTop()
                - Window.getScrollTop();
        final int requiredHeight = getOffsetHeight()
                + arrowElement.getOffsetHeight();
        return spaceOntop > requiredHeight;
    }

    public static boolean isSmallScreenDevice() {
        return Window.getClientWidth() < SMALL_SCREEN_WIDTH_THRESHOLD;
    }

    @Override
    public void center() {
        // Ignore centering if the popover is relative to component, or we're on
        // a small screen device
        if (relComponent == null && !isSmallScreenDevice()) {
            super.center();
        }
    }

    @Override
    public void hide() {
        if (arrowElement != null && arrowElement.getParentElement() != null) {
            arrowElement.removeFromParent();
        }
        super.hide();
    }

    /* Make the methods below visible in this package */

    @Override
    public boolean isClosable() {
        return super.isClosable();
    }

    @Override
    public Element getModalityCurtain() {
        return super.getModalityCurtain();
    }

}
