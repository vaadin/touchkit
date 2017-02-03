package org.vaadin.touchkit.itest.extensions;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.extensions.LocalStorage;
import org.vaadin.touchkit.extensions.LocalStorageCallback;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextField;

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
                                new LocalStorageCallback() {
                                    @Override
                                    public void onSuccess(String value) {
                                        Notification.show("Value received:" + value);
                                        valueField.setValue(value);
                                    }

                                    @Override
                                    public void onFailure(FailureEvent error) {
                                        Notification.show("Value retrieval failed: " + error.getMessage());
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
        
        Button saveInsaneButton = new Button("Save huge value (should fail)",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        LocalStorage.get().put("huge",
                                gethugeString(), new LocalStorageCallback() {
                                    
                                    @Override
                                    public void onSuccess(String value) {
                                        Notification.show("Succeeeded");
                                    }
                                    
                                    @Override
                                    public void onFailure(FailureEvent error) {
                                        Notification.show("Failed as expected:" + error.getMessage());
                                    }
                                });
                    }

                    private String gethugeString() {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < 8000000; i++) {
                            stringBuilder.append("diipadaipa");
                        }
                        return stringBuilder.toString();
                    }
                });
        addComponent(saveInsaneButton);

    }
}
