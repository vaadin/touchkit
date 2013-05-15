package com.vaadin.addon.itest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;

import com.vaadin.addon.touchkit.itest.navigationmanager.SimpleTestWithNavigationButtons;

public class SimpleWithNavigationButtonsITCase extends AbstractTestBenchTest {

    @Test
    public void testSwipeView() throws IOException, AssertionError, InterruptedException {
        startBrowser();
        try {
            driver.navigate().to(BASEURL + SimpleTestWithNavigationButtons.class.getName());

            String text = driver.findElement(By.tagName("body")).getText();
            assertTrue(text.contains("CURR"));
            
            driver.findElement(By.xpath("//div[@id = 'l1']//div[@class = 'v-touchkit-navbutton v-widget']")).click();
            driver.findElement(By.xpath("//div[@id = 'l2']//div[text() = '-->']")).click();
            
            text = driver.findElement(By.tagName("body")).getText();
            
            assertTrue(text.contains("YET ANOTHER"));
        } finally {
            driver.quit();
        }
    }

}
