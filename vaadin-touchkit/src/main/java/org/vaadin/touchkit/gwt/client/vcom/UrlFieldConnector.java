package org.vaadin.touchkit.gwt.client.vcom;

import org.vaadin.touchkit.gwt.client.ui.UrlFieldWidget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.client.ui.textfield.TextFieldConnector;

@Connect(org.vaadin.touchkit.ui.UrlField.class)
public class UrlFieldConnector extends TextFieldConnector {

    @Override
    protected Widget createWidget() {
        return GWT.create(UrlFieldWidget.class);
    }

    @Override
    public UrlFieldWidget getWidget() {
        return (UrlFieldWidget) super.getWidget();
    }
}
