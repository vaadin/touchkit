package com.vaadin.addon.touchkit.gwt.client;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VCaption;
import com.vaadin.terminal.gwt.client.ui.VCssLayout;

public class VVerticalComponentGroup extends VCssLayout {

    private DivElement breakElement;

    private ArrayList<Paintable> componentsWithCaptions;

    private boolean rendering;

    private ApplicationConnection client;

    public VVerticalComponentGroup() {
        addStyleName("v-touchkit-verticalcomponentgroup");
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        rendering = true;
        if (!uidl.hasAttribute("cached")) {
            componentsWithCaptions = new ArrayList<Paintable>();
        }
        super.updateFromUIDL(uidl, client);
        this.client = client;
        ensureBreakElement();
        checkCaptionWidths();
        rendering = false;
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        if (!rendering) {
            checkCaptionWidths();
        }
    }

    /**
     * TODO make this with generated content
     */
    private void ensureBreakElement() {
        if (breakElement == null) {
            breakElement = Document.get().createDivElement();
            breakElement.getStyle().setProperty("clear", "both");
            breakElement.getStyle().setProperty("height", "0");
        }
        if (getContainerElement().getLastChild() != breakElement) {
            getContainerElement().appendChild(breakElement);
        }
    }

    @Override
    public void updateCaption(Paintable component, UIDL uidl) {
        super.updateCaption(component, uidl);
        boolean captionNeeded = VCaption.isNeeded(uidl);
        ((Widget) component).setStyleName("v-touchkit-has-caption",
                captionNeeded);
        if (captionNeeded) {
            componentsWithCaptions.add(component);
        }
    }

    /**
     * Forces caption width for non relatively sized components so that caption
     * can fit on left side of the component. Still, if the component itself
     * leaves less that 100px for the caption the component is left to float on
     * the next line.
     * 
     * TODO split client side imple from VCssLayout, put caption in DOM after
     * the component, implement this method with few lines of css
     */
    private void checkCaptionWidths() {
        int availableSpace = getContainerElement().getFirstChildElement()
                .getOffsetWidth();
        FlowPane pane = (FlowPane) getWidget();
        for (Paintable paintableWithPendingCaptionWidthCheck : componentsWithCaptions) {
            if (paintableWithPendingCaptionWidthCheck != null) {
                checkCaptionWidth(availableSpace, pane,
                        paintableWithPendingCaptionWidthCheck);
            }
        }
    }

    private void checkCaptionWidth(int availableSpace, FlowPane pane,
            Paintable paintableWithPendingCaptionWidthCheck) {
        Widget c = (Widget) paintableWithPendingCaptionWidthCheck;
        int captionIndex = pane.getWidgetIndex(c) - 1;
        if (captionIndex < 0) {
            return;
        }
        int offsetWidth = c.getOffsetWidth();

        Widget component = pane.getWidget(captionIndex);
        if (component instanceof VCaption) {
            VCaption caption = (VCaption) component;
            int requiredWidth = caption.getRequiredWidth();
            int availableForCaption = availableSpace - offsetWidth;
            if (requiredWidth > availableForCaption
                    && availableForCaption > 100) {
                // clip
                caption.setMaxWidth((availableSpace - offsetWidth) - 10);
            } else {
                caption.setMaxWidth(requiredWidth);
            }
        }
    }

    @Override
    public boolean requestLayout(Set<Paintable> children) {

        int availableSpace = getContainerElement().getFirstChildElement()
                .getOffsetWidth();
        FlowPane pane = (FlowPane) getWidget();

        for (Paintable paintable : children) {
            client.handleComponentRelativeSize((Widget) paintable);
            checkCaptionWidth(availableSpace, pane, paintable);
        }

        return super.requestLayout(children);
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

    /**
     * @return the panel that actually contains all child widgets.
     */
    public Panel getContainerPanel() {
        return (Panel) getWidget();
    }

    /**
     * Helper method to ease usage as standalone GWT widget. This method add the
     * given widget to the container panel.
     * 
     * @see #getContainerPanel()
     */
    @Override
    public void add(Widget child) {
        getContainerPanel().add(child);
    }

}
