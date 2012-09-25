package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.TouchComboBox;

public class TouchComboBoxTest extends AbstractTouchKitIntegrationTest {

    public TouchComboBoxTest() {
        setDescription("This is a test for TouchComboBox");
        TouchComboBox comboBox = new TouchComboBox();

        comboBox.setWidth("15em");
        comboBox.setCaption("button caption");
        for (int i = 1; i <= 20; i++) {
            comboBox.addItem("Item " + i);
        }
        // comboBox.select("Item 20");

        addComponent(comboBox);
    }

}
