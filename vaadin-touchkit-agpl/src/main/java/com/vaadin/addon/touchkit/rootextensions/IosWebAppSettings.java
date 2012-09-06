package com.vaadin.addon.touchkit.rootextensions;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.vaadin.server.AbstractExtension;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.BootstrapResponse;

public class IosWebAppSettings extends AbstractTouchKitRootExtension implements
        BootstrapListener {

    private boolean webAppCapable = true;
    private String statusBarStyle;
    private String startupImage;

    /**
     * Sets a header that tells the client whether the application is designed
     * to be used as a web application rather than a web page. If this is set to
     * true, the client may for instance hide the browsers own UI and give more
     * space for the web app. E.g. if app with this header is added to the home
     * screen on an iOS device, browser controls are hidden when the application
     * is started via the homescreen icon.
     * 
     * <p>
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param webAppCapable
     */
    public void setWebAppCapable(boolean webAppCapable) {
        this.webAppCapable = webAppCapable;
    }

    /**
     * @return
     * @see #setWebAppCapable(Boolean)
     */
    public boolean isWebAppCapable() {
        return webAppCapable;
    }

    /**
     * Some mobile devices (like Apple's iOS devices) may allow customizing
     * status bars appearances.
     * <p>
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param statusBarStyle
     */
    public void setStatusBarStyle(String statusBarStyle) {
        this.statusBarStyle = statusBarStyle;
    }

    /**
     * @return
     * @see #setStatusBarStyle(String)
     */
    public String getStatusBarStyle() {
        return statusBarStyle;
    }

    /**
     * Sets the image that the device may use as a place-holder while the web
     * application is starting up - e.g splash-screen or screenshot of initial
     * screen.
     * 
     * @param startupImage
     */
    public void setStartupImage(String startupImage) {
        this.startupImage = startupImage;
    }

    /**
     * @return
     * @see #setStartupImage(String)
     */
    public String getStartupImage() {
        return startupImage;
    }

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Document document = response.getDocument();

        Element head = document.getElementsByTag("head").get(0);

        Element element;

        if (isWebAppCapable()) {
            element = document.createElement("meta");
            element.attr("name", "apple-mobile-web-app-capable");
            element.attr("content", "yes");
            head.appendChild(element);
        }

        if (getStatusBarStyle() != null) {
            element = document.createElement("meta");
            element.attr("name", "apple-mobile-web-app-status-bar-style");
            element.attr("content", "black");
            head.appendChild(element);
        }

        if (getStartupImage() != null) {
            element = document.createElement("link");
            element.attr("rel", "apple-touch-startup-image");
            element.attr("href", getStartupImage());
            head.appendChild(element);
        }
    }

}
