package org.vaadin.touchkit.gwt.client.ui;

import com.google.gwt.core.client.GWT;

public class Css3Propertynames {

    public static final Css3Propertynames INSTANCE = GWT
            .create(Css3Propertynames.class);

    protected Css3Propertynames() {
    }

    public static String transitionEnd() {
        return INSTANCE._transitionEnd();
    }

    public static String transition() {
        return INSTANCE._transition();
    }

    public static String transform() {
        return INSTANCE._transform();
    }

    public static String flex() {
        return INSTANCE._flex();
    }

    protected String _flex() {
        return "flex";
    }

    protected String _transitionEnd() {
        return "transitionEnd";
    }

    protected String _transition() {
        return "transition";
    }

    protected String _transform() {
        return "transform";
    }

}
