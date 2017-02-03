package org.vaadin.touchkit.itest;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;

import com.vaadin.ui.Notification;

public class ComboBoxTest extends AbstractTouchKitIntegrationTest implements ClickListener {
    public ComboBoxTest() {
        setDescription("This is a button test");
        
        ComboBox comboBox = new ComboBox();
        comboBox.addItem("foo");
        comboBox.addItem("bar");
        
        comboBox.setWidth("200px");
        
        addComponent(comboBox);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Notification.show("Clicked");
    }

}
