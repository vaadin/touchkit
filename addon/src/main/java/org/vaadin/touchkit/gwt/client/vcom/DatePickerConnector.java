package org.vaadin.touchkit.gwt.client.vcom;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vaadin.touchkit.gwt.client.ui.DatePicker;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.vaadin.client.DateTimeService;
import com.vaadin.client.LocaleNotLoadedException;
import com.vaadin.client.LocaleService;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.v7.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Connector of DatePicker
 */
@SuppressWarnings("serial")
@Connect(org.vaadin.touchkit.ui.DatePicker.class)
public class DatePickerConnector extends AbstractFieldConnector implements
        ValueChangeHandler<Date>, DatePicker.DateParser {

    private DateTimeService dateTimeService;

    private final DatePickerServerRpc rpc = RpcProxy.create(
            DatePickerServerRpc.class, this);

    private String formatString;

    @Override
    public void init() {
        super.init();
        getWidget().addValueChangeHandler(this);
        dateTimeService = new DateTimeService();
    }

    @Override
    public DatePicker createWidget() {
        DatePicker picker = GWT.create(DatePicker.class);
        picker.setDateParser(this);
        return picker;
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
     *
     * @param res
     *            State resolution
     * @return Widget resolution
     */
    private DatePicker.Resolution convertResolution(
            DatePickerState.Resolution res) {

        switch (res) {
        case DAY:
            return DatePicker.Resolution.DAY;
        case MONTH:
            return DatePicker.Resolution.MONTH;
        case TIME:
        default:
            return DatePicker.Resolution.TIME;
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        DatePicker.Resolution resolution = convertResolution(getState().resolution);

        try {
            dateTimeService.setLocale(getState().locale);
        } catch (LocaleNotLoadedException e) {
            Logger.getLogger(DatePickerConnector.class.getName()).log(
                    Level.SEVERE,
                    "Tried to use an unloaded locale \"" + getState().locale);
        }

        formatString = getFormatString(resolution, getState().locale);

        getWidget().setUseNative(getState().useNative);
        getWidget().setResolution(resolution);
        getWidget().setDate(parseDateInWireFormat(getState().date));

        getWidget().setMin(parseDateInWireFormat(getState().min));
        getWidget().setMax(parseDateInWireFormat(getState().max));
    }

    private Date parseDateInWireFormat(String date) {
        return (date == null || date.isEmpty()) ? null : getWireFormat().parse(
                date);
    }

    /**
     * Date format used over the wire.
     *
     * @see <a href="http://tools.ietf.org/html/rfc3339">RFC3339</a>
     */
    protected DateTimeFormat getWireFormat() {
        DatePicker.Resolution resolution = convertResolution(getState().resolution);
        return DatePicker.getFormat(resolution);
    }

    @Override
    public void onValueChange(ValueChangeEvent<Date> event) {
        if (event.getValue() != null) {
            rpc.valueChanged(getWireFormat().format((event.getValue())));
        } else {
            rpc.valueChanged(null);
        }
    }

    /*
     * From VTextualDate
     */
    private String getFormatString(DatePicker.Resolution resolution,
            String currentLocale) {
        switch (resolution) {
        case MONTH:
        case DAY:
            return createDateFormat(currentLocale, resolution);

        case TIME:
            String dateAndTimeFormat = createDateFormat(currentLocale,
                    resolution);
            if (dateTimeService.isTwelveHourClock()) {
                dateAndTimeFormat += " hh";
            } else {
                dateAndTimeFormat += " HH";
            }
            dateAndTimeFormat += ":mm";
            if (dateTimeService.isTwelveHourClock()) {
                dateAndTimeFormat += " aaa";
            }

            return dateAndTimeFormat;

        default:
            throw new RuntimeException();
        }
    }

    private String createDateFormat(String currentLocale,
            DatePicker.Resolution resolution) {
        try {
            String dateFormat = LocaleService.getDateFormat(currentLocale);
            return cleanFormat(dateFormat, resolution);
        } catch (LocaleNotLoadedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * From VTextualDate
     */
    private String cleanFormat(String format, DatePicker.Resolution resolution) {
        if (resolution == DatePicker.Resolution.MONTH) {
            format = format.replaceAll("d", "");
        }

        // Remove unsupported patterns
        format = format.replaceAll("[GzZwWkK]", "");

        // Remove extra delimiters ('/' and '.')
        while (format.startsWith("/") || format.startsWith(".")
                || format.startsWith("-")) {
            format = format.substring(1);
        }
        while (format.endsWith("/") || format.endsWith(".")
                || format.endsWith("-")) {
            format = format.substring(0, format.length() - 1);
        }

        // Remove duplicate delimiters
        format = format.replaceAll("//", "/");
        format = format.replaceAll("\\.\\.", ".");
        format = format.replaceAll("--", "-");

        return format.trim();
    }

    @Override
    public String dateToString(Date date) {
        return dateTimeService.formatDate(date, formatString);
    }

    @Override
    public Date stringToDate(String string) {
        return dateTimeService.parseDate(string, formatString, false);
    }
}
