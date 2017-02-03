package org.vaadin.touchkit.ui;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.TextField;

/**
 * A {@link TextField} that is intended for numerical input.
 * <p>
 * Modern browsers and devices provide a better interface for entering numbers
 * when this field is used. This is especially important for touch devices that
 * can provide an on-screen keyboard optimized for numerical input.
 * </p>
 */
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
    public NumberField(Property<?> dataSource) {
        super(dataSource);
    }

    /**
     * @see TextField#TextField(String, Property)
     */
    public NumberField(String caption, Property<?> dataSource) {
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
