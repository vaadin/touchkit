package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.EmailField;

import com.vaadin.v7.event.FieldEvents.TextChangeEvent;
import com.vaadin.v7.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextField;

public class EmailFieldTest extends AbstractTouchKitIntegrationTest {

    public EmailFieldTest() {
        setDescription("This is EmailField test");

        final EmailField nf = new EmailField("EmailField");
        nf.setImmediate(true);
        nf.addTextChangeListener(new TextChangeListener() {
            public void textChange(TextChangeEvent event) {
                Notification.show("New emailfield value: " + event.getText());
            }
        });

        final TextField tf = new TextField("TextField");
        tf.setImmediate(true);
        tf.addTextChangeListener(new TextChangeListener() {
            public void textChange(TextChangeEvent event) {
                nf.setValue(event.getText());
            }
        });

        addComponent(tf);
        addComponent(nf);
    }
}
