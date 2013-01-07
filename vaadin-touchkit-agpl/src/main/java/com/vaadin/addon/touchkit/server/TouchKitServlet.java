package com.vaadin.addon.touchkit.server;

import javax.servlet.ServletException;

import com.vaadin.addon.touchkit.rootextensions.TouchKitSettings;
import com.vaadin.server.VaadinServlet;

public class TouchKitServlet extends VaadinServlet {

    private TouchKitSettings touchKitSettings;

    
    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        touchKitSettings = new TouchKitSettings(getService());
    }

    public TouchKitSettings getTouchKitSettings() {
        return touchKitSettings;
    }

}
