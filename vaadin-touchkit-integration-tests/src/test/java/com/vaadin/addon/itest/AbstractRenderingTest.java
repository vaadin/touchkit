package com.vaadin.addon.itest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public abstract class AbstractRenderingTest extends AbstractTouchKitTest {

    @Test
    public void test() throws IOException, AssertionError {
        startBrowser();
        try {
            driver.navigate().to(BASEURL + getTestViewName());

            assertTrue(testBench
                    .compareScreen(getReferenceImage(getTestViewName() + ".png")));
        } finally {
            driver.quit();
        }
    }

    protected String getTestViewName() {
        String simpleName = getClass().getSimpleName();
        simpleName = simpleName.substring(0, simpleName.indexOf("ITCase"));
        return simpleName;
    }
}
