package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.ui.NumberFieldWidget;
import com.vaadin.shared.ui.Connect;
import com.vaadin.client.ui.textfield.TextFieldConnector;

@Connect(com.vaadin.addon.touchkit.ui.NumberField.class)
public class NumberFieldConnector extends TextFieldConnector {

    @Override
    protected Widget createWidget() {
        return GWT.create(NumberFieldWidget.class);
    }

    @Override
    public NumberFieldWidget getWidget() {
        return (NumberFieldWidget) super.getWidget();
    }

}
