package com.vaadin.addon.touchkit.gwt.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
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
        HasValueChangeHandlers<java.util.Date>, ClickHandler {

    private static final String CLASSNAME = "v-touchkit-datepicker";
    private final static String MONTH_FORMAT = "yyyy-MM";
    private final static String DAY_FORMAT = "yyyy-MM-dd";
    private final static String TIME_MINUTES_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";
    private final static String TIME_SECONDS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private Date date;
    private Date min;
    private Date max;
    private InputElement input;
    private Resolution resolution;

    private Label backUpWidget = null;
    private CalendarOverlay overlay = null;
    private boolean useNative = true;
    private long overlayClosed = 0;

    /**
     * Resolution of widget
     */
    public enum Resolution {
        /**
         * Resolution in time (usually minutes)
         */
        TIME("datetime", PredefinedFormat.DATE_TIME_SHORT),
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
            return this.type;
        }

        public PredefinedFormat getPredefinedFormat() {
            return this.predefinedFormat;
        }
    }

    /**
     * Create new DatePicker
     */
    public DatePicker() {
        super();
        changeResolution(Resolution.DAY);
        setStyleName(CLASSNAME);
    }

    /**
     * Input element listener
     */
    private final EventListener elementListener = new EventListener() {

        @Override
        public void onBrowserEvent(com.google.gwt.user.client.Event event) {
            String value = input.getValue();
            Date newDate = stringToDate(value);
            if (newDate == null) {
                setDate(date, true, false);
            } else if (!newDate.equals(date)) {
                setDate(newDate, false, true);
            }
        }
    };

    /**
     * Get formats based on current resolution
     * 
     * @return Formats used to parse String value
     */
    private List<DateTimeFormat> getCurrentFormats() {
        List<DateTimeFormat> ret = new ArrayList<DateTimeFormat>();

        switch (resolution) {
        case DAY:
            ret.add(DateTimeFormat.getFormat(DAY_FORMAT));
            break;
        case MONTH:
            ret.add(DateTimeFormat.getFormat(MONTH_FORMAT));
            break;
        case TIME:
            ret.add(DateTimeFormat
                    .getFormat(DateTimeFormat.PredefinedFormat.ISO_8601));
            ret.add(DateTimeFormat.getFormat(TIME_MINUTES_FORMAT));
            ret.add(DateTimeFormat.getFormat(TIME_SECONDS_FORMAT));
            break;
        default:
            ret.add(DateTimeFormat
                    .getFormat(DateTimeFormat.PredefinedFormat.ISO_8601));
        }

        return ret;
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

        return getCurrentFormats().get(0).format(date);
    }

    /**
     * Convert string value from input field to Date
     * 
     * @param string
     *            String value of input field
     * @return Date value or null if failure
     */
    private Date stringToDate(String string) {
        VConsole.log("input: " + string);
        Date date = null;
        List<DateTimeFormat> formats = getCurrentFormats();

        // Iterate all formats until success
        for (DateTimeFormat format : formats) {
            try {
                date = format.parse(string);

                // Convert no timezoned times back to local time when iOS
                if (BrowserInfo.get().isIOS() && string.endsWith("Z")) {
                    @SuppressWarnings("deprecation")
                    int minOffset = date.getTimezoneOffset();
                    date.setTime(date.getTime() - minOffset * 60000);
                }

                break;

            } catch (Exception e) {
                // Doesn't matter
            }
        }

        if (date == null) {
            GWT.log("Failed to parse: " + string);
        }

        return date;
    }

    /**
     * Set date value of this DatePicker. Some parts of date will be ignored
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
            input.setValue(dateToString(date));
        } else if (backUpWidget != null) {
            if (date == null) {
                backUpWidget.setText("");
            } else {
                backUpWidget.setText(DateTimeFormat.getFormat(
                        resolution.getPredefinedFormat()).format(date));
            }
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<java.util.Date> handler) {

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
                input.setAttribute("min", dateToString(min));
            }
            if (max != null) {
                input.setAttribute("max", dateToString(max));
            }
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
            backUpWidget.setStyleName("v-select-select"); // style like native input
            add(backUpWidget);
            backUpWidget.addClickHandler(this);
            updateValue();
        }
    }

    protected void openCalendar() {
        closeCalendar();

        overlay = new CalendarOverlay(resolution, min, max);
        overlay.setOwner(DatePicker.this);
        // overlay.showRelativeTo(this);
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
        if (backUpWidget == null) {
            return;
        }

        if (overlay != null) {
            closeCalendar();
        } else if ((new Date().getTime() - overlayClosed) > 250) {
            openCalendar();
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
        this.min = date;

        if (input != null) {
            if (min != null) {
                String value = dateToString(min);
                VConsole.log("Set min attribute to:" + value);
                input.setAttribute("min", value);
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
        this.max = date;

        if (input != null) {
            if (max != null) {
                String value = dateToString(max);
                VConsole.log("Set max attribute to:" + value);
                input.setAttribute("max", value);
            } else {
                input.removeAttribute("max");
            }
        }
    }

}
