package com.vaadin.addons.touchkit.gwt.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VCaption;
import com.vaadin.terminal.gwt.client.ui.VCssLayout;

public class VOptionLayout extends VCssLayout {

    static {
        TouchKitResources.INSTANCE.css().ensureInjected();
    }

    @Override
    public void setStyleName(String style) {
        super.setStyleName(style + " v-tk-componentgroup");
    }

    @Override
    public void updateCaption(Paintable component, UIDL uidl) {
        super.updateCaption(component, uidl);
        ((Widget) component).setStyleName("v-tk-has-caption",
                VCaption.isNeeded(uidl));
    }

    @Override
    public RenderSpace getAllocatedSpace(Widget child) {
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
