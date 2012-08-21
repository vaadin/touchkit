package com.vaadin.addon.itest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.addon.touchkit.itest.TabBarTest;

public class TabBarITCase extends AbstractTestBenchTest {
    @Test
    public void whenClickTab_thenTabSelected() throws IOException,
            AssertionError {
        startBrowser();
        try {
            // given
            driver.navigate().to(BASEURL + TabBarTest.class.getName());
            String text = driver.findElement(By.tagName("body")).getText();
            assertTrue(text.contains("Tab0"));

            for (int loop = 0; loop < 4; loop++) {
                // Skip Tab3 because it is icon only
                if (loop == 2) {
                    continue;
                }

                // ...given
                String tabname = String.format("Tab%d", loop);
                WebElement tab = driver
                        .findElement(By.xpath(String
                                .format("//span[@class = 'v-button-caption' and text() = '%s']",
                                        tabname)));

                // when
                tab.click();

                // then
                text = driver
                        .findElement(
                                By.xpath("//div[@class = 'v-touchkit-tabbar-wrapper']/div[contains(@class, 'v-label')]"))
                        .getText();

                assertTrue(text.contains(tabname));
            }
        } finally {
            driver.quit();
        }
    }

    @Test
    public void givenRemoveButton_whenPressRemove_thenCurrentTabVanishes() {
        startBrowser();
        try {
            // given
            driver.navigate().to(BASEURL + TabBarTest.class.getName());
            String body = driver.findElement(By.tagName("body")).getText();
            assertTrue(body.contains("Tab0"));

            WebElement removeButton = driver
                    .findElement(By
                            .xpath("//span[@class = 'v-button-caption' and text() = 'Remove selected tab']"));

            // when
            removeButton.click();

            // then
            body = driver.findElement(By.tagName("body")).getText();
            assertTrue(!body.contains("Tab0"));
            assertTrue(body.contains("Tab1"));
            assertTrue(body.contains("Tab3"));

        } finally {
            driver.quit();
        }
    }

    @Test
    public void givenFirstTabSelected_whenPressSetSelected_thenLastTabSelected() {
        startBrowser();
        try {
            // given
            driver.navigate().to(BASEURL + TabBarTest.class.getName());
            String body = driver.findElement(By.tagName("body")).getText();
            assertTrue(body.contains("Tab0"));
            WebElement selectionButton = driver
                    .findElement(By
                            .xpath("//span[@class = 'v-button-caption' and text() = 'Set selected']"));

            // when
            selectionButton.click();

            // then
            String content = driver
                    .findElement(
                            By.xpath("//div[@class = 'v-touchkit-tabbar-wrapper']/div[contains(@class, 'v-label')]"))
                    .getText();

            assertTrue(!content.contains("Tab0"));
            assertTrue(content.contains("Tab4"));

        } finally {
            driver.quit();
        }
    }
}
