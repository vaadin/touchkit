package com.vaadin.addon.touchkit.gwt.client.vcom;

import com.vaadin.shared.AbstractFieldState;

/**
 * State class for DatePicker
 */
public class DatePickerState extends AbstractFieldState {
    /**
     * Resolution of DatePicker
     */
    public enum Resolution {
        /**
         * Resolution is time, usually in minutes
         */
        TIME,
        /**
         * Resolution is in days
         */
        DAY,
        /**
         * Resolution is in months
         */
        MONTH;
    }

    public Resolution resolution;
    public String date;
    public String min;
    public String max;
    public boolean useNative = true;

}
