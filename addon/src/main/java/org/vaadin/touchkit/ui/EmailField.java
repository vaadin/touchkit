package org.vaadin.touchkit.ui;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.TextField;

/**
 * A {@link TextField} that is intended for email address entry.
 * <p>
 * Modern browsers and devices provide a better interface for entering email
 * addresses when this field is used. This is especially important for touch
 * devices that can provide an on-screen keyboard optimized for email address
 * entry.
 * </p>
 */
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
    public EmailField(Property<String> dataSource) {
        super(dataSource);
    }

    /**
     * @see TextField#TextField(String, Property)
     */
    public EmailField(String caption, Property<String> dataSource) {
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
