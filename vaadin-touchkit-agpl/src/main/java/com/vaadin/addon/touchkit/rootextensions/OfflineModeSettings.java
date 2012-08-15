package com.vaadin.addon.touchkit.rootextensions;

import org.jsoup.nodes.Document;

import com.vaadin.terminal.AbstractExtension;
import com.vaadin.terminal.gwt.server.BootstrapFragmentResponse;
import com.vaadin.terminal.gwt.server.BootstrapListener;
import com.vaadin.terminal.gwt.server.BootstrapPageResponse;

/**
 * TODO
 * Needs a client side extension as well
 *  - go offline
 *  - persisten session cookie
 *  
 * @author mattitahvonen
 *
 */
public class OfflineModeSettings extends AbstractExtension implements
        BootstrapListener {
    
//    WebApplicationContext context = (WebApplicationContext) getApplication()
//            .getContext();
//    int maxInactiveInterval = context.getHttpSession()
//            .getMaxInactiveInterval();
//    target.addAttribute("persistSession", maxInactiveInterval);


    private static final int DEFAULT_OFFLINE_MODE_DELAY = 5;

    private boolean cacheManifestEnabled = true;

    private int offlineModeTimeout = DEFAULT_OFFLINE_MODE_DELAY;
    
    private boolean persistentSessionCookie;

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Document document = response.getDocument();
        if (isCacheManifestEnabled()) {
            document.getElementsByTag("html").attr("manifest",
                    getCacheManifestLocation(response));
        }
    }

    private String getCacheManifestLocation(BootstrapPageResponse response) {
        String staticFileLocation = response.getRequest()
                .getDeploymentConfiguration()
                .getStaticFileLocation(response.getRequest());
        String configuredWidgetset = response.getRequest()
                .getDeploymentConfiguration()
                .getConfiguredWidgetset(response.getRequest());
        return staticFileLocation + "/VAADIN/widgetsets/" + configuredWidgetset
                + "/cache.manifest";
    }

    public boolean isCacheManifestEnabled() {
        return cacheManifestEnabled;
    }

    public void setCacheManifestEnabled(boolean cacheManifestEnabled) {
        this.cacheManifestEnabled = cacheManifestEnabled;
    }

    public boolean isOfflineModeEnabled() {
        return offlineModeTimeout == -1;
    }

    public void setOfflineModeEnabled(boolean offlineModeEnabled) {
        if (isOfflineModeEnabled() != offlineModeEnabled) {
            if (offlineModeEnabled) {
                setOfflineModeTimeout(DEFAULT_OFFLINE_MODE_DELAY);
            } else {
                setOfflineModeTimeout(-1);
            }
        }
    }

    /**
     * Returns the amount of seconds the client side waits request from the
     * server until it opens the offline mode. -1 means "offline mode" is
     * completely disabled on slow connection.
     */
    public int getOfflineModeTimeout() {
        return offlineModeTimeout;
    }
    
    /**
     * Sets the timeout (in seconds) how long the client side waits for server
     * to respond before falling back to "offline mode". If the value is set for
     * -1, the client side waits forever.
     * 
     * @param offlineTimeout
     *            timeout in seconds
     */
    public void setOfflineModeTimeout(int offlineModeDelay) {
        this.offlineModeTimeout = offlineModeDelay;
    }


    /**
     * Vaadin uses the servlet's session mechanism to track users. With its
     * default settings all sessions will be discarded when the browser
     * application closes. For mobile web applications (such as web apps in iOS
     * devices that are added to home screen) this might not be the desired
     * solution.
     * 
     * @return true if session cookie will be made persistent when closing the
     *         browser application
     */
    public boolean isPersistentSessionCookie() {
        return persistentSessionCookie;
    }

    /**
     * Vaadin uses the servlet's session mechanism to track users. With its
     * default settings all sessions will be discarded when the browser
     * application closes. For mobile web applications (such as web apps in iOS
     * devices that are added to home screen) this might not be the desired
     * solution. With this method the session cookie can be made persistent. A
     * returning user can then be shown his/her previous UI state.
     * <p>
     * 
     * Note, that the normal session lifetime is still respected although
     * persistent cookies are in use.
     * 
     * @param persistentCookie
     *            true if persistent session cookies should be used
     */
    public void setPersistentSessionCookie(boolean persistentSessionCookie) {
        this.persistentSessionCookie = persistentSessionCookie;
    }
    
    /**
     * Instructs the client side to get into offline mode. Can be used
     * beforehand if e.g. the user knows he is about to lose his network
     * connection.
     */
    public void goOffline() {
        // FIXME
    }


}
