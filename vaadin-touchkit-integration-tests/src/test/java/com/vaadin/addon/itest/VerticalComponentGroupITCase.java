package com.vaadin.addon.itest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;

import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;

public class VerticalComponentGroupITCase extends AbstractTestBenchTest {
	@Test
    public void testLayout() throws IOException, AssertionError,
            InterruptedException {
        startBrowser();
        try {
        	String className = VerticalComponentGroup.class.getName();
        	className = className.replaceAll(".ui.", ".itest.");
            driver.navigate().to(BASEURL + className+"Test");

            try {
                // Wait a moment to let scrollbar dissappear if testing on
                // desktop
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }

            String text = driver.findElement(By.tagName("body")).getText();
            assertTrue(text.contains("Vertical component group"));
            assertTrue(text.contains("Button too"));
            assertTrue(text.contains("Horizontal in vertical"));
            
        } finally {
            driver.quit();
        }
    }
}
