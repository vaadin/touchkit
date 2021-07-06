package com.vaadin.addon.touchkit;

import javax.servlet.ServletException;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.settings.TouchKitSettings;

/**
 *
 * @author mstahv
 */
public class TestTouchKitServlet extends TouchKitServlet {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        TouchKitSettings touchKitSettings = getTouchKitSettings();

        touchKitSettings.getWebAppSettings().setApplicationName("TouchKit test app");
        touchKitSettings.getWebAppSettings().setApplicationShortName("TouchKitTest");
        touchKitSettings.getApplicationIcons().addApplicationIcon(128, 128, "image.png", true);

    }
}
