package org.vaadin.touchkit.itest;

import java.net.MalformedURLException;
import java.net.URL;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.UrlField;

import com.vaadin.v7.event.FieldEvents.TextChangeEvent;
import com.vaadin.v7.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextField;

public class UrlFieldTest extends AbstractTouchKitIntegrationTest {
    public UrlFieldTest() {
        setDescription("This is UrlField test");

        final UrlField nf = new UrlField("UrlField");
        nf.setStyleName("urlfield");
        nf.setImmediate(true);
        nf.addTextChangeListener(new TextChangeListener() {
            public void textChange(TextChangeEvent event) {
                try {
                    Notification.show("New urlfield value: " + nf.getUrl());
                } catch (MalformedURLException e) {
                    Notification.show("Not valid: " + e.getMessage());
                }
            }
        });

        final TextField tf = new TextField("TextField");
        tf.setImmediate(true);
        tf.addTextChangeListener(new TextChangeListener() {
            public void textChange(TextChangeEvent event) {
                try {
                    nf.setUrl(new URL(event.getText()));
                } catch (MalformedURLException e) {
                    Notification.show("Not valid: " + e.getMessage());
                }
            }
        });

        addComponent(tf);
        addComponent(nf);
    }
}
