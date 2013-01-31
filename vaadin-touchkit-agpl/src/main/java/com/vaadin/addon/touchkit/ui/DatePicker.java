package com.vaadin.addon.touchkit.ui;

import java.util.Date;

import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerServerRpc;
import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerState;
import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerState.Resolution;
import com.vaadin.ui.AbstractField;

/**
 * EXPERIMENTAL!
 * 
 * This component is still experimental, don't expect it to by as high quality
 * as the library in general.
 * <p>
 * Field used to ask time values from users.
 */
@SuppressWarnings("serial")
public class DatePicker extends AbstractField<Date> {

    /**
     * Create new DatePicker
     */
    public DatePicker() {
        registerRpc(rpc);
        setResolution(Resolution.DAY);
    }

    /**
     * Create new DatePicker with caption
     * 
     * @param caption
     *            Caption of DatePicker
     */
    public DatePicker(String caption) {
        this();
        setCaption(caption);
    }

    private final DatePickerServerRpc rpc = new DatePickerServerRpc() {

        @Override
        public void valueChanged(Date date) {
            DatePicker.this.setValue(date, false);
        }
    };

    @Override
    public Class<Date> getType() {
        return Date.class;
    }

    @Override
    protected DatePickerState getState() {
        return (DatePickerState) super.getState();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        getState().date = getValue();
    }

    /**
     * Get current resolution of this DatePicker
     * 
     * @return Resolution
     */
    public Resolution getResolution() {
        return getState().resolution;
    }

    /**
     * Set current resolution of this DatePicker
     * 
     * @param resolution
     *            Resolution of DatePicker. Not all resolutions are supported on
     *            all devices.
     */
    public void setResolution(Resolution resolution) {
        getState().resolution = resolution;
    }

    /**
     * Define if component should use native date field when possible. Eg. iOS
     * Safari supports native date fields since iOS version 5.
     * 
     * @param useNative
     *            If true native date field is used with browsers supporting it
     */
    public void setUseNative(boolean useNative) {
        getState().useNative = useNative;
    }

    /**
     * If native date field is used when supported.
     * 
     * @return true if native field is used when supported
     */
    public boolean isUseNative() {
        return getState().useNative;
    }
}
