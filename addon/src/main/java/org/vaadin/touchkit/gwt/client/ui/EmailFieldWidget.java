package org.vaadin.touchkit.gwt.client.ui;

import com.vaadin.v7.client.ui.VTextField;

public class EmailFieldWidget extends VTextField {

    public EmailFieldWidget() {
        getElement().setPropertyString("type", "email");
        getElement().setAttribute("autocapitalize", "off");
        getElement().setAttribute("autocorrect", "off");
    }

}
