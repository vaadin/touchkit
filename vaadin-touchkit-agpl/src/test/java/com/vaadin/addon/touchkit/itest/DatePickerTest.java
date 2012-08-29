package com.vaadin.addon.touchkit.itest;

import java.util.Date;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.external.com.ibm.icu.util.Calendar;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.addon.touchkit.gwt.client.vaadincomm.DatePickerState.Resolution;

@SuppressWarnings("serial")
public class DatePickerTest extends AbstractTouchKitIntegrationTest  {
  
  private final DatePicker pickerA = new DatePicker("Pick day");
  private Label pickerALabel;
  
  public  DatePickerTest() {
    //setDescription("Testing for DatePicker");
    
    pickerA.setResolution(Resolution.TIME);
    Calendar cal = Calendar.getInstance();
    cal.set(1982, 10, 28, 9, 17);
    pickerA.setValue(cal.getTime());
    
    createResolutionOptions();
    
    addComponent(pickerA);
    pickerA.addListener(new Property.ValueChangeListener() {
      
      @Override
      public void valueChange(ValueChangeEvent event) {
        Date value = (Date)event.getProperty().getValue();
        pickerALabel.setValue(value != null ? value.toString() : "null");
      }
    });
    
    pickerALabel = new Label (pickerA.getValue().toString());
    pickerALabel.setCaption("Value sent by client");
    addComponent(pickerALabel);
  }
  
  private void createResolutionOptions() {
    HorizontalLayout buttonLayout = new HorizontalLayout();

    NativeSelect resolution = new NativeSelect("Resolution");
    resolution.setNullSelectionAllowed(false);
    resolution.setImmediate(true);
    buttonLayout.addComponent(resolution);
    for(Resolution res : Resolution.values()) {
      resolution.addItem(res);
    }
    resolution.setValue(pickerA.getResolution());
    
    resolution.addListener(new Property.ValueChangeListener() {
      
      @Override
      public void valueChange(ValueChangeEvent event) {
        Resolution res = (Resolution)event.getProperty().getValue();
        System.out.println("Resolution: " + res);
        pickerA.setResolution(res);
      }
    });
    
    addComponent(buttonLayout);
  }
}
