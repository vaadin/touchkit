package com.vaadin.addon.itest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.vaadin.addon.touchkit.itest.oldtests.ButtonsInComponentGroups;

public class ButtonsInComponentGroupsITCase extends AbstractTestBenchTest {

    @Test
    public void testSwipeView() throws IOException, AssertionError {
        startBrowser();
        try {
            driver.navigate().to(BASEURL + ButtonsInComponentGroups.class.getName());

            assertTrue(testBench
                    .compareScreen(getReferenceImage("buttonsincomponentgroups.png")));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            driver.quit();
        }
    }

}
