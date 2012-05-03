package com.vaadin.addon.itest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;

public class TabsheetITCase extends AbstractTouchKitTest {

    @Test
    public void testNavigationMananger() throws IOException, AssertionError,
            InterruptedException {
        startBrowser();
        try {
            driver.navigate().to(BASEURL + "Tabsheet");

            try {
                // Wait a moment to let scrollbar dissappear if testing on
                // desktop
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }

            assertTrue(testBench
                    .compareScreen(getReferenceImage("tabsheet/first.png")));

            driver.findElement(By.xpath("//span[. = 'Other']")).click();

            assertTrue(testBench
                    .compareScreen(getReferenceImage("tabsheet/another.png")));

            driver.findElement(By.xpath("//span[. = 'Third']")).click();

            assertTrue(testBench
                    .compareScreen(getReferenceImage("tabsheet/third.png")));

            driver.findElement(By.xpath("//span[. = '4th']")).click();

            driver.findElement(By.tagName("body")).getText()
                    .contains("Date please");

            Thread.sleep(2000);

            assertTrue(testBench
                    .compareScreen(getReferenceImage("tabsheet/last.png")));

        } finally {
            driver.quit();
        }
    }

}
