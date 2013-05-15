package com.vaadin.addon.itest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;

import com.vaadin.addon.touchkit.itest.oldtests.NavigationManagerView;

public class NavigationManagerViewITCase extends AbstractTestBenchTest {

    @Test
    public void testNavigationMananger() throws IOException, AssertionError,
            InterruptedException {
        startBrowser();
        try {
            driver.navigate().to(BASEURL + NavigationManagerView.class.getName());

            driver.findElement(By.id("toSecondView")).click();
            
            Thread.sleep(500);

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
