package com.vaadin.addon.itest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;

public class NavigationManagerViewITCase extends AbstractTouchKitTest {

    @Test
    public void testNavigationMananger() throws IOException, AssertionError,
            InterruptedException {
        startBrowser();
        try {
            driver.navigate().to(BASEURL + "NavigationManagerView");

            driver.findElement(By.id("toSecondView")).click();

            assertTrue(testBench
                    .compareScreen(getReferenceImage("navigationmanager/secondview.png")));

            driver.findElement(By.className("v-touchkit-navbutton-back"))
                    .click();

            assertTrue(testBench
                    .compareScreen(getReferenceImage("navigationmanager/rootview.png")));

        } finally {
            driver.quit();
        }
    }

}
