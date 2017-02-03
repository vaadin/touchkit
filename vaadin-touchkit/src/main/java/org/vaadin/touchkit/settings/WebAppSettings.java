package org.vaadin.touchkit.settings;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;

/**
 * The WebAppSettings class is used to configure various settings that control
 * how the application behaves when linked to from mobile devices. Some of these
 * settings are platform specific, most introduced by Apple for their
 * "home screen web app" concept.
 * 
 * See the <a href=
 * "http://developer.apple.com/library/safari/#documentation/AppleApplications/Reference/SafariHTMLRef/Articles/MetaTags.html"
 * >Apple Meta tags documentation</a> for details.
 */
public class WebAppSettings implements BootstrapListener {

    private boolean webAppCapable = true;
    private String statusBarStyle = "black";
    private String startupImage;

    /**
     * Sets a header that tells the client whether the application is designed
     * to be used as a web application rather than a web page. If this is set to
     * true, the client may for instance hide the browsers own UI and give more
     * space for the web app. E.g. if app with this header is added to the home
     * screen on an iOS device, browser controls are hidden when the application
     * is started via the home screen icon.
     * 
     * @param webAppCapable
     *            true to treat the application as a web application rather than
     *            a web page.
     */
    public void setWebAppCapable(boolean webAppCapable) {
        this.webAppCapable = webAppCapable;
    }

    /**
     * @return true if the app is to be treated as a web application instead of
     *         a web page. The default is true.
     * @see #setWebAppCapable(boolean)
     */
    public boolean isWebAppCapable() {
        return webAppCapable;
    }

    /**
     * Some mobile devices (like Apple's iOS devices) may allow customizing
     * status bars appearances.
     * 
     * See the <a href=
     * "http://developer.apple.com/library/safari/#documentation/AppleApplications/Reference/SafariHTMLRef/Articles/MetaTags.html%23//apple_ref/doc/uid/TP40008193-SW4"
     * >apple-mobile-web-app-status-bar-style documentation</a> for details.
     * 
     * @param statusBarStyle
     *            The style of the status bar.
     */
    public void setStatusBarStyle(String statusBarStyle) {
        this.statusBarStyle = statusBarStyle;
    }

    /**
     * @return the status bar style or null if none set.
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
     *            the splash screen image to use.
     */
    public void setStartupImage(String startupImage) {
        this.startupImage = startupImage;
    }

    /**
     * @return the splash screen image used or null if none set.
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
            element.attr("content", getStatusBarStyle());
            head.appendChild(element);
        }

        if (getStartupImage() != null) {
            element = document.createElement("link");
            element.attr("rel", "apple-touch-startup-image");
            element.attr("href", getStartupImage());
            head.appendChild(element);
        }

        /*
         * Ensure window has "stable name", in case PreserveOnRefresh is used.
         * This is to fool vaadinBootstrap.js so that for example applications
         * used as ios "home screen webapps", can preserve their state among app
         * switches, like following links (with browser) and then returning back
         * to app.
         */
        if (response.getUiClass().getAnnotation(PreserveOnRefresh.class) != null) {
            element = document.createElement("script");
            element.attr("type", "text/javascript");
            element.appendText("\nwindow.name = '"
                    + response.getUiClass().hashCode() + "';\n");
            head.appendChild(element);
        }
    }

}
