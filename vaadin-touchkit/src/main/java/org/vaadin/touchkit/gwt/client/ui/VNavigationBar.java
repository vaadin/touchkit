package org.vaadin.touchkit.gwt.client.ui;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class VNavigationBar extends ComplexPanel {

    private static final String CLASSNAME = "v-touchkit-navbar";
    private DivElement caption = Document.get().createDivElement();
    private DivElement rightComponentElement = Document.get()
            .createDivElement();
    private DivElement leftComponentElement = Document.get().createDivElement();
    private Widget leftComponent;
    private Widget rightComponent;
    private SpanElement captionText;
    
    public VNavigationBar() {
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
        getElement().appendChild(caption);
        caption.setClassName(CLASSNAME + "-caption");
        captionText = Document.get().createSpanElement();
        caption.appendChild(captionText);
        rightComponentElement.setClassName(CLASSNAME + "-right");
        getElement().appendChild(rightComponentElement);
        leftComponentElement.setClassName(CLASSNAME + "-left");
        getElement().appendChild(leftComponentElement);
    }

    public void setCaption(String stringAttribute) {
        captionText.setInnerText(stringAttribute);
    }

    /**
     * Does some magic to avoid situation where caption is overlapped by
     * components in case the caption is centered to the component. This happens
     * with long caption and big right/left component.
     */
    public void avoidCaptionOverlap() {
        final int captionWidth = captionText.getOffsetWidth(); // cache the caption size

        int freeLeftCoordinate = leftComponent != null ? leftComponentElement
                .getAbsoluteRight() - getAbsoluteLeft() : 0;

        int freeRightCoordinate = rightComponent != null ? rightComponentElement
                .getAbsoluteLeft() - getAbsoluteLeft()
                : getOffsetWidth();

        int maxCenteredSizeByRightComponent = getOffsetWidth() - 2
                * (getOffsetWidth() - freeRightCoordinate);
        int maxCenteredSizeByLeftComponent = getOffsetWidth() - 2
                * freeLeftCoordinate;

        boolean canBeCentered = captionWidth < maxCenteredSizeByRightComponent
                && captionWidth < maxCenteredSizeByLeftComponent;
        if (canBeCentered) {
            if (hasAbsolutelyPositionedCaption()) {
                makeCenteredCaption();
            }
            return;
        }

        makeCaptionAbsolutelyPositioned();

        boolean fitsWithoutClipping = freeRightCoordinate - freeLeftCoordinate > captionWidth;

        boolean fixLeft = false;
        boolean fixRight = false;
        if (fitsWithoutClipping) {
            if (freeLeftCoordinate < getOffsetWidth() - freeRightCoordinate) {
                fixRight = true;
            } else {
                fixLeft = true;
            }
        } else {
            fixLeft = true;
            fixRight = true;
        }

        caption.getStyle().setProperty("left",
                fixLeft ? freeLeftCoordinate + "px" : "");
        caption.getStyle()
                .setProperty(
                        "right",
                        fixRight ? (getOffsetWidth() - freeRightCoordinate)
                                + "px" : "");
    }

    private void makeCenteredCaption() {
        caption.getStyle().setPosition(Position.STATIC);
    }

    private boolean hasAbsolutelyPositionedCaption() {
        return caption.getStyle().getPosition().equals("absolute");
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
    }

    private void makeCaptionAbsolutelyPositioned() {
        caption.getStyle().setPosition(Position.ABSOLUTE);
    }

    private void clearBackComponent() {
        // ((Widget) leftComponent).removeFromParent();
        // FIXME
        // client.unregisterPaintable(leftComponent);
        // leftComponent = null;
    }

    private void clearComponent() {
        // ((Widget) rightComponent).removeFromParent();
        // FIXME
        // client.unregisterPaintable(rightComponent);
        // rightComponent = null;
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        throw new UnsupportedOperationException();
    }

    public boolean hasChildComponent(Widget component) {
        if (component == leftComponent || component == rightComponent) {
            return true;
        }
        return false;
    }

    // public boolean requestLayout(Set<Paintable> children) {
    // return true; // always 100% width + fixed height
    // }
    //
    // public RenderSpace getAllocatedSpace(Widget child) {
    // return new RenderSpace(getOffsetWidth(), getOffsetHeight());
    // }

    public void setLeftWidget(Widget left) {
        if (left != null) {
            if (leftComponent != left && leftComponent != null) {
                clearBackComponent();
            }

            leftComponent = left;
            if (!leftComponent.isAttached()) {
                add(leftComponent,
                        (com.google.gwt.user.client.Element) leftComponentElement
                                .cast());
            }
        } else if (leftComponent != null) {
            clearBackComponent();
        }
    }

    public void setRightWidget(Widget right) {
        rightComponentElement.getStyle().setDisplay(
                right != null ? Display.BLOCK : Display.NONE);
        if (right != null) {
            if (rightComponent != right && rightComponent != null) {
                clearComponent();
            }

            rightComponent = right;
            if (!rightComponent.isAttached()) {
                add(rightComponent,
                        (com.google.gwt.user.client.Element) rightComponentElement
                                .cast());
            }
        } else if (rightComponent != null) {
            clearComponent();
        }
    }
}
