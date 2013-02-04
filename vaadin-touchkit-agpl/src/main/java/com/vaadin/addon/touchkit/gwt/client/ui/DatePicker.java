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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VOverlay;

/**
 * DatePicker widget. Uses HTML5 date input fields to ask time values from user.
 */
public class DatePicker extends SimplePanel
implements
HasValueChangeHandlers<java.util.Date>, ClickHandler {

    private static final String CLASSNAME = "v-touchkit-datepicker";
    private static final String POPOVER_CLASSNAME = "v-touchkit-datepopover";
    private final static String MONTH_FORMAT = "yyyy-MM";
    private final static String DAY_FORMAT = "yyyy-MM-dd";
    private final static String TIME_MINUTES_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";
    private final static String TIME_SECONDS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private Date date;
    private InputElement input;
    private Resolution resolution;

    private VButton backUpWidget = null;
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
        TIME("datetime", PredefinedFormat.DATE_TIME_MEDIUM),
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
     * Check if input field can be used for current browser.
     * 
     * @return
     */
    public boolean checkInputTagSupport() {
        return useNative && checkInputTagSupport(resolution);
    }

    /**
     * Check if input field is supported for current browser.
     * 
     * @param resolution
     *            Resolution used by DatePicker
     * @return
     */
    public static boolean checkInputTagSupport(Resolution resolution) {

        BrowserInfo info = BrowserInfo.get();

        if (!info.isIOS() && !(info.isAndroid() && info.isChrome())) {
            VConsole.log("DateField support undefined for: "
                    + BrowserInfo.getBrowserString());
            return false;
        } else {
            return true;
        }
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
                setDate (newDate, false, true);
            }
        }
    };

    /**
     * Get formats based on current resolution
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
            ret.add(DateTimeFormat.getFormat(
                    DateTimeFormat.PredefinedFormat.ISO_8601));
            ret.add(DateTimeFormat.getFormat(TIME_MINUTES_FORMAT));
            ret.add(DateTimeFormat.getFormat(TIME_SECONDS_FORMAT));
            break;
        default:
            ret.add(DateTimeFormat.getFormat(
                    DateTimeFormat.PredefinedFormat.ISO_8601));
        }

        return ret;
    }

    /**
     * Convert Date to string format understood by browsers
     * @param date Date converted
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
     * @param string String value of input field
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
                continue;
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
     * @param newDate New date value
     */
    public void setDate (Date newDate) {
        setDate (newDate, date == null || !date.equals(newDate), false);
    }

    /**
     * Set date value of this DatePicker
     * @param newDate New value set
     * @param updateInput If input field should be updated
     * @param fire If change event should be fired
     */
    protected void setDate (Date newDate, boolean updateInput, boolean fire) {
        if (date == null || !date.equals(newDate)) {
            closeCalendar();
            date = newDate;
            if (updateInput) {
                updateValue(date);
            }
            if (fire) {
                VConsole.log("FIRE!");
                ValueChangeEvent.fire(DatePicker.this, newDate);
            }
        } else {
            updateValue(date);
        }
    }

    protected void updateValue(Date newDate) {
        if (input != null) {
            input.setValue(dateToString(date));
        } else if (backUpWidget != null) {
            backUpWidget.setText(DateTimeFormat.getFormat(
                    resolution.getPredefinedFormat()).format(date));
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<java.util.Date> handler) {

        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Set resolution of this DatePicker
     * @param res Resolution
     */
    public void setResolution(Resolution res) {
        if (resolution != res) {
            changeResolution(res);
        }
    }

    protected void changeResolution(Resolution res) {
        resolution = res;

        if (checkInputTagSupport()) {
            if (backUpWidget != null) {
                backUpWidget.removeFromParent();
                backUpWidget = null;
            }
            if (input == null) {
                input = Document.get().createTextInputElement();
                getElement().appendChild(input);
                com.google.gwt.user.client.Element userElement = (com.google.gwt.user.client.Element) Element
                        .as(input);
                DOM.sinkEvents(userElement, Event.ONCHANGE | Event.ONBLUR);
                DOM.setEventListener(userElement, elementListener);
            }
        } else {
            closeCalendar();

            if (input != null) {
                input.removeFromParent();
                input = null;
            }
            if (backUpWidget == null) {
                backUpWidget = new VButton();
                add(backUpWidget);
                backUpWidget.addClickHandler(this);
            }
        }

        if (input != null) {
            input.setAttribute("type", res.getType());
        }

        if (date != null) {
            updateValue(date);
        }
    }

    protected class CalendarOverlay extends VOverlay implements
    HasValueChangeHandlers<java.util.Date>, ClickHandler {

        private com.google.gwt.user.datepicker.client.DatePicker calendarWidget = null;
        private final VButton okButton;
        private final VButton cancelButton;
        private final Resolution resolution;

        public CalendarOverlay(Resolution resolution) {

            this.resolution = resolution;
            setAutoHideEnabled(true);

            FlowPanel panel = new FlowPanel();
            add(panel);

            calendarWidget = new com.google.gwt.user.datepicker.client.DatePicker();
            panel.add(calendarWidget);

            okButton = new VButton();
            okButton.addStyleName("v-touchkit-date-ok");
            okButton.setHtml("<div class=\"v-touchkit-ok-image\">&nbsp;</div>");
            panel.add(okButton);
            okButton.setWidth("45%");
            okButton.addClickHandler(CalendarOverlay.this);

            cancelButton = new VButton();
            cancelButton.addStyleName("v-touchkit-date-cancel");
            cancelButton
            .setHtml("<div class=\"v-touchkit-cancel-image\">&nbsp;</div>");
            panel.add(cancelButton);
            cancelButton.setWidth("45%");
            cancelButton.addClickHandler(CalendarOverlay.this);

            addStyleName(POPOVER_CLASSNAME);

            if (resolution == Resolution.MONTH) {
                addStyleName(POPOVER_CLASSNAME + "-hidedays");
            }
        }

        @Override
        public HandlerRegistration addValueChangeHandler(
                ValueChangeHandler<Date> handler) {
            return addHandler(handler, ValueChangeEvent.getType());
        }

        public void setDate(Date date) {
            calendarWidget.setValue(date, false);
            calendarWidget.setCurrentMonth(date);
        }

        @Override
        public void onClick(ClickEvent event) {
            VButton button = (VButton) event.getSource();
            if (button == okButton) {
                Date value = calendarWidget.getValue();
                if (resolution == Resolution.MONTH) {
                    value = calendarWidget.getCurrentMonth();
                }
                ValueChangeEvent.fire(CalendarOverlay.this, value);
                this.hide();
            } else if (button == cancelButton) {
                this.hide(false);
            }
        }

    }

    protected void openCalendar() {
        closeCalendar();

        overlay = new CalendarOverlay(resolution);
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

}
