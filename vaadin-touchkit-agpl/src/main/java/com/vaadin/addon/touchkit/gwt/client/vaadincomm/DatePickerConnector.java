package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import java.util.Date;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.addon.touchkit.gwt.client.ui.DatePicker;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Connector of DatePicker
 */
@SuppressWarnings("serial")
@Connect(com.vaadin.addon.touchkit.ui.DatePicker.class)
public class DatePickerConnector extends AbstractFieldConnector
  implements ValueChangeHandler<java.util.Date> {
  
  private DatePickerServerRpc rpc = RpcProxy.create(DatePickerServerRpc.class,
      this);
  
  private HandlerRegistration valueChangeReg = null;
  
  @Override
  public void init() {
      super.init();
      
      valueChangeReg = getWidget().addValueChangeHandler(this);
  }
  
  @Override
  public DatePicker createWidget() {
    DatePicker widget = GWT.create(DatePicker.class);   
    return widget;
  }
  
  @Override
  public DatePicker getWidget() {
    return (DatePicker) super.getWidget();
  }

  @Override
  public DatePickerState getState() {
    return (DatePickerState) super.getState();
  }
  
  /**
   * Convert state resolution to widget resolution
   * @param res State resolution
   * @return Widget resolution
   */
  private DatePicker.Resolution convertResolution(
      DatePickerState.Resolution res) {
    
    switch (res) {
      case TIME:
        return DatePicker.Resolution.TIME;
      case DAY:
        return DatePicker.Resolution.DAY;
      case MONTH:
        return DatePicker.Resolution.MONTH;
      default:
        return DatePicker.Resolution.DAY;
    }
  }
  
  @Override
  public void onStateChanged(StateChangeEvent stateChangeEvent) {
    super.onStateChanged(stateChangeEvent);

    getWidget().setResolution(convertResolution(getState().getResolution()));
    getWidget().setDate (getState().getDate());
  }

  @Override
  public void onValueChange(ValueChangeEvent<Date> event) {
    rpc.valueChanged(event.getValue());
  }

}
