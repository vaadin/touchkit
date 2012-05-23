package com.vaadin.addon.touchkit.itest;

import org.junit.Ignore;

import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.ui.Component;

@Ignore
public class AbstractTouchKitIntegrationTest extends TouchKitWindow {
    
    public static void makeSmallTabletSize(Component c) {
        c.setWidth(450, UNITS_PIXELS);
        c.setHeight(640, UNITS_PIXELS);
    }
}
