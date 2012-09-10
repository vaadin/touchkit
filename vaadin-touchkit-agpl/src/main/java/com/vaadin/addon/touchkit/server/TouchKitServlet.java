package com.vaadin.addon.touchkit.server;

import javax.servlet.ServletException;

import com.vaadin.addon.touchkit.ui.TouchKitSettings;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletSession;
import com.vaadin.server.WrappedHttpServletRequest;

public class TouchKitServlet extends VaadinServlet {
        
    @Override
    protected void onVaadinSessionStarted(WrappedHttpServletRequest request,
            VaadinServletSession session) throws ServletException {
        TouchKitSettings.init(session);
        super.onVaadinSessionStarted(request, session);
    }

}
