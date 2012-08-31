package com.vaadin.addon.touchkit.gwt.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.VConsole;

/**
 * DatePicker widget. Uses HTML5 date input fields to ask time values from user.
 */
public class DatePicker extends Widget
  implements HasValueChangeHandlers<java.util.Date> {
  
  private static final String CLASSNAME = "v-touchkit-datepicker";
  private final static String MONTH_FORMAT = "yyyy-MM";
  private final static String DAY_FORMAT = "yyyy-MM-dd";
  private final static String TIME_MINUTES_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";
  private final static String TIME_SECONDS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  private Date date;
  private final InputElement input;
  private Resolution resolution;
  
  /**
   * Resolution of widget
   */
  public enum Resolution {
    /**
     * Resolution in time (usually minutes)
     */
    TIME("datetime"),
    /**
     * Resolution in days
     */
    DAY("date"),
    /**
     * Resolution in months
     */
    MONTH("month");
    
    private String type;
    
    private Resolution (String type) {
      this.type = type;
    }
    
    /**
     * Get type value used in input field
     * @return Type value used in input field
     */
    public String getType() {
      return this.type;
    }
  }
  
  /**
   * Create new DatePicker
   */
  public DatePicker() {
    
    Element wrapper = Document.get().createDivElement();
    setElement(wrapper);
    
    input = Document.get().createTextInputElement();
    setResolution(Resolution.DAY);
    wrapper.appendChild(input);
    
    com.google.gwt.user.client.Element userElement =
        (com.google.gwt.user.client.Element) Element.as(input);
    DOM.sinkEvents(userElement, Event.ONCHANGE | Event.ONBLUR);
    DOM.setEventListener(userElement, elementListener);
    
    setStyleName(CLASSNAME);
  }
  
  /**
   * Input element listener
   */
  private EventListener elementListener = new EventListener() {

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
      date = newDate;
      if (updateInput) {
        input.setValue(dateToString(date));
      }
      if (fire) {
        VConsole.log("FIRE!");
        ValueChangeEvent.fire(DatePicker.this, newDate);
      }
    } else {
      input.setValue(dateToString(date));
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
    resolution = res;
    input.setAttribute("type", res.getType());
    input.setValue(dateToString(date));
  }
  
}
