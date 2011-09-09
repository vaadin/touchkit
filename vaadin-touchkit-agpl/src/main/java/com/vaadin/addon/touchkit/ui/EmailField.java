package com.vaadin.addon.touchkit.ui;

import com.vaadin.addon.touchkit.gwt.client.VEmailField;
import com.vaadin.data.Property;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.TextField;

/**
 * A {@link TextField} that is intended for email address entry.
 * <p>
 * Modern browsers and devices provide a better interface for entering email
 * addresses when this field is used. This is especially important for touch
 * devices that can provide a on-screen keyboard customized for email address
 * entry.
 * </p>
 */
@ClientWidget(value = VEmailField.class, loadStyle = LoadStyle.EAGER)
public class EmailField extends TextField {

    /**
     * @see TextField#TextField()
     */
    public EmailField() {
        super();
    }

    /**
     * @see TextField#TextField(Property)
     */
    public EmailField(Property dataSource) {
        super(dataSource);
    }

    /**
     * @see TextField#TextField(String, Property)
     */
    public EmailField(String caption, Property dataSource) {
        super(caption, dataSource);
    }

    /**
     * @see TextField#TextField(String, String)
     */
    public EmailField(String caption, String value) {
        super(caption, value);
    }

    /**
     * @see TextField#TextField(String)
     */
    public EmailField(String caption) {
        super(caption);
    }

}
