package com.vaadin.addon.touchkit.itest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.icu.util.Calendar;
import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.gwt.client.vcom.DatePickerState.Resolution;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;

@SuppressWarnings("serial")
public class DatePickerTest extends AbstractTouchKitIntegrationTest {

    private final DatePicker pickerA = new DatePicker("Pick day");
    private final Label pickerALabel;

    private final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public DatePickerTest() {
        // setDescription("Testing for DatePicker");

        pickerA.setResolution(Resolution.TIME);
        Calendar cal = Calendar.getInstance();
        cal.set(1982, 10, 28, 9, 17);
        pickerA.setValue(cal.getTime());

        createResolutionOptions();

        addComponent(pickerA);
        pickerA.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Date value = (Date) event.getProperty().getValue();
                pickerALabel.setValue(value != null ? value.toString() : "null");
            }
        });

        pickerALabel = new Label(pickerA.getValue().toString());
        pickerALabel.setCaption("Value sent by client");
        addComponent(pickerALabel);

        CheckBox cbox = new CheckBox("Use native");
        cbox.setImmediate(true);
        cbox.setValue(pickerA.isUseNative());
        addComponent(cbox);
        cbox.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                pickerA.setUseNative((Boolean) event.getProperty().getValue());
            }

        });

        HorizontalLayout buttonLayout = new HorizontalLayout();
        addComponent(buttonLayout);
        buttonLayout.addComponent(new Button("1982-10-25", dateButtonListener));
        buttonLayout.addComponent(new Button("2011-02-11", dateButtonListener));
        buttonLayout.addComponent(new Button("2013-01-31", dateButtonListener));
        buttonLayout.addComponent(new Button("2015-06-23", dateButtonListener));
        buttonLayout.addComponent(new Button("2078-12-25", dateButtonListener));
    }

    private final Button.ClickListener dateButtonListener = new ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            try {
                pickerA.setValue(df.parse(event.getButton().getCaption()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    };

    private void createResolutionOptions() {
        HorizontalLayout buttonLayout = new HorizontalLayout();

        NativeSelect resolution = new NativeSelect("Resolution");
        resolution.setNullSelectionAllowed(false);
        resolution.setImmediate(true);
        buttonLayout.addComponent(resolution);
        for (Resolution res : Resolution.values()) {
            resolution.addItem(res);
        }
        resolution.setValue(pickerA.getResolution());

        resolution.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Resolution res = (Resolution) event.getProperty().getValue();
                System.out.println("Resolution: " + res);
                pickerA.setResolution(res);
            }
        });

        addComponent(buttonLayout);
    }
}
