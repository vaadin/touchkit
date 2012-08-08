package com.vaadin.addon.touchkit;

import org.junit.Ignore;

import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;

@Ignore
public class AbstractTouchKitIntegrationTest extends CssLayout {
    
    public static void makeSmallTabletSize(Component c) {
        c.setWidth(450, UNITS_PIXELS);
        c.setHeight(640, UNITS_PIXELS);
    }
}
