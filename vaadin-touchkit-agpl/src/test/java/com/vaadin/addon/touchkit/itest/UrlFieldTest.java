package com.vaadin.addon.touchkit.itest;

import java.net.MalformedURLException;
import java.net.URL;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.UrlField;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

public class UrlFieldTest extends AbstractTouchKitIntegrationTest {
    public UrlFieldTest() {
        setDescription("This is UrlField test");

        final UrlField nf = new UrlField("UrlField");
        nf.setImmediate(true);
        nf.addListener(new TextChangeListener() {
            public void textChange(TextChangeEvent event) {
                try {
                    Notification.show("New urlfield value: " + nf.getUrl());
                } catch (MalformedURLException e) {
                    Notification.show("Not valid: "+e.getMessage());
                }
            }
        });

        final TextField tf = new TextField("TextField");
        tf.setImmediate(true);
        tf.addListener(new TextChangeListener() {
            public void textChange(TextChangeEvent event) {
                try {
                    nf.setUrl(new URL(event.getText()));
                } catch (MalformedURLException e) {
                    Notification.show("Not valid: "+e.getMessage());
                }
            }
        });

        addComponent(tf);
        addComponent(nf);
    }
}
