package org.vaadin.touchkit.ui;

import java.net.MalformedURLException;
import java.net.URL;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.TextField;

/**
 * A {@link TextField} that is intended for URL input.
 * <p>
 * Modern browsers and devices provide a better interface for entering URLs when
 * this field is used. This is especially important for touch devices that can
 * provide an on-screen keyboard optimized for URL input.
 * </p>
 */
public class UrlField extends TextField {
    /**
     * @see TextField#TextField()
     */
    public UrlField() {
        super();
    }

    /**
     * @see TextField#TextField(Property)
     */
    public UrlField(Property dataSource) {
        super(dataSource);
    }

    /**
     * @see TextField#TextField(String, Property)
     */
    public UrlField(String caption, Property dataSource) {
        super(caption, dataSource);
    }

    /**
     * @see TextField#TextField(String, String)
     */
    public UrlField(String caption, String value) {
        super(caption, value);
    }

    /**
     * @see TextField#TextField(String)
     */
    public UrlField(String caption) {
        super(caption);
    }

    /**
     * Convenience getter to make a java.net.URL out of the string.
     * 
     * @return Field value as URL.
     * @throws MalformedURLException
     */
    public URL getUrl() throws MalformedURLException {
        return new URL(getValue());
    }

    /**
     * Convenience setter for java.net.URL
     * 
     * @param url
     *            Field value to be set.
     */
    public void setUrl(URL url) {
        setValue(url.toString());
    }

}
