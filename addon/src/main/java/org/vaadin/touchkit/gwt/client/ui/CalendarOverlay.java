package org.vaadin.touchkit.gwt.client.ui;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.vaadin.touchkit.gwt.client.ui.DatePicker.Resolution;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.vaadin.client.ui.VOverlay;

/**
 * Overlay with calendar, used by DatePicker.
 */
public class CalendarOverlay extends VOverlay implements
        HasValueChangeHandlers<java.util.Date>, ClickHandler {

    private static final String CLASSNAME = "v-touchkit-datepopover";

    private com.google.gwt.user.datepicker.client.DatePicker calendarWidget = null;
    private TextBox timeBox;

    private final Label okButton;
    private final Label cancelButton;
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

        calendarWidget = GWT
                .create(com.google.gwt.user.datepicker.client.DatePicker.class);

        calendarWidget.addShowRangeHandlerAndFire(showRangeHandler);

        panel.add(calendarWidget);

        if (resolution == Resolution.TIME) {
            SimplePanel p = new SimplePanel();
            p.setStyleName(CLASSNAME + "-time");
            timeBox = GWT.create(TextBox.class);
            p.add(timeBox);
            panel.add(p);
        }

        okButton = new Label("✓");
        okButton.addStyleName("v-touchkit-date-ok");
        okButton.addClickHandler(CalendarOverlay.this);

        cancelButton = new Label("✕");
        cancelButton.addStyleName("v-touchkit-date-cancel");
        cancelButton.addClickHandler(CalendarOverlay.this);
        panel.add(cancelButton);
        panel.add(okButton);

        addStyleName(CLASSNAME);
        if (resolution == Resolution.MONTH) {
            addStyleName(CLASSNAME + "-hidedays");
        }
    }

    private final ShowRangeHandler<Date> showRangeHandler = new ShowRangeHandler<Date>() {

        @Override
        public void onShowRange(ShowRangeEvent<Date> event) {

            // Verify min and max values
            Date startDay = justDay(event.getStart());
            Date endDay = justDay(event.getEnd());
            Date currentMonth = justMonth(calendarWidget.getCurrentMonth());
            if (min != null) {
                if (currentMonth.before(justMonth(min))) {
                    calendarWidget.setCurrentMonth(min);
                    return;
                }
                Date minDay = justDay(min);
                if (endDay.before(minDay)) {
                    calendarWidget.setCurrentMonth(minDay);
                    return;
                }
            }
            if (max != null) {
                if (currentMonth.after(justMonth(max))) {
                    calendarWidget.setCurrentMonth(max);
                    return;
                }
                Date maxDay = justDay(max);
                if (startDay.after(maxDay)) {
                    calendarWidget.setCurrentMonth(maxDay);
                    return;
                }
            }

            // Disable all dates that are in current view but are out of scope
            disableDaysNotInCurrentMonth(startDay, endDay);

            // Now update browsing button enable state
            updatePrevNextButtons();
        }

        private void disableDaysNotInCurrentMonth(Date startDay, Date endDay) {
            List<Date> disableDates = new LinkedList<Date>();
            Date firstDayOfMonth = firstDayOfMonth(calendarWidget
                    .getCurrentMonth());
            Date lastDayOfMonth = lastDayOfMonth(calendarWidget
                    .getCurrentMonth());
            Date dayAfterEnd = (Date) endDay.clone();
            CalendarUtil.addDaysToDate(dayAfterEnd, 1);
            for (Date day = startDay; day.before(dayAfterEnd); CalendarUtil
                    .addDaysToDate(day, 1)) {
                if (day.before(firstDayOfMonth) || day.after(lastDayOfMonth)) {
                    disableDates.add((Date) day.clone());
                }
            }
            if (!disableDates.isEmpty()) {
                calendarWidget.setTransientEnabledOnDates(false, disableDates);
            }
        }

        private void updatePrevNextButtons() {
            Date currentMonth;
            currentMonth = justMonth(calendarWidget.getCurrentMonth());
            if (min != null) {
                setPrevButtonEnabled(currentMonth.after(min));
            } else {
                setPrevButtonEnabled(true);
            }
            if (max != null) {
                setNextButtonEnabled(currentMonth.before(max));
            } else {
                setNextButtonEnabled(true);
            }
        }

    };

    private Date firstDayOfMonth(Date month) {
        Date firstDay = (Date) month.clone();
        CalendarUtil.setToFirstDayOfMonth(firstDay);
        return justDay(firstDay);
    }

    private Date lastDayOfMonth(Date month) {
        Date lastDay = (Date) month.clone();
        CalendarUtil.addMonthsToDate(lastDay, 1);
        CalendarUtil.setToFirstDayOfMonth(lastDay);
        CalendarUtil.addDaysToDate(lastDay, -1);
        return justDay(lastDay);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<Date> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void setDate(Date date) {
        calendarWidget.setValue(date, false);
        calendarWidget.setCurrentMonth(date);
        if (resolution == Resolution.TIME) {
            timeBox.setText(timeOnlyFormat.format(date));
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == okButton) {
            Date value = calendarWidget.getValue();
            if (resolution == Resolution.MONTH) {
                value = calendarWidget.getCurrentMonth();
            } else if (resolution == Resolution.TIME) {
                value = trySetTimeFromTimeBoxText(value);
            }
            ValueChangeEvent.fire(CalendarOverlay.this, value);
            this.hide();
        } else if (event.getSource() == cancelButton) {
            this.hide(false);
        }
    }

    @SuppressWarnings("deprecation")
    protected Date trySetTimeFromTimeBoxText(Date value) {
        try {
            Date time = timeOnlyFormat.parse(timeBox.getText());
            value.setHours(time.getHours());
            value.setMinutes(time.getMinutes());
        } catch (IllegalArgumentException e) {
            // Couldn't parse hours, just ignore them and use the old,
            // unmodified value.
        }
        return value;
    }

    private final static DateTimeFormat timeOnlyFormat = DateTimeFormat
            .getFormat(DateTimeFormat.PredefinedFormat.HOUR24_MINUTE);

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

    protected void setPrevButtonEnabled(boolean enabled) {
        if (!enabled) {
            addStyleName(CLASSNAME + "-noprev");
        } else {
            removeStyleName(CLASSNAME + "-noprev");
        }
    }

    protected void setNextButtonEnabled(boolean enabled) {
        if (!enabled) {
            addStyleName(CLASSNAME + "-nonext");
        } else {
            removeStyleName(CLASSNAME + "-nonext");
        }
    }
}
