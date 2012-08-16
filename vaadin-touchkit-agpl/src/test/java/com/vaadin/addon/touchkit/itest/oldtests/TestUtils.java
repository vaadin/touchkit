package com.vaadin.addon.touchkit.itest.oldtests;

import org.junit.Ignore;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.ComponentContainer;

@Ignore
public class TestUtils {
    public static void makeSmallTabletSize(ComponentContainer c) {
        c.setWidth(450, Sizeable.UNITS_PIXELS);
        c.setHeight(640, Sizeable.UNITS_PIXELS);
    }

}
