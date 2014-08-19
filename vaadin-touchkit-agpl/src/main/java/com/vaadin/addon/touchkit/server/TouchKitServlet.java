package com.vaadin.addon.touchkit.server;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.addon.touchkit.settings.TouchKitSettings;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;

/**
 * This servlet should be used by all TouchKit applications. It automatically
 * creates an instance of TouchKitSettings bound to the current
 * {@link VaadinService} in the {@link #servletInitialized()} method. The
 * {@link #servletInitialized()} method can be overridden to configure defaults
 * suitable for this web application.
 * <p>
 * If a TouchKit application cannot use this Servlet as its super class,
 * developers should manually create and bind a {@link TouchKitSettings}
 * instance.
 */
@SuppressWarnings("serial")
public class TouchKitServlet extends VaadinServlet {

    /** The TouchKitSettings instance used */
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
     * @return The {@link TouchKitSettings} instance bound to the
     *         {@link VaadinService} related to this servlet
     */
    public TouchKitSettings getTouchKitSettings() {
        return touchKitSettings;
    }

    @Override
    protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            if (pathInfo.startsWith("/VAADIN/themes/touchkit/styles.css")) {
                serveDummyFile(response, "max-age=1000000");
                return;
            } else if (pathInfo.startsWith("/PING")) {
                serveDummyFile(response, "no-store, no-cache, max-age=0, must-revalidate");
                return;
            }
        }
        super.service(request, response);
    }

    @Override
    protected void writeStaticResourceResponse(HttpServletRequest request,
            HttpServletResponse response, URL resourceUrl) throws IOException {
        String file = resourceUrl.getFile();
        if (file.endsWith(".manifest")) {
            response.setContentType("text/cache-manifest");
            response.setHeader("Cache-Control", "max-age=1, must-revalidate");
        } else if (file.endsWith(".otf")) {
            response.setContentType("font/opentype");
        } else if (file.endsWith(".ttf")) {
            response.setContentType("font/ttf");
        } else if (file.endsWith(".woff")) {
            response.setContentType("application/font-woff");
        } else if (file.endsWith("vaadinBootstrap.js")) {
            // Patch bootstrap script in order to export some properties and
            // methods to the offline entry-point.
            // TODO: This is a temporary fix for 7.2.X and 7.3.0.betaX
            // when 7.3.0 is released with a new version of vaadinBootstrap.js
            // we could eventually remove this.
            Scanner scanner = new Scanner(resourceUrl.openStream());
            String js = scanner.useDelimiter("\\A").next();
            if (!js.contains("rootResponseStatus")) {
                js = js.replaceFirst("getApp:", "getAppIds: function(){return Object.keys(apps)},getApp:");
                js = js.replaceFirst("'getConfig'",
                        "'fetchRootConfig':fetchRootConfig,'getConfig'");
                js = js.replaceFirst("fetchRootConfig = function\\(\\)",
                        "fetchRootConfig = function(callback)");
                js = js.replaceFirst(
                        "r.responseText;",
                        "r.responseText; config.rootResponseStatus = r.status;config.rootResponseText = r.responseText; callback && callback(r);");
            }
            response.setContentType("application/javascript");
            response.getOutputStream().print(js);
            return;
        }
        super.writeStaticResourceResponse(request, response, resourceUrl);
    }

    private void serveDummyFile(HttpServletResponse response, String cacheControl)
            throws IOException {
        response.setContentType("text/css");
        response.setHeader("Cache-Control", cacheControl);
        response.getOutputStream().write("\n".getBytes());
    }
}
