package com.vaadin.addon.touchkit.gwt.client.vcom;

import java.util.Date;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.vaadin.addon.touchkit.gwt.client.ui.DatePicker;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Connector of DatePicker
 */
@SuppressWarnings("serial")
@Connect(com.vaadin.addon.touchkit.ui.DatePicker.class)
public class DatePickerConnector extends AbstractFieldConnector
implements ValueChangeHandler<java.util.Date> {

    private final DatePickerServerRpc rpc = RpcProxy.create(DatePickerServerRpc.class,
            this);

    @Override
    public void init() {
        super.init();
        getWidget().addValueChangeHandler(this);
    }

    @Override
    public DatePicker createWidget() {
        DatePicker widget = GWT.create(DatePicker.class);
        return widget;
    }

    @Override
    public DatePicker getWidget() {
        return (DatePicker) super.getWidget();
    }

    @Override
    public DatePickerState getState() {
        return (DatePickerState) super.getState();
    }

    /**
     * Convert state resolution to widget resolution
     * @param res State resolution
     * @return Widget resolution
     */
    private DatePicker.Resolution convertResolution(
            DatePickerState.Resolution res) {

        switch (res) {
        case TIME:
            return DatePicker.Resolution.TIME;
        case DAY:
            return DatePicker.Resolution.DAY;
        case MONTH:
            return DatePicker.Resolution.MONTH;
        default:
            return DatePicker.Resolution.DAY;
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setUseNative(getState().useNative);
        getWidget().setResolution(convertResolution(getState().resolution));
        getWidget().setDate(getState().date);
    }

    @Override
    public void onValueChange(ValueChangeEvent<Date> event) {
        rpc.valueChanged(event.getValue());
    }

}
