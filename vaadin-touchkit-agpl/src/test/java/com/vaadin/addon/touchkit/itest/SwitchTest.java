package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;

public class SwitchTest extends AbstractTouchKitIntegrationTest {

    public SwitchTest() {
        setDescription("This is Switch test");

        boolean initialState = true;

        // Label to display the value of the Switch.
        final Label statusLabel = new Label(Boolean.toString(initialState));
        statusLabel.setCaption("Current Switch value:");
        statusLabel.setId("statusLabel");

        // Normal Switch that can be toggled.
        final Switch switchComponent = new Switch("Switch", initialState);
        switchComponent.setId("switchComponent");
        switchComponent.setImmediate(true);
        switchComponent.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                statusLabel.setValue(event.getProperty().getValue().toString());
            }
        });

        // Disabled Switch that cannot be interacted with.
        final Switch disabledSwitch = new Switch("Disabled Switch");
        disabledSwitch.setEnabled(false);

        Button toggleButton = new Button("Toggle Switch value",
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        switchComponent.setValue(!switchComponent.getValue());
                    }
                });
        toggleButton.setId("toggleButton");

        addComponent(switchComponent);
        addComponent(disabledSwitch);
        addComponent(statusLabel);
        addComponent(toggleButton);
    }

}
