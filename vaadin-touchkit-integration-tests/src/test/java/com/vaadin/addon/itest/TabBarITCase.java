package com.vaadin.addon.itest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;

import com.vaadin.addon.touchkit.itest.TabBarTest;

public class TabBarITCase extends AbstractTestBenchTest {
    @Test
    public void testSwipeView() throws IOException, AssertionError {
        startBrowser();
        try {
            driver.navigate().to(BASEURL + TabBarTest.class.getName());

            String text = driver.findElement(By.tagName("body")).getText();
            assertTrue(text.contains("Tab0"));

            driver.findElement(
                    By.xpath("//span[@class = 'v-button-caption' and text() = 'Tab1']"))
                    .click();

            text = driver.findElement(By.tagName("body")).getText();

            assertTrue(text.contains("Tab1"));
        } finally {
            driver.quit();
        }
    }
}
