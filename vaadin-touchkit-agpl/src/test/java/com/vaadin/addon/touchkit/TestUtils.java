package com.vaadin.addon.touchkit;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;

public class TestUtils {
    public static void makeSmallTabletSize(Component c) {
        c.setWidth(450, Sizeable.UNITS_PIXELS);
        c.setHeight(640, Sizeable.UNITS_PIXELS);
    }

}
