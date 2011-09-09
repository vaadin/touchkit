package com.vaadin.addon.touchkit.ui;

import com.vaadin.addon.touchkit.gwt.client.VNumberField;
import com.vaadin.data.Property;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.TextField;

/**
 * A {@link TextField} that is intended for numerical input.
 * <p>
 * Modern browsers and devices provide a better interface for entering numbers
 * when this field is used. This is especially important for touch devices that
 * can provide a on-screen keyboard customized for numerical input.
 * </p>
 */
@ClientWidget(value = VNumberField.class, loadStyle = LoadStyle.EAGER)
public class NumberField extends TextField {

    /**
     * @see TextField#TextField()
     */
    public NumberField() {
        super();
    }

    /**
     * @see TextField#TextField(Property)
     */
    public NumberField(Property dataSource) {
        super(dataSource);
    }

    /**
     * @see TextField#TextField(String, Property)
     */
    public NumberField(String caption, Property dataSource) {
        super(caption, dataSource);
    }

    /**
     * @see TextField#TextField(String, String)
     */
    public NumberField(String caption, String value) {
        super(caption, value);
    }

    /**
     * @see TextField#TextField(String)
     */
    public NumberField(String caption) {
        super(caption);
    }

}
