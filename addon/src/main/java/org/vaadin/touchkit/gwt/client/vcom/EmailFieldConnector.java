package org.vaadin.touchkit.gwt.client.vcom;

import org.vaadin.touchkit.gwt.client.ui.EmailFieldWidget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.client.ui.textfield.TextFieldConnector;

@Connect(org.vaadin.touchkit.ui.EmailField.class)
public class EmailFieldConnector extends TextFieldConnector {

    @Override
    protected Widget createWidget() {
        return GWT.create(EmailFieldWidget.class);
    }

    @Override
    public EmailFieldWidget getWidget() {
        return (EmailFieldWidget) super.getWidget();
    }
}
