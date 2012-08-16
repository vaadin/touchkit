package com.vaadin.addon.itest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;

import com.vaadin.addon.touchkit.itest.SwitchTest;

public class SwitchITCase extends AbstractTestBenchTest {

    @Test
    public void testSwipeView() throws IOException, AssertionError {
        startBrowser();
        try {
            driver.navigate().to(BASEURL + SwitchTest.class.getName());

            // Test the initial state (true).
            String text = driver.findElement(By.id("statusLabel")).getText();
            assertEquals("true", text);

            // Click should toggle the state to false.
            driver.findElement(By.id("switchComponent")).click();
            text = driver.findElement(By.id("statusLabel")).getText();
            assertEquals("false", text);
        } finally {
            driver.quit();
        }
    }

}
