package com.vaadin.addon.touchkit.server;

import javax.servlet.ServletException;

import com.vaadin.addon.touchkit.settings.TouchKitSettings;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;

/**
 * This servlet should be used by TouchKit applications. It automatically
 * creates TouchKitSettings bound to {@link VaadinService} in
 * {@link #servletInitialized()} method. It should be overridden to configure
 * defaults suitable for this web application.
 * <p>
 * If TouchKit application cannot use this Servlet as super class, developers
 * should create and bound {@link TouchKitSettings} instance manually.
 */
public class TouchKitServlet extends VaadinServlet {

    private TouchKitSettings touchKitSettings;

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.server.VaadinServlet#servletInitialized()
     */
    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        touchKitSettings = new TouchKitSettings(getService());
    }

    /**
     * @return TouchKitSettings bound to VaadinService related to this Servlet
     */
    public TouchKitSettings getTouchKitSettings() {
        return touchKitSettings;
    }

}
