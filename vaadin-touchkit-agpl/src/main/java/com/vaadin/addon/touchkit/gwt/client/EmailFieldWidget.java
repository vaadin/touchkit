package com.vaadin.addon.touchkit.gwt.client;

import com.vaadin.client.ui.VTextField;

public class EmailFieldWidget extends VTextField {

    public EmailFieldWidget() {
        getElement().setPropertyString("type", "email");
        getElement().setAttribute("autocapitalize", "off");
        getElement().setAttribute("autocorrect", "off");
    }

}
