package com.vaadin.addon.touchkit.gwt.client.ui;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.vaadin.addon.touchkit.gwt.client.ui.DatePicker.Resolution;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VOverlay;

/**
 * Overlay with calendar, used by DatePicker.
 */
public class CalendarOverlay extends VOverlay implements
HasValueChangeHandlers<java.util.Date>, ClickHandler {

    private static final String CLASSNAME = "v-touchkit-datepopover";

    private com.google.gwt.user.datepicker.client.DatePicker calendarWidget = null;
    private final VButton okButton;
    private final VButton cancelButton;
    private final Resolution resolution;
    private final Date min;
    private final Date max;

    public CalendarOverlay(Resolution resolution, final Date min, final Date max) {

        this.resolution = resolution;
        this.min = min;
        this.max = max;
        setAutoHideEnabled(true);

        FlowPanel panel = new FlowPanel();
        add(panel);

        calendarWidget = new com.google.gwt.user.datepicker.client.DatePicker();

        calendarWidget.addShowRangeHandlerAndFire(showRangeHandler);

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

        addStyleName(CLASSNAME);

        if (resolution == Resolution.MONTH) {
            addStyleName(CLASSNAME + "-hidedays");
        }
    }

    private final ShowRangeHandler<Date> showRangeHandler = new ShowRangeHandler<Date>() {

        @Override
        public void onShowRange(ShowRangeEvent<Date> event) {

            // Verify min and max values
            long start = justDay(event.getStart()).getTime();
            long end = justDay(event.getEnd()).getTime();
            long min = 0;
            if (CalendarOverlay.this.min != null) {
                if (compareMonths(calendarWidget.getCurrentMonth(),
                        CalendarOverlay.this.min) < 0) {
                    calendarWidget.setCurrentMonth(CalendarOverlay.this.min);
                    return;
                }
                min = justDay(CalendarOverlay.this.min).getTime();
                if (min > end) {
                    calendarWidget.setCurrentMonth(new Date(min));
                    return;
                }
            }
            long max = justDay(new Date(addDay(end))).getTime();
            if (CalendarOverlay.this.max != null) {
                if (compareMonths(calendarWidget.getCurrentMonth(),
                        CalendarOverlay.this.max) > 0) {
                    calendarWidget.setCurrentMonth(CalendarOverlay.this.max);
                    return;
                }
                max = justDay(CalendarOverlay.this.max).getTime();
                if (max < start) {
                    calendarWidget.setCurrentMonth(new Date(max));
                    return;
                }
            }

            // Disable all dates that are in current view but are out of scope
            List<Date> disableDates = new LinkedList<Date>();
            for (long at = start; at <= end; at = justDay(
                    new Date(addDay(at))).getTime()) {
                VConsole.log("at: " + new Date(at).toString());
                if (at < min || at > max) {
                    disableDates.add(new Date(at));
                }
            }
            if (!disableDates.isEmpty()) {
                calendarWidget.setTransientEnabledOnDates(false,
                        disableDates);
            }

            // Now update browsing button enable state
            if (CalendarOverlay.this.min != null) {
                setPrevButtonEnabled(compareMonths(CalendarOverlay.this.min,
                        calendarWidget.getCurrentMonth()) < 0);
            } else {
                setPrevButtonEnabled(true);
            }
            if (CalendarOverlay.this.max != null) {
                setNextButtonEnabled(compareMonths(CalendarOverlay.this.max,
                        calendarWidget.getCurrentMonth()) > 0);
            } else {
                setNextButtonEnabled(true);
            }
        }
    };

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

    protected final static DateTimeFormat dropTimeFormat = DateTimeFormat
            .getFormat("yyyyMMdd");

    protected Date justDay(Date date) {
        return dropTimeFormat.parse(dropTimeFormat.format(date));
    }

    protected final static DateTimeFormat dropDayFormat = DateTimeFormat
            .getFormat("yyyyMM");

    protected Date justMonth(Date date) {
        return dropDayFormat.parse(dropDayFormat.format(date));
    }

    protected long compareMonths(Date a, Date b) {
        return justMonth(a).getTime() - justMonth(b).getTime();
    }

    /**
     * Adds about ~day (actually 24h), so remember to use with justDay!
     * 
     * @param time
     * @return
     */
    protected long addDay (long time) {
        return time + 24 * 60 * 60 * 1000;
    }

    protected void setPrevButtonEnabled (boolean enabled) {
        if (!enabled) {
            addStyleName(CLASSNAME + "-noprev");
        } else {
            removeStyleName(CLASSNAME + "-noprev");
        }
    }

    protected void setNextButtonEnabled (boolean enabled) {
        if (!enabled) {
            addStyleName(CLASSNAME + "-nonext");
        } else {
            removeStyleName(CLASSNAME + "-nonext");
        }
    }
}

