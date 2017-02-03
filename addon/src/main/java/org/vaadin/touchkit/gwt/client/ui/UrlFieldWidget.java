package org.vaadin.touchkit.gwt.client.ui;

import com.vaadin.v7.client.ui.VTextField;

public class UrlFieldWidget extends VTextField {

    public static final String CLASSNAME = "urlfield";

    public UrlFieldWidget() {
        setStyleName(CLASSNAME);
        getElement().setPropertyString("type", "url");
    }
}
