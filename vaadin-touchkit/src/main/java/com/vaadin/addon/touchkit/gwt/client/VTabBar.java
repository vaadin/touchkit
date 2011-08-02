package com.vaadin.addon.touchkit.gwt.client;

import java.util.Iterator;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.ui.VCssLayout;

public class VTabBar extends VCssLayout {

    public static final String CLASSNAME = "v-touchkit-tabbar";

    public VTabBar() {
        setStyleName(CLASSNAME);
    }

    private int tabbarheight;

    @Override
    public RenderSpace getAllocatedSpace(Widget child) {
        return new RenderSpace(getOffsetWidth(), getOffsetHeight()
                - getTabbarHeight());
    }

    private int getTabbarHeight() {
        if (tabbarheight == 0) {
            HasWidgets p = (HasWidgets) getWidget();
            Iterator<Widget> iterator = p.iterator();
            while (iterator.hasNext()) {
                Widget next = iterator.next();
                if (next.getStyleName().contains("v-touchkit-toolbar")) {
                    tabbarheight = next.getOffsetHeight();
                    break;
                }
            }
        }
        return tabbarheight;
    }

}
