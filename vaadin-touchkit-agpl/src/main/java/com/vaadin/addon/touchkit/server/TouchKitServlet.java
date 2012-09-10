package com.vaadin.addon.touchkit.server;

import javax.servlet.http.HttpSession;

import com.vaadin.addon.touchkit.ui.TouchKitSettings;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

public class TouchKitServlet extends VaadinServlet {
    
    @Override
    @Deprecated
    protected VaadinSession getApplicationContext(HttpSession session) {
        // TODO Auto-generated method stub
        VaadinSession app = super.getApplicationContext(session);
        if(app != null) {
            TouchKitSettings.init(app);
        }
        return app;
    }

}
