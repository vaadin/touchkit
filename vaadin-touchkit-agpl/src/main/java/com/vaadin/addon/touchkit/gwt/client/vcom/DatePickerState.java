package com.vaadin.addon.touchkit.gwt.client.vcom;

import java.util.Date;

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
  
  private Resolution resolution;
  private Date date;

  /**
   * Get resolution of DatePicker
   * @return Resolution
   */
  public Resolution getResolution() {
    return resolution;
  }

  /**
   * Set resolution of DatePicker
   * @param resolution New resolution
   */
  public void setResolution(Resolution resolution) {
    this.resolution = resolution;
  }

  /**
   * Get current date value of DatePicker
   * @return Date value
   */
  public Date getDate() {
    return date;
  }

  /**
   * Set current date value of DatePicker
   * @param date Date value
   */
  public void setDate(Date date) {
    this.date = date;
  }

}
