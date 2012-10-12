package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.TouchComboBox;

public class TouchComboBoxTest extends AbstractTouchKitIntegrationTest {

    public TouchComboBoxTest() {
        setDescription("This is a test for TouchComboBox");

        TouchComboBox defaultComboBox = new TouchComboBox();
        defaultComboBox.setWidth("15em");
        defaultComboBox.setCaption("Normal comboBox with 20 items");
        addItems(defaultComboBox);

        TouchComboBox withNullSelectIdComboBox = new TouchComboBox();
        withNullSelectIdComboBox.setWidth("15em");
        withNullSelectIdComboBox
                .setCaption("ComboBox with 20 items, NullSelectItem 'Item 10'");
        withNullSelectIdComboBox.setNullSelectionItemId("Item 10");
        addItems(withNullSelectIdComboBox);

        TouchComboBox noNullComboBox = new TouchComboBox();
        noNullComboBox.setWidth("15em");
        noNullComboBox
                .setCaption("ComboBox with 20 items, null selection not allowed");
        noNullComboBox.setNullSelectionAllowed(false);
        addItems(noNullComboBox);

        addComponent(defaultComboBox);
        addComponent(withNullSelectIdComboBox);
        addComponent(noNullComboBox);
    }

    private void addItems(TouchComboBox comboBox) {
        for (int i = 1; i <= 2000; i++) {
            comboBox.addItem("Item " + i);
            // comboBox.setItemIcon("Item " + i, new ThemeResource(
            // "runo/icons/16/arrow-down.png"));
        }
    }
}
