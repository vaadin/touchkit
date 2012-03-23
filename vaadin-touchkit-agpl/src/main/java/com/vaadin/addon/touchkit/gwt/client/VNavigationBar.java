package com.vaadin.addon.touchkit.gwt.client;

import java.util.Set;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;

public class VNavigationBar extends ComplexPanel implements Container {

    private static final String CLASSNAME = "v-touchkit-navbar";
    private ApplicationConnection client;
    private DivElement caption = Document.get().createDivElement();
    private DivElement rightComponentElement = Document.get()
            .createDivElement();
    private DivElement leftComponentElement = Document.get().createDivElement();
    private Paintable leftComponent;
    private Paintable rightComponent;
    private boolean rendering;
    private int captionWidth;

    public VNavigationBar() {
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
        getElement().appendChild(caption);
        caption.setClassName(CLASSNAME + "-caption");
        rightComponentElement.setClassName(CLASSNAME + "-right");
        getElement().appendChild(rightComponentElement);
        leftComponentElement.setClassName(CLASSNAME + "-left");
        getElement().appendChild(leftComponentElement);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        rendering = true;
        this.client = client;
        if (client.updateComponent(this, uidl, false)) {
            rendering = false;
            return;
        }

        if (hasAbsolutelyPositionedCaption()) {
            caption.getStyle().setProperty("left", "0");
            caption.getStyle().setProperty("right", "");
        }
        setCaption(uidl.getStringAttribute("caption"));

        UIDL backUidl = uidl.getChildByTagName("back");
        if (backUidl == null && leftComponent != null) {
            ((Widget) leftComponent).removeFromParent();
            client.unregisterPaintable(leftComponent);
            leftComponent = null;
        }
        if (backUidl != null) {
            UIDL backButtonUidl = backUidl.getChildUIDL(0);
            Paintable newBackButton = client.getPaintable(backButtonUidl);
            if (leftComponent != null && leftComponent != newBackButton) {
                clearBackComponent();
            }
            leftComponent = newBackButton;
            if (!((Widget) leftComponent).isAttached()) {
                add((Widget) leftComponent,
                        (com.google.gwt.user.client.Element) leftComponentElement
                                .cast());
            }
            leftComponent.updateFromUIDL(backButtonUidl, client);
        } else if (leftComponent != null) {
            clearBackComponent();
        }

        UIDL componentUidl = uidl.getChildByTagName("component");
        boolean hasComponent = componentUidl != null;
        rightComponentElement.getStyle().setDisplay(
                hasComponent ? Display.BLOCK : Display.NONE);
        if (hasComponent) {
            componentUidl = componentUidl.getChildUIDL(0);
            Paintable paintable = client.getPaintable(componentUidl);
            if (rightComponent != paintable && rightComponent != null) {
                clearComponent();
            }

            rightComponent = paintable;
            if (!(((Widget) rightComponent)).isAttached()) {
                add((Widget) rightComponent,
                        (com.google.gwt.user.client.Element) rightComponentElement
                                .cast());
            }

            rightComponent.updateFromUIDL(componentUidl, client);

        } else if (rightComponent != null) {
            clearComponent();
        }

        avoidCaptionOverlap();
        rendering = false;
    }

    public void setCaption(String stringAttribute) {
        caption.setInnerText(stringAttribute);
        captionWidth = caption.getOffsetWidth(); // cache the caption size
    }

    /**
     * Does some magic to avoid situation where caption is overlapped by
     * components in case the caption is centered to the component. This happens
     * with long caption and big right/left component.
     */
    private void avoidCaptionOverlap() {
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
        // This is just to make resizing to work for development purposes
        if (!rendering) {
            avoidCaptionOverlap();
        }
    }

    private void makeCaptionAbsolutelyPositioned() {
        caption.getStyle().setPosition(Position.ABSOLUTE);
    }

    private void clearBackComponent() {
        ((Widget) leftComponent).removeFromParent();
        client.unregisterPaintable(leftComponent);
        leftComponent = null;
    }

    private void clearComponent() {
        ((Widget) rightComponent).removeFromParent();
        client.unregisterPaintable(rightComponent);
        rightComponent = null;
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

    public void updateCaption(Paintable component, UIDL uidl) {
        // NOP, doesn't support delegated caption rendering.
    }

    public boolean requestLayout(Set<Paintable> children) {
        return true; // always 100% width + fixed height
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        return new RenderSpace(getOffsetWidth(), getOffsetHeight());
    }

}
