package com.vaadin.addon.touchkit.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.vaadin.addon.touchkit.ui.TouchKitSettings;
import com.vaadin.server.AddonContext;
import com.vaadin.server.ApplicationStartedEvent;
import com.vaadin.server.ApplicationStartedListener;
import com.vaadin.server.VaadinServlet;

public class TouchKitServlet extends VaadinServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        AddonContext addonContext2 = getVaadinService().getAddonContext();
        addonContext2
                .addApplicationStartedListener(new ApplicationStartedListener() {

                    @Override
                    public void applicationStarted(ApplicationStartedEvent event) {
                        TouchKitSettings.init(event.getApplication());
                    }
                });
    }

}
