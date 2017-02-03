package org.vaadin.touchkit.gwt.client.ui;

import java.util.Date;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.VConsole;
import com.vaadin.shared.VBrowserDetails;

/**
 * DatePicker widget. Uses HTML5 date input fields to ask time values from user.
 *
 */
public class DatePicker extends SimplePanel implements
        HasValueChangeHandlers<Date>, ClickHandler, HasEnabled {

    private static final String CLASSNAME = "v-touchkit-datepicker";
    private final static String DAY_FORMAT = "yyyy-MM-dd";
    private final static String MONTH_FORMAT = "yyyy-MM";
    private final static String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";
    private DateParser dateParser;

    private Date date;
    private Date min;
    private Date max;
    private InputElement input;
    private Resolution resolution;

    private Label backUpWidget = null;
    private CalendarOverlay overlay = null;
    private boolean useNative = true;
    private long overlayClosed = 0;

    private boolean enabled = true;

    /**
     * Interface for parsing dates. DatePicker provides a standard
     * implementation based on {@link DatePicker#getFormat(Resolution)} which is
     * used when in native mode.
     *
     */
    public interface DateParser {
        String dateToString(Date date);

        Date stringToDate(String string);
    }

    /**
     * Parses dates according to <a
     * href="http://tools.ietf.org/html/rfc3339">RFC3339</a> This is needed
     * since {@link #input} takes only date strings in that format
     */
    private DateParser standardParser = createStandardDateParser();

    private DateParser createStandardDateParser() {
        if (BrowserInfo.get().isIOS()) {
            return new StandardIosDateParser();
        } else {
            return new StandardDateParser();
        }
    }

    /**
     * Resolution of widget
     */
    public enum Resolution {
        /**
         * Resolution in time (usually minutes)
         */
        TIME("datetime-local"),
        /**
         * Resolution in days
         */
        DAY("date"),
        /**
         * Resolution in months
         */
        MONTH("month");

        private String type;

        Resolution(String type) {
            this.type = type;

        }

        /**
         * Get type value used in input field
         *
         * @return Type value used in input field
         */
        public String getType() {
            return type;
        }
    }

    /**
     * Create new DatePicker
     */
    public DatePicker() {
        super();
        setStyleName(CLASSNAME);
    }

    /**
     * This format must be used for native mode
     *
     * @param resolution
     * @return
     * @see <a href="http://www.w3.org/TR/html-markup/input.date.html">W3C spec
     *      for date input</a>
     */
    public static DateTimeFormat getFormat(Resolution resolution) {
        switch (resolution) {
        case MONTH:
            return DateTimeFormat.getFormat(MONTH_FORMAT);
        case DAY:
            return DateTimeFormat.getFormat(DAY_FORMAT);
        case TIME:
        default:
            return DateTimeFormat.getFormat(TIME_FORMAT);
        }
    }

    /**
     * Input element listener for the HTML5 date input element
     */
    private final EventListener elementListener = new EventListener() {

        @Override
        public void onBrowserEvent(com.google.gwt.user.client.Event event) {
            String newDateString = input.getValue();
            if (newDateString == null || newDateString.isEmpty()) {
                // fire event if date value has changed
                setDate(null, true, date != null);
            } else {
                // non-empty value, set if changed
                Date newDate = toStandardDate(newDateString);

                if (!newDate.equals(date)) {
                    setDate((newDate), false, true);
                }
            }
        }
    };

    private Date toStandardDate(String newDateString) {
        return (newDateString == null || newDateString.isEmpty()) ? null
                : standardParser.stringToDate(newDateString);
    }

    private String toStandardDateString(Date date) {
        return (date == null) ? "" : standardParser.dateToString(date);
    }

    /**
     * Convert Date to string format needed for display
     *
     * @param date
     *            Date to convert
     * @return String date or empty string for null date
     */
    private String dateToString(Date date) {
        if (date == null) {
            return "";
        }

        return getDateParser().dateToString(date);
    }

    /**
     * Convert string value from input field to Date
     *
     * @param string
     *            String value of input field
     * @return Date value or null if failure
     */
    private Date stringToDate(String string) {
        Date date = null;
        try {
            date = getDateParser().stringToDate(string);

            // break;

        } catch (Exception e) {
            // Doesn't matter
        }
        // }

        if (date == null) {
            VConsole.error("Failed to parse: " + string);
        }

        return date;
    }

    /**
     * Get the current value of this DatePicker.
     *
     * @return The current value as a String
     */
    public String getValue() {
        return dateToString(date);
    }

    /**
     * Get the current value of this DatePicker.
     *
     * @return The current value as a Date
     */
    public Date getDateValue() {
        return date;
    }

    /**
     * Set date value of this DatePicker. Some parts of date may be ignored
     * based on current resolution.
     *
     * @param newDate
     *            New date value
     */
    public void setDate(Date newDate) {
        setDate(newDate, date == null || !date.equals(newDate), false);
    }

    /**
     * Set date value of this DatePicker
     *
     * @param newDate
     *            New value set
     * @param updateInput
     *            If input field should be updated
     * @param fire
     *            If change event should be fired
     */
    protected void setDate(Date newDate, boolean updateInput, boolean fire) {
        if (date == null || !date.equals(newDate)) {
            closeCalendar();
            date = newDate;
            if (updateInput) {
                updateValue(date);
            }
            if (fire) {
                ValueChangeEvent.fire(DatePicker.this, newDate);
            }
        } else {
            updateValue(newDate);
        }
    }

    protected void updateValue(Date date) {
        if (input != null) {
            input.setValue(toStandardDateString(date));
        } else if (backUpWidget != null) {
            backUpWidget.setText(dateToString(date));
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<Date> handler) {

        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Set resolution of this DatePicker
     *
     * @param res
     *            Resolution
     */
    public void setResolution(Resolution res) {
        if (resolution != res) {
            changeResolution(res);
        }
    }

    public Resolution getResolution() {
        return resolution;
    }

    protected void changeResolution(Resolution res) {
        closeCalendar();
        resolution = res;

        if (useNative) {
            addHtml5Input();
            input.setAttribute("type", res.getType());
            if ("text".equals(input.getType()) || isOldAndroid()) {
                // HTML5 is not supported, revert to backup.
                removeHtml5Input();
                addBackupWidget();
            } else {
                // HTML5 is supported, make sure the backup widget is removed
                removeBackupWidget();
            }
        } else {
            removeHtml5Input();
            addBackupWidget();
        }
        if (date != null) {
            updateValue(date);
        }
    }

    /**
     * @return true if the Android version is older than 4.2, in which case the
     *         HTML5 date field is not very useful.
     */
    private boolean isOldAndroid() {
        VBrowserDetails details = new VBrowserDetails(
                BrowserInfo.getBrowserString());
        int major = details.getBrowserMajorVersion();
        int minor = details.getOperatingSystemMinorVersion();
        return details.isAndroid() && (major < 4 || (major == 4 && minor < 2));
    }

    private void addHtml5Input() {
        if (input == null) {
            input = Document.get().createTextInputElement();
            if (min != null) {
                input.setAttribute("min", toStandardDateString(min));
            }
            if (max != null) {
                input.setAttribute("max", toStandardDateString(max));
            }
            input.setDisabled(!enabled);
            getElement().appendChild(input);
            com.google.gwt.user.client.Element userElement = (com.google.gwt.user.client.Element) Element
                    .as(input);
            DOM.sinkEvents(userElement, Event.ONCHANGE | Event.ONBLUR);
            DOM.setEventListener(userElement, elementListener);
        }
    }

    private void removeHtml5Input() {
        if (input != null) {
            input.removeFromParent();
            input = null;
        }
    }

    private void removeBackupWidget() {
        if (backUpWidget != null && backUpWidget.isAttached()) {
            backUpWidget.removeFromParent();
            backUpWidget = null;
        }
    }

    private void addBackupWidget() {
        if (backUpWidget == null) {
            backUpWidget = new Label();
            backUpWidget.setStyleName("v-select-select"); // style like native
                                                          // input
            add(backUpWidget);
            backUpWidget.addClickHandler(this);
            updateValue(date);
        }
    }

    protected void openCalendar() {
        closeCalendar();

        overlay = new CalendarOverlay(resolution, min, max);
        overlay.setOwner(DatePicker.this);
        overlay.center();
        if (date != null) {
            overlay.setDate(date);
        }

        overlay.addValueChangeHandler(new ValueChangeHandler<Date>() {

            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                setDate(event.getValue(), true, true);
            }
        });

        overlay.addCloseHandler(new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                overlayClosed = new Date().getTime();
                overlay = null;
            }

        });
    }

    protected void closeCalendar() {
        if (overlay != null) {
            overlay.hide(true);
            overlay = null;
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (enabled) {
            if (backUpWidget == null) {
                return;
            }

            if (overlay != null) {
                closeCalendar();
            } else if ((new Date().getTime() - overlayClosed) > 250) {
                openCalendar();
            }
        }
    }

    /**
     * If widget should try to use native presentation
     *
     * @param useNative
     *            true to use native if possible
     */
    public void setUseNative(boolean useNative) {
        if (this.useNative != useNative) {
            this.useNative = useNative;
            changeResolution(resolution);
        }
    }

    /**
     * Set minimal value accepted from user.
     *
     * @param date
     *            the first accepted date.
     */
    public void setMin(Date date) {
        min = date;

        if (input != null) {
            if (min != null) {
                input.setAttribute("min", toStandardDateString(date));
            } else {
                input.removeAttribute("min");
            }
        }
    }

    /**
     * Set maximal value accepted from user.
     *
     * @param date
     *            the last accepted date.
     */
    public void setMax(Date date) {
        max = date;
        if (input != null) {
            if (max != null) {
                input.setAttribute("max", toStandardDateString(max));
            } else {
                input.removeAttribute("max");
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (input != null) {
            input.setDisabled(!enabled);
        }
    }

    /**
     * @see #setDateParser(DateParser)
     */
    public DateParser getDateParser() {
        if (useNative) {
            return standardParser;
        } else {
            return dateParser;
        }
    }

    /**
     * Set a custom parser for dates. Must be able to parse date<->string.
     *
     * @param dateParser
     */
    public void setDateParser(DateParser dateParser) {
        this.dateParser = dateParser;
    }

    private class StandardDateParser implements DateParser {
        @Override
        public String dateToString(Date date) {
            return getFormat(getResolution()).format(date);
        }

        @Override
        public Date stringToDate(String string) {
            return getFormat(getResolution()).parse(string);
        }
    }

    private class StandardIosDateParser extends StandardDateParser {
        @Override
        public String dateToString(Date date) {
            return getFormat(getResolution()).format(date);

        }

        @Override
        public Date stringToDate(String string) {
            Date parsedDate = super.stringToDate(string);
            // Convert no timezoned times back to local time when iOS
            if (string.endsWith("Z")) {
                @SuppressWarnings("deprecation")
                int minOffset = parsedDate.getTimezoneOffset();
                parsedDate.setTime(parsedDate.getTime() - minOffset * 60000);
            }
            return parsedDate;
        }
    }
}
