package com.vaadin.addon.touchkit.rootextensions;

import com.vaadin.addon.touchkit.gwt.client.vcom.OfflineModeClientRpc;
import com.vaadin.addon.touchkit.gwt.client.vcom.OfflineModeState;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * TODO Needs a client side extension as well
 * 
 * 
 * - go offline
 * 
 * - persistent session cookie
 * 
 * In TK2 offline mode delay is in app config but it could just as well be
 * written to offline storage or cookie. This way these settings could survive
 * without bootstrap listener which are problematic
 * 
 * TODO rename or split to different features
 * 
 * @author mattitahvonen
 * 
 */
public class OfflineModeSettings extends AbstractExtension {

    private static final int DEFAULT_OFFLINE_MODE_DELAY = 5;

    private int offlineModeTimeout = DEFAULT_OFFLINE_MODE_DELAY;

    private Boolean persistentSessionCookie;

    @Override
    protected OfflineModeState getState() {
        return (OfflineModeState) super.getState();
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
        offlineModeTimeout = offlineModeDelay;
    }

    /**
     * Vaadin uses the servlet's session mechanism to track users. With its
     * default settings all sessions will be discarded when the browser
     * application closes. For mobile web applications (such as web apps in iOS
     * devices that are added to home screen) this might not be the desired
     * solution.
     * <p>
     * Persistent session cookie is on by default if the UI uses
     * {@link PreserveOnRefresh} annotation.
     * 
     * @return true if session cookie will be made persistent when closing the
     *         browser application
     */
    public boolean isPersistentSessionCookie() {
        if (persistentSessionCookie != null) {
            return persistentSessionCookie;
        } else {
            UI ui = getUI();
            if (ui != null
                    && ui.getClass().getAnnotation(PreserveOnRefresh.class) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vaadin uses the servlet's session mechanism to track users. With its
     * default settings all sessions will be discarded when the browser
     * application closes. For mobile web applications (such as web apps in iOS
     * devices that are added to home screen) this might not be the desired
     * solution. With this method the session cookie can be made persistent. A
     * returning user can then be shown his/her previous UI state.
     * <p>
     * Persistent session cookie is on by default if the UI uses
     * {@link PreserveOnRefresh} annotation. It is suggested to be used with
     * TouchKit applications that might be used as home screen apps.
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

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        if (isPersistentSessionCookie()) {
            getState().persistentSessionTimeout = VaadinSession.getCurrent()
                    .getSession().getMaxInactiveInterval();
        } else {
            getState().persistentSessionTimeout = null;
        }

    }

    /**
     * Instructs the client side to get into offline mode. Can be used
     * beforehand if e.g. the user knows he is about to lose his network
     * connection.
     */
    public void goOffline() {
        getRpcProxy(OfflineModeClientRpc.class).goOffline();
    }

    public void extend(UI ui) {
        super.extend(ui);
    }
}
