package com.vaadin.addon.touchkit.gwt.client;

import com.vaadin.client.ui.textfield.VTextField;

public class UrlFieldWidget extends VTextField {

    public static final String CLASSNAME = "urlfield";

    public UrlFieldWidget() {
        setStyleName(CLASSNAME);
        getElement().setPropertyString("type", "url");
    }
}
