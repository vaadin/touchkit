package com.vaadin.addon.touchkit.ui;

import java.util.Collection;
import java.util.HashSet;

import com.vaadin.Application;
import com.vaadin.RootRequiresMoreInformationException;
import com.vaadin.addon.touchkit.rootextensions.ApplicationIcons;
import com.vaadin.addon.touchkit.rootextensions.IosWebAppSettings;
import com.vaadin.addon.touchkit.rootextensions.OfflineModeSettings;
import com.vaadin.addon.touchkit.rootextensions.ViewPortSettings;
import com.vaadin.terminal.Extension;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.terminal.WrappedRequest.BrowserDetails;
import com.vaadin.terminal.gwt.server.BootstrapFragmentResponse;
import com.vaadin.terminal.gwt.server.BootstrapListener;
import com.vaadin.terminal.gwt.server.BootstrapPageResponse;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Root;

/**
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
public abstract class TouchKitApplication extends Application implements BootstrapListener {

    private boolean browserDetailsReady = false;

    public TouchKitApplication() {
        addExtension(new ViewPortSettings());
        addExtension(new IosWebAppSettings());
        addExtension(new ApplicationIcons());
        addExtension(new OfflineModeSettings());
        addBootstrapListener(this);
    }

    private Collection<Extension> touchkitHostPageExtensions = new HashSet<Extension>();
    
    private void addExtension(Extension extension) {
        touchkitHostPageExtensions.add(extension);
    }
    
    /**
     * 
     * @return viewport settings used for this Root, null if this root has no
     *         {@link ViewPortSettings} extension attached
     */
    public ViewPortSettings getViewPortSettings() {
        for (Extension e : getTouchkitHostPageExtensions()) {
            if (e instanceof ViewPortSettings) {
                return (ViewPortSettings) e;
            }
        }
        return null;
    }
    
    public OfflineModeSettings getOfflineModeSettings() {
        for (Extension e : getTouchkitHostPageExtensions()) {
            if (e instanceof OfflineModeSettings) {
                return (OfflineModeSettings) e;
            }
        }
        return null;
    }
    
    private Collection<Extension> getTouchkitHostPageExtensions() {
        return touchkitHostPageExtensions;
    }

    public IosWebAppSettings getIosWebAppSettings() {
        for (Extension e : getTouchkitHostPageExtensions()) {
            if (e instanceof IosWebAppSettings) {
                return (IosWebAppSettings) e;
            }
        }
        return null;
    }

    public ApplicationIcons getApplicationIcons() {
        for (Extension e : getTouchkitHostPageExtensions()) {
            if (e instanceof ApplicationIcons) {
                return (ApplicationIcons) e;
            }
        }
        return null;
    }


    @Override
    protected Root getRoot(WrappedRequest request)
            throws RootRequiresMoreInformationException {
        BrowserDetails browserDetails = request.getBrowserDetails();
        // This is a limitation of 7.0.0.alpha1 that there is no better way to
        // check if WebBrowser has been fully initialized
        if (!browserDetailsReady && browserDetails.getUriFragment() == null) {
            throw new RootRequiresMoreInformationException();
        }
        browserDetailsReady = true;

        Root r;
        // could also use screen size, browser version etc.
        if (browserDetails.getWebBrowser().isTouchDevice()) {
            r = getTouchRoot(request);
        } else {
            r = getFallbackRoot(request);
        }
        ensureInitialized(r);
        return r;
    }
    
    private void ensureInitialized(Root r) {
        // TODO add or clone extensions for roots here??
    }

    public abstract Root getTouchRoot(WrappedRequest request);
    
    public Root getFallbackRoot(WrappedRequest request) {
        return getTouchRoot(request);
    }
    
    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
//        if(response.getRoot() != null) {
//            for (Extension e : response.getRoot().getExtensions()) {
//                if (e instanceof BootstrapListener) {
//                    ((BootstrapListener) e).modifyBootstrapPage(response);
//                }
//            }
//        } else {
            for (Extension e : getTouchkitHostPageExtensions()) {
                if (e instanceof BootstrapListener) {
                    ((BootstrapListener) e).modifyBootstrapPage(response);
                }
            }
//        }
    }

}
