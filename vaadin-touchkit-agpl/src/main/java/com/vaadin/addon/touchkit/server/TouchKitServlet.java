package com.vaadin.addon.touchkit.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.vaadin.addon.touchkit.ui.TouchKitSettings;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletSession;

public class TouchKitServlet extends VaadinServlet {

    @Override
    protected VaadinServletSession createApplication(HttpServletRequest request)
            throws ServletException {
        VaadinServletSession app = super.createApplication(request);
        VaadinServletSession.setCurrent(app);
        TouchKitSettings.init(app);
        return app;
    }

}
