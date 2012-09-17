package com.vaadin.addon.touchkit.ui;

import java.util.Date;

import com.vaadin.addon.touchkit.gwt.client.vaadincomm.DatePickerServerRpc;
import com.vaadin.addon.touchkit.gwt.client.vaadincomm.DatePickerState;
import com.vaadin.addon.touchkit.gwt.client.vaadincomm.DatePickerState.Resolution;
import com.vaadin.ui.AbstractField;

/**
 * Field used to ask time values from users. Uses browser's own time input
 * handling. Currently not supported by all browsers.
 */
@SuppressWarnings("serial")
public class DatePicker extends AbstractField<Date> {
  
  /**
   * Create new DatePicker
   */
  public DatePicker() {
    registerRpc(rpc);
    setResolution(Resolution.DAY);
  }
  
  /**
   * Create new DatePicker with caption
   * @param caption Caption of DatePicker
   */
  public DatePicker(String caption) {
    this();
    setCaption(caption);
  }
  
  private DatePickerServerRpc rpc = new DatePickerServerRpc() {

    @Override
    public void valueChanged(Date date) {
      DatePicker.this.setValue(date, false);
    }
  };

  @Override
  public Class<Date> getType() {
    return Date.class;
  }
  
  @Override
  protected DatePickerState getState() {
    return (DatePickerState) super.getState();
  }
  
  @Override
  public void beforeClientResponse(boolean initial) {
      super.beforeClientResponse(initial);

      getState().setDate(getValue());
  }

  /**
   * Get current resolution of this DatePicker
   * @return Resolution
   */
  public Resolution getResolution() {
    return getState().getResolution();
  }
  
  /**
   * Set current resolution of this DatePicker
   * @param resolution Resolution
   */
  public void setResolution(Resolution resolution) {
     getState().setResolution(resolution);
  }
}
