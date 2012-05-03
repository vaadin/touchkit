package com.vaadin.addon.itest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SwipeViewITCase extends AbstractTouchKitTest {

    @Test
    public void testSwipeView() throws IOException, AssertionError {
        startBrowser();
        try {
            driver.navigate().to(BASEURL + "SwipeViewTest");

            for (int i = 0; i < 11; i++) {
                List<WebElement> findElements = driver.findElements(By
                        .className("v-touchkit-navbutton-forward"));
                int size = findElements.size();
                System.out.print(i + " ");
                System.out.println(size);
                if (i == 0) {
                    assertTrue(testBench
                            .compareScreen(getReferenceImage("swipeview/initialview.png")));
                    findElements.get(0).click();
                } else {
                    findElements.get(1).click();
                }
            }

            testBench.waitForVaadin();

            WebElement firstCheckbox = driver.findElement(By
                    .xpath("//input[@type='checkbox']"));
            assertNotNull(firstCheckbox);
            firstCheckbox.click();
            String attribute = firstCheckbox.getAttribute("value");
            assertEquals("on", attribute);
            assertTrue(testBench
                    .compareScreen(getReferenceImage("swipeview/formview.png")));

        } finally {
            driver.quit();
        }
    }

}
