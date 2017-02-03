package org.vaadin.touchkit.itest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.gwt.client.vcom.DatePickerState.Resolution;
import org.vaadin.touchkit.ui.DatePicker;

import com.ibm.icu.util.Calendar;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.OptionGroup;

@SuppressWarnings("serial")
public class DatePickerTest extends AbstractTouchKitIntegrationTest {

    private final DatePicker pickerA = new DatePicker("Pick day");
    private final Label pickerALabel;
    private final static String NULL_VALUE = "null";

    private final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public DatePickerTest() {
        // setDescription("Testing for DatePicker");

        pickerA.setResolution(Resolution.DAY);
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

        HorizontalLayout optionsLayout = new HorizontalLayout();
        optionsLayout.setCaption("Options");
        addComponent(optionsLayout);

        final CheckBox useNative = new CheckBox("Use native");
        useNative.setId("usenative");
        useNative.setImmediate(true);
        useNative.setValue(pickerA.isUseNative());
        optionsLayout.addComponent(useNative);
        useNative.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                pickerA.setUseNative((Boolean) event.getProperty().getValue());
            }

        });

        addComponent(new Button("Toggle enabled state", new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                pickerA.setEnabled(!pickerA.isEnabled());
            }
        }));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidth("100%");
        buttonLayout.setCaption("Set date value");
        addComponent(buttonLayout);
        buttonLayout.addComponent(new Button(NULL_VALUE, dateButtonListener));
        buttonLayout.addComponent(new Button("1982-10-25", dateButtonListener));
        buttonLayout.addComponent(new Button("2011-02-11", dateButtonListener));
        buttonLayout.addComponent(new Button("2013-01-31", dateButtonListener));
        buttonLayout.addComponent(new Button("2015-06-23", dateButtonListener));
        buttonLayout.addComponent(new Button("2078-12-25", dateButtonListener));

        OptionGroup mingroup = new OptionGroup("Min value");
        mingroup.setImmediate(true);
        mingroup.addItem(NULL_VALUE);
        mingroup.setValue(NULL_VALUE);
        mingroup.addItem("1982-01-01");
        mingroup.addItem("2011-01-01");
        mingroup.addItem("2011-02-01");
        mingroup.addItem("2011-02-10");
        mingroup.addItem("2011-02-12");
        mingroup.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String val = (String) event.getProperty().getValue();
                if (val.equals(NULL_VALUE)) {
                    System.out.println("Null min value");
                    pickerA.setMin(null);
                } else {
                    try {
                        System.out.println("Set min value to: " + val);
                        pickerA.setMin(df.parse(val));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        OptionGroup maxgroup = new OptionGroup("Max value");
        maxgroup.setImmediate(true);
        maxgroup.addItem(NULL_VALUE);
        maxgroup.setValue(NULL_VALUE);
        maxgroup.addItem("1982-01-01");
        maxgroup.addItem("2011-02-01");
        maxgroup.addItem("2011-02-12");
        maxgroup.addItem("2011-02-27");
        maxgroup.addItem("2011-03-01");
        maxgroup.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String val = (String) event.getProperty().getValue();
                if (val.equals(NULL_VALUE)) {
                    System.out.println("Null max value");
                    pickerA.setMax(null);
                } else {
                    try {
                        System.out.println("Set max value to: " + val);
                        pickerA.setMax(df.parse(val));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        HorizontalLayout minmax = new HorizontalLayout();
        minmax.setSpacing(true);
        minmax.addComponents(mingroup, maxgroup);

        addComponent(minmax);

        final OptionGroup localeGroup = new OptionGroup("Change the locale");
        localeGroup.setId("locale");
        localeGroup.setEnabled(!useNative.getValue());
        localeGroup.addContainerProperty("locale", Locale.class, null);
        localeGroup.addItem("Default");
        localeGroup.addItem("Finnish").getItemProperty("locale").setValue(new Locale("fi", "FI"));
        localeGroup.addItem("US").getItemProperty("locale").setValue(Locale.US);
        localeGroup.addItem("UK").getItemProperty("locale").setValue(Locale.UK);
        localeGroup.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                Locale locale = (Locale) localeGroup.getItem(event.getProperty().getValue()).getItemProperty("locale").getValue();
                pickerA.setLocale(locale);
            }
        });
        useNative.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent valueChangeEvent) {
                localeGroup.setEnabled(!useNative.getValue());
            }
        });

        localeGroup.select("Default");

        addComponent(localeGroup);
    }

    private final Button.ClickListener dateButtonListener = new ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            try {
                String caption = event.getButton().getCaption();
                if (caption.equals(NULL_VALUE)) {
                    pickerA.setValue(null);
                } else {
                    pickerA.setValue(df.parse(caption));
                }
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
