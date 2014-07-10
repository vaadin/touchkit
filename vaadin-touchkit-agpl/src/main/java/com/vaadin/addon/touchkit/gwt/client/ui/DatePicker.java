package com.vaadin.addon.touchkit.gwt.client.ui;

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
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
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
        HasValueChangeHandlers<String>, ClickHandler, HasEnabled {

    private static final String CLASSNAME = "v-touchkit-datepicker";
    private final static String DAY_FORMAT = "yyyy-MM-dd";
    private final static String MONTH_FORMAT = "yyyy-MM";
    private final static String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";
    private String date;
    private String min;
    private String max;
    private InputElement input;
    private Resolution resolution;

    private Label backUpWidget = null;
    private CalendarOverlay overlay = null;
    private boolean useNative = true;
    private long overlayClosed = 0;

    private boolean enabled = true;

    /**
     * Resolution of widget
     */
    public enum Resolution {
        /**
         * Resolution in time (usually minutes)
         */
        TIME("datetime-local", PredefinedFormat.DATE_TIME_SHORT),
        /**
         * Resolution in days
         */
        DAY("date", PredefinedFormat.DATE_LONG),
        /**
         * Resolution in months
         */
        MONTH("month", PredefinedFormat.YEAR_MONTH);

        private String type;
        private PredefinedFormat predefinedFormat;

        private Resolution(String type, PredefinedFormat predefinedFormat) {
            this.type = type;
            this.predefinedFormat = predefinedFormat;
        }

        /**
         * Get type value used in input field
         * 
         * @return Type value used in input field
         */
        public String getType() {
            return type;
        }

        public PredefinedFormat getPredefinedFormat() {
            return predefinedFormat;
        }
    }

    /**
     * Create new DatePicker
     */
    public DatePicker() {
        super();
        // changeResolution(Resolution.DAY);
        setStyleName(CLASSNAME);
    }

    /**
     * Input element listener
     */
    private final EventListener elementListener = new EventListener() {

        @Override
        public void onBrowserEvent(com.google.gwt.user.client.Event event) {
            String newDate = input.getValue();
            if (newDate == null || newDate.isEmpty()) {
                setDate(newDate, true, false);
            } else if (!newDate.equals(date)) {
                setDate(newDate, false, true);
            }
        }
    };

    private DateTimeFormat getFormat() {
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
     * Convert Date to string format understood by browsers
     * 
     * @param date
     *            Date converted
     * @return String version
     */
    private String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        return getFormat().format(date);
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
        // List<DateTimeFormat> formats = getCurrentFormats();
        //
        // // Iterate all formats until success
        // for (DateTimeFormat format : formats) {
        try {
            date = getFormat().parse(string);

            // Convert no timezoned times back to local time when iOS
            if (BrowserInfo.get().isIOS() && string.endsWith("Z")) {
                @SuppressWarnings("deprecation")
                int minOffset = date.getTimezoneOffset();
                date.setTime(date.getTime() - minOffset * 60000);
            }

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
        return date;
    }

    /**
     * Get the current value of this DatePicker.
     * 
     * @return The current value as a Date
     */
    public Date getDateValue() {
        return stringToDate(date);
    }

    /**
     * Set date value of this DatePicker. Some parts of date may be ignored
     * based on current resolution.
     * 
     * @param newDate
     *            New date value
     */
    public void setDate(Date newDate) {
        String newDateStr = dateToString(newDate);
        setDate(newDateStr);
    }

    /**
     * Set date value of this DatePicker. Some parts of date may be ignored
     * based on current resolution.
     * 
     * @param newDateStr
     *            New date value as yyyy-MM-ddTHH:mm:ss
     */
    public void setDate(String newDateStr) {
        setDate(newDateStr, date == null || !date.equals(newDateStr), false);
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
    protected void setDate(String newDate, boolean updateInput, boolean fire) {
        if (date == null || !date.equals(newDate)) {
            closeCalendar();
            date = newDate;
            if (updateInput) {
                updateValue();
            }
            if (fire) {
                ValueChangeEvent.fire(DatePicker.this, newDate);
            }
        } else {
            updateValue();
        }
    }

    protected void updateValue() {
        if (input != null) {
            input.setValue(date);
        } else if (backUpWidget != null) {
            Date stringToDate = stringToDate(date);
            if (stringToDate == null) {
                backUpWidget.setText("");
            } else {
                backUpWidget.setText(DateTimeFormat.getFormat(
                        resolution.getPredefinedFormat()).format(stringToDate));
            }
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<String> handler) {

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
            updateValue();
        }
    }

    /**
     * @return true if the Android version is older than 4.2, in which case the
     *         HTML5 date field is not very useful.
     */
    private boolean isOldAndroid() {
        VBrowserDetails details = new VBrowserDetails(
                BrowserInfo.getBrowserString());
        return details.isAndroid()
                && (details.getOperatingSystemMajorVersion() < 4 || details
                        .getOperatingSystemMinorVersion() < 2);
    }

    private void addHtml5Input() {
        if (input == null) {
            input = Document.get().createTextInputElement();
            if (min != null) {
                input.setAttribute("min", min);
            }
            if (max != null) {
                input.setAttribute("max", max);
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
            updateValue();
        }
    }

    protected void openCalendar() {
        closeCalendar();

        overlay = new CalendarOverlay(resolution, stringToDate(min),
                stringToDate(max));
        overlay.setOwner(DatePicker.this);
        // overlay.showRelativeTo(this);
        overlay.center();
        if (date != null) {
            overlay.setDate(stringToDate(date));
        }

        overlay.addValueChangeHandler(new ValueChangeHandler<Date>() {

            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                setDate(dateToString(event.getValue()), true, true);
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
    public void setMin(String date) {
        min = date;

        if (input != null) {
            if (min != null) {
                input.setAttribute("min", date);
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
    public void setMax(String date) {
        max = date;
        if (input != null) {
            if (max != null) {
                input.setAttribute("max", date);
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

}
