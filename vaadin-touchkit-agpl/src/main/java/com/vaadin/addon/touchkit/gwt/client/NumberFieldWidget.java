package com.vaadin.addon.touchkit.gwt.client;

import com.vaadin.terminal.gwt.client.ui.textfield.VTextField;

public class NumberFieldWidget extends VTextField {

    public static final String CLASSNAME = "numberfield";

    public NumberFieldWidget() {
        setStyleName(CLASSNAME);
        getElement().setPropertyString("type", "number");
    }

}
