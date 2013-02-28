package com.vaadin.addon.touchkit.ui;

import java.util.Date;

import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerServerRpc;
import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerState;
import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerState.Resolution;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;

/**
 * EXPERIMENTAL!
 * 
 * This component is still experimental, don't expect it to by as high quality
 * as the library in general.
 * <p>
 * Field used to ask time values from users. Instead of std Vaadin
 * {@link DateField} developers might want to use this to let browsers show
 * their own native date picker. Native date pickers are supported on most
 * recent devices only.
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

    /**
     * Set minimal value accepted from user. Notice that this is supported only
     * by some devices in native mode. This function is here for future use.
     * 
     * @param min
     *            Minimal value accepted from user. Null to clear. Minimal value
     *            must be before maximal value (if defined).
     */
    public void setMin(Date min) {
        if (min != null && getState().max != null) {
            if (min.after(getState().max)) {
                throw new IllegalArgumentException("Given minimal value ("
                        + min.toString() + "), is after maximal value ("
                        + getState().max.toString() + ")");
            }
        }
        getState().min = min;
    }

    /**
     * Get minimal value accepted from user.
     * 
     * @return Minimal value accepted from user, null if not define.
     */
    public Date getMin() {
        return getState().min;
    }

    /**
     * Set maximal value accepted from user. Notice that this is supported only
     * by some devices in native mode. This function is here for future use.
     * 
     * @param max
     *            Maximal value accepted from user. Null to clear. Maximal value
     *            must be after minimal value (if defined).
     */
    public void setMax(Date max) {
        if (max != null && getState().min != null) {
            if (max.before(getState().min)) {
                throw new IllegalArgumentException("Given maximal value ("
                        + max.toString() + "), is before minimal value ("
                        + getState().min.toString() + ")");
            }
        }
        getState().max = max;
    }

    /**
     * Get maximal value accepted from user.
     * 
     * @return Maximal value accepted from user, null if not define.
     */
    public Date getMax() {
        return getState().max;
    }
}
