package com.vaadin.addon.touchkit.ui;

import com.vaadin.Application;
import com.vaadin.server.WebBrowser;
import com.vaadin.server.WrappedRequest;
import com.vaadin.server.WrappedRequest.BrowserDetails;
import com.vaadin.ui.UI;

/**
 * TODO try to get rid of this class, needs changes to core
 * 
 * TouchKitApplication is a specialized {@link Application} implementation to
 * help building Vaadin applications designed specifically for various touch
 * devices. It provides quick access to the {@link WebBrowser} object via
 * {@link #getBrowser()} and adds a specific method to do real application setup
 * in a phase when all information in WebBrowser is available via
 * {@link #onBrowserDetailsReady()}.
 * <p>
 * Helper also provides static getter method for TouchKitApplication a.k.a
 * thread local pattern.
 * <p>
 * The standard Vaadin application init process calls the {@link #init()} method
 * very early. In that phase data all browser details are not necessary known by
 * the server side, so instead of overriding the init method as usual,
 * developers should override {@link #onBrowserDetailsReady()} method in case
 * their UI depends on details detected via {@link WebBrowser}. The init method
 * implementation sets a {@link TouchKitWindow} as main window and does the
 * required magic to call {@link #onBrowserDetailsReady()} when JavaScript
 * detected details from the client have been received. On that phase the main
 * window also has window width and height set according to the data provided by
 * the client side code.
 * <p>
 * Note, that only {@link TouchKitWindow}s are supported as top level windows.
 */
@SuppressWarnings("serial")
public abstract class TouchKitApplication extends Application {

    private boolean browserDetailsReady;

    public TouchKitApplication() {
        TouchKitSettings.init(this);
    }

    @Override
    public UI getUIForRequest(WrappedRequest request) {
        BrowserDetails browserDetails = request.getBrowserDetails();
        // This is a limitation of 7.0.0.alpha1 that there is no better way to
        // check if WebBrowser has been fully initialized
        if (!browserDetailsReady && browserDetails.getUriFragment() == null) {
            throw new IllegalStateException("Browser is not ready");
        }
        browserDetailsReady = true;

        UI r;
        // could also use screen size, browser version etc.
        if (browserDetails.getWebBrowser().isTouchDevice()) {
            r = getTouchRoot(request);
            TouchKitSettings.init(r);
        } else { 
            r = getFallbackRoot(request);
        }
        return r;
    }
    
    public abstract UI getTouchRoot(WrappedRequest request);
    
    public UI getFallbackRoot(WrappedRequest request) {
        return getTouchRoot(request);
    }
    

}
