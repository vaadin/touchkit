package com.vaadin.addon.itest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

public class SmokeITCase {
    /**
     * Ensures a valid vaadin kickstart page is returned from deployment url.
     * 
     * @throws IOException
     */
    @Test
    public void smokeTest() throws IOException {

        URL url = new URL("http://localhost:5678/");

        InputStream openStream = url.openStream();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(openStream));
        String line;
        boolean isInitPage = false;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains("vaadin.vaadinConfigurations")) {
                isInitPage = true;
            }
        }
        bufferedReader.close();
        Assert.assertEquals(true, isInitPage);

    }

}
