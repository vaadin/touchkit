package com.vaadin.addon.touchkit.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.vaadin.addon.touchkit.rootextensions.TouchKitSettings;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSessionInitializationListener;
import com.vaadin.server.VaadinSessionInitializeEvent;

public class TouchKitServlet extends VaadinServlet {

    private TouchKitSettings tkSettings;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        getService().addVaadinSessionInitializationListener(
                new VaadinSessionInitializationListener() {
                    @Override
                    public void vaadinSessionInitialized(
                            VaadinSessionInitializeEvent event)
                            throws ServiceException {
                        tkSettings = TouchKitSettings.init(event.getSession(),
                                TouchKitServlet.this.getService());

                    }
                });
    }

}
