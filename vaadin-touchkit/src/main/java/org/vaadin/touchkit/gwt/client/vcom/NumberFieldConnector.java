package org.vaadin.touchkit.gwt.client.vcom;

import org.vaadin.touchkit.gwt.client.ui.NumberFieldWidget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.client.ui.textfield.TextFieldConnector;

@Connect(org.vaadin.touchkit.ui.NumberField.class)
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
