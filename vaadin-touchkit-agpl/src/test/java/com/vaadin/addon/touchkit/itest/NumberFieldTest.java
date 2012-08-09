package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NumberField;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

public class NumberFieldTest extends AbstractTouchKitIntegrationTest {

    public NumberFieldTest() {
        setDescription("This is NumberField test");

        final NumberField nf = new NumberField("NumberField");
        nf.setImmediate(true);
        nf.addListener(new TextChangeListener() {
            public void textChange(TextChangeEvent event) {
                Notification.show("New numberfield value: " + event.getText());
            }
        });

        final TextField tf = new TextField("TextField");
        tf.setImmediate(true);
        tf.addListener(new TextChangeListener() {
            public void textChange(TextChangeEvent event) {
                nf.setValue(event.getText());
            }
        });

        addComponent(tf);
        addComponent(nf);
    }
}
