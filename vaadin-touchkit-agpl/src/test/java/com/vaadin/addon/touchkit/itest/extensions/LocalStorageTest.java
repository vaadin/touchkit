package com.vaadin.addon.touchkit.itest.extensions;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.rootextensions.LocalStorage;
import com.vaadin.addon.touchkit.rootextensions.LocalStorageValueCallback;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class LocalStorageTest extends AbstractTouchKitIntegrationTest {

    private TextField keyField = new TextField("Key");
    private TextField valueField = new TextField("Value");

    public LocalStorageTest() {
        setDescription("Test Geolocation");
        
        addComponent(keyField);
        addComponent(valueField);

        Button detectButton = new Button("Detect value",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        LocalStorage.detectValue(keyField.getValue(),
                                new LocalStorageValueCallback() {
                                    @Override
                                    public void onSuccess(String value) {
                                        Notification.show("Value received:" + value);
                                        valueField.setValue(value);
                                    }
                                });
                    }
                });
        addComponent(detectButton);
        Button saveButton = new Button("Save value",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        LocalStorage.get().put(keyField.getValue(),
                                valueField.getValue());
                    }
                });
        addComponent(saveButton);
    }
}
