package com.vaadin.addon.itest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.addon.touchkit.itest.ToolbarTest;

public class ToolbarITCase extends AbstractTestBenchTest {

    @Test
    public void givenToolbarWithButtons_whenClick_thenLabelShowSelection() {
        startBrowser();
        try {
            // given
            driver.navigate().to(BASEURL + ToolbarTest.class.getName());
            WebElement toolbar = driver
                    .findElement(By
                            .xpath("//div[contains(@class, 'v-touchkit-toolbar')]"));
            WebElement label = driver.findElement(By
                    .xpath("//div[contains(@class, 'v-label')]"));
            List<WebElement> toolbarButtons = toolbar.findElements(By
                    .xpath("./div"));

            assertEquals("Clicked: none", label.getText());
            assertSame(10, toolbarButtons.size());

            for (int index = 0; index < toolbarButtons.size(); index++) {

                WebElement button = toolbarButtons.get(index);

                // when
                button.click();

                // then
                assertEquals(String.format("Clicked: %d", index),
                        label.getText());
            }

        } finally {
            driver.quit();
        }
    }
}
