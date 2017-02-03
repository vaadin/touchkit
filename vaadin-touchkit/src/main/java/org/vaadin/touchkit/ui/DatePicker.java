package org.vaadin.touchkit.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.vaadin.touchkit.gwt.client.vcom.DatePickerServerRpc;
import org.vaadin.touchkit.gwt.client.vcom.DatePickerState;
import org.vaadin.touchkit.gwt.client.vcom.DatePickerState.Resolution;

import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.DateField;

/**
 * DatePicker is a field used to ask time values from the user. In contrast to
 * the standard Vaadin {@link DateField}, this component shows a native
 * DatePicker in browsers that support this. Browsers that do not support native
 * date pickers show a touch friendly fallback.
 *
 * Browsers that have good support for native date pickers:
 * <ul>
 * <li>iOS - full support since 5.0, earlier versions have partial support</li>
 * <li>Android - partial, but good support in 4.2+</li>
 * </ul>
 * Earlier versions of Android have very limited support for native date
 * pickers.
 */
@SuppressWarnings("serial")
public class DatePicker extends AbstractField<Date> {

    /**
     * Constructs a new DatePicker instance with day resolution.
     */
    public DatePicker() {
        registerRpc(rpc);
        setResolution(Resolution.DAY);
    }

    /**
     * Constructs a new DatePicker instance with day resolution and the
     * specified caption.
     *
     * @param caption
     *            The caption
     */
    public DatePicker(String caption) {
        this();
        setCaption(caption);
    }

    private final DatePickerServerRpc rpc = new DatePickerServerRpc() {

        @Override
        public void valueChanged(String date) {
            if (date != null) {
                DatePicker.this.setValue(fromStr(date), false);
            } else {
                DatePicker.this.setValue(null, false);
            }
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

        Date value = getValue();
        if (value == null) {
            getState().date = null;
        } else {
            getState().date = getFormat().format(getValue());
        }

        Locale locale = getLocale();

        if (locale != null) {
            getState().locale = locale.toString();
        } else {
            getState().locale = null;
        }
    }

    private SimpleDateFormat getFormat() {
        switch (getResolution()) {
        case MONTH:
            return new SimpleDateFormat("yyyy-MM", getLocale());
        case DAY:
            return new SimpleDateFormat("yyyy-MM-dd", getLocale());
        case TIME:
        default:
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", getLocale());
        }
    }

    private String toStr(Date d) {
        return getFormat().format(d);
    }

    private Date fromStr(String dateStr) {
        try {
            return getFormat().parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return The current resolution
     */
    public Resolution getResolution() {
        return getState().resolution;
    }

    /**
     * Sets the current resolution of this DatePicker
     *
     * @param resolution
     *            The resolution. Not all resolutions are supported on all
     *            devices.
     */
    public void setResolution(Resolution resolution) {
        getState().resolution = resolution;
    }

    /**
     * <p>
     * Sets whether to use native date field when possible or always use the
     * fallback. E.g. iOS Safari fully supports native date fields since iOS
     * version 5.
     * </p>
     *
     * <p>
     * Note that when using the native date field, {@link #setLocale(Locale)}
     * will have no effect, since the locale for the native field is defined by
     * the browser locale
     * </p>
     *
     * @param useNative
     *            If true native date field is used with browsers supporting it
     */
    public void setUseNative(boolean useNative) {
        getState().useNative = useNative;
    }

    /**
     * @return true if native date field is used in supported browsers.
     * @see #setUseNative(boolean)
     */
    public boolean isUseNative() {
        return getState().useNative;
    }

    /**
     * Sets the minimum date value accepted from the user. Notice that, in
     * native mode, this is supported only by some devices. This function is
     * here for future use.
     *
     * @param min
     *            The minimum date value accepted from the user. Set to null to
     *            clear. The value must be before the maximum date value (if
     *            defined).
     */
    public void setMin(Date min) {
        if (min != null && getState().max != null) {
            if (min.after(getMax())) {
                throw new IllegalArgumentException("Given minimal value ("
                        + min.toString() + "), is after maximal value ("
                        + getState().max.toString() + ")");
            }
        }
        getState().min = toStr(min);
    }

    /**
     * @return The minimum date value accepted from the user, null if undefined.
     */
    public Date getMin() {
        return fromStr(getState().min);
    }

    /**
     * Sets the maximum date value accepted from the user. Notice that, in
     * native mode, this is supported only by some devices. This function is
     * here for future use.
     *
     * @param max
     *            Maximum date value accepted from the user. Set to null to
     *            clear. The value must be after the minimum date value (if
     *            defined).
     */
    public void setMax(Date max) {
        if (max != null && getState().min != null) {
            if (max.before(getMin())) {
                throw new IllegalArgumentException("Given maximal value ("
                        + max.toString() + "), is before minimal value ("
                        + getState().min.toString() + ")");
            }
        }
        getState().max = toStr(max);
    }

    /**
     * @return The maximum date value accepted from the user, null if undefined.
     */
    public Date getMax() {
        return fromStr(getState().max);
    }
}
