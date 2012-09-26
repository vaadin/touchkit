package com.vaadin.addon.touchkit.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.vaadin.addon.touchkit.rootextensions.TouchKitSettings;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;

public class TouchKitServlet extends VaadinServlet {

    private TouchKitSettings tkSettings;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        getService().addSessionInitListener(new SessionInitListener() {
            @Override
            public void sessionInit(SessionInitEvent event)
                    throws ServiceException {

                tkSettings = TouchKitSettings.init(event.getSession(),
                        TouchKitServlet.this.getService());
            }
        });
    }

}
