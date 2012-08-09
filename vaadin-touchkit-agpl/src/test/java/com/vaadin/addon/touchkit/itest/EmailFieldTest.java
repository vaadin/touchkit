package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

public class EmailFieldTest extends AbstractTouchKitIntegrationTest {

    public EmailFieldTest() {
        setDescription("This is EmailField test");

        final EmailField nf = new EmailField("EmailField");
        nf.setImmediate(true);
        nf.addListener(new TextChangeListener() {
            public void textChange(TextChangeEvent event) {
                Notification.show("New emailfield value: " + event.getText());
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
