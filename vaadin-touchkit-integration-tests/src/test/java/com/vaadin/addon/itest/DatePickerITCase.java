package com.vaadin.addon.itest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vaadin.addon.touchkit.itest.DatePickerTest;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.OptionGroupElement;

public class DatePickerITCase extends AbstractTestBenchTest {

    @Override
    public void setUp() {
        super.setUp();
        startBrowser();
        driver.navigate().to(BASEURL + DatePickerTest.class.getName());
    }

    @Test
    public void datePicker_changeFieldLocaleToFinnish_dateFormattedWithFinnishLocale() {
        // native doesn't support locales
        $(CheckBoxElement.class).id("usenative").clear();

        $(OptionGroupElement.class).id("locale").selectByText("Finnish");

        String currentText = driver
                .findElement(By.className("v-touchkit-datepicker"))
                .findElement(By.className("v-select-select")).getText();

        assertEquals("28.11.1982", currentText);
    }
}
