package com.vaadin.addon.itest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.addon.touchkit.itest.UrlFieldTest;
import com.vaadin.testbench.By;

public class UrlFieldITCase extends AbstractTestBenchTest {

    @Test
    public void givenTextField_whenTypeValidUrl_thenSameInputInUrlField() {

        startBrowser();
        try {
            // given
            driver.navigate().to(BASEURL + UrlFieldTest.class.getName());

            WebElement textfield = driver.findElement(By
                    .className("v-textfield"));
            WebElement urlfield = driver.findElement(By.className("urlfield"));

            // when
            textfield.sendKeys("http://www.vaadin.com/");
            Thread.sleep(1000);

            // then
            assertEquals("http://www.vaadin.com/",
                    urlfield.getAttribute("value"));

        } catch (InterruptedException e) {
            fail("sleep interrupted");
        } finally {
            driver.quit();
        }
    }

    @Test
    public void givenTextField_whenInvalidUrl_thenErrorNotification() {
        startBrowser();
        try {
            // given
            driver.navigate().to(BASEURL + UrlFieldTest.class.getName());

            WebElement textfield = driver.findElement(By
                    .className("v-textfield"));
            WebElement urlfield = driver.findElement(By.className("urlfield"));
            WebElement body = driver.findElement(By.tagName("body"));

            // when
            textfield.sendKeys("notvalidurl");
            Thread.sleep(1000);

            // then
            assert (urlfield.getAttribute("value").equals(""));
            assert (body.getText().contains("Not valid:"));

        } catch (InterruptedException e) {
            fail("sleep interrupted");
        } finally {
            driver.quit();
        }
    }
}
