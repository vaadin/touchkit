package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.gwt.client.theme.StyleNames;
import com.vaadin.ui.Button;

public class ColorButtonsTest extends AbstractTouchKitIntegrationTest {

    public ColorButtonsTest() {
        Button normal = new Button("Normal");
        addComponent(normal);

        Button red = new Button("Red");
        red.addStyleName(StyleNames.BUTTON_RED);
        addComponent(red);

        Button green = new Button("Green");
        green.addStyleName(StyleNames.BUTTON_GREEN);
        addComponent(green);

        Button blue = new Button("Blue");
        blue.addStyleName(StyleNames.BUTTON_BLUE);
        addComponent(blue);
    }
}
