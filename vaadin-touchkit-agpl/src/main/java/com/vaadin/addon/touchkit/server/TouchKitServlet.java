package com.vaadin.addon.touchkit.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.vaadin.Application;
import com.vaadin.addon.touchkit.ui.TouchKitSettings;
import com.vaadin.server.VaadinServlet;

public class TouchKitServlet extends VaadinServlet {
    
    @Override
    protected Application getNewApplication(HttpServletRequest request)
            throws ServletException {
        Application newApplication = super.getNewApplication(request);
        TouchKitSettings.init(newApplication);
        return newApplication;
    }

}
