package com.vaadin.addon.touchkit.gwt.client;

import com.vaadin.client.ui.textfield.VTextField;

public class NumberFieldWidget extends VTextField {

    public static final String CLASSNAME = "v-touchkit-numberfield";

    public NumberFieldWidget() {
        setStyleName(CLASSNAME);
        getElement().setPropertyString("type", "number");
    }

}
