package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Notification;

public class SwitchTest extends AbstractTouchKitIntegrationTest {

    public SwitchTest() {
        setDescription("This is Switch test");

        final Switch switchComponent = new Switch("Switch", true);
        switchComponent.setImmediate(true);
        switchComponent.addListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Notification.show("New Switch value: "
                        + event.getProperty().getValue());
            }
        });

        final Switch disabledSwitch = new Switch("Disabled Switch");
        disabledSwitch.setEnabled(false);

        addComponent(switchComponent);
        addComponent(disabledSwitch);
    }

}
