package org.vaadin.touchkit.extensions;

import org.vaadin.touchkit.annotations.OfflineModeEnabled;
import org.vaadin.touchkit.gwt.client.vcom.OfflineModeClientRpc;
import org.vaadin.touchkit.gwt.client.vcom.OfflineModeState;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * The OfflineMode extension adds offline support for the application. Settings
 * for offline mode can be accessed through this class and offline mode can be
 * triggered even though the network connection is still online.
 * 
 * @author Vaadin Ltd
 */
public class OfflineMode extends AbstractExtension {

    private Boolean persistentSessionCookie;

    @Override
    protected OfflineModeState getState() {
        return (OfflineModeState) super.getState();
    }

    /**
     * This method has been deprecated in favor of the
     * {@link OfflineModeEnabled} annotation.
     * 
     * @return always true
     */
    @Deprecated
    public boolean isOfflineModeEnabled() {
        return true;
    }

    /**
     * This method has been deprecated in favor of the
     * {@link OfflineModeEnabled} annotation and is a no-op.
     */
    @Deprecated
    public void setOfflineModeEnabled(boolean offlineModeEnabled) {
        // NOP
    }

    /**
     * Returns the amount of seconds the client side waits for requests to
     * return a response from the server until it opens the offline mode. A
     * value of -1 means that offline mode is completely disabled on slow
     * connections.
     */
    public int getOfflineModeTimeout() {
        return getState().offlineModeTimeout;
    }

    /**
     * Sets the timeout for how long the client side waits for the server to
     * respond before falling back to offline mode. If the value is set to -1,
     * the client side waits forever.
     * 
     * @param offlineModeTimeout
     *            timeout in seconds, -1 to disable the timeout.
     */
    public void setOfflineModeTimeout(int offlineModeTimeout) {
        getState().offlineModeTimeout = offlineModeTimeout;
    }

    /**
     * Vaadin uses the servlet's session mechanism to track users. With its
     * default settings, all sessions will be discarded when the browser
     * application closes. For mobile web applications (such as web apps that
     * are added to the home screen in iOS) this might not be the desired
     * solution.
     * <p>
     * The persistent session cookie is on by default if the UI uses the
     * {@link PreserveOnRefresh} annotation.
     * 
     * @return true if the session cookie will be made persistent when closing
     *         the browser application
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
     * default settings, all sessions will be discarded when the browser
     * application closes. For mobile web applications (such as web apps that
     * are added to the home screen in iOS) this might not be the desired
     * solution.
     * <p>
     * This method makes the session cookie persistent. A returning user can
     * then be shown his/her previous UI state.
     * <p>
     * The persistent session cookie is on by default if the UI uses the
     * {@link PreserveOnRefresh} annotation. It is suggested to be used with
     * TouchKit applications that might be used as home screen apps.
     * <p>
     * 
     * Note that the normal session lifetime is still respected although
     * persistent cookies are in use.
     * 
     * @param persistentSessionCookie
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
     * Instructs the client side to go into offline mode. This can be used e.g.
     * if the user knows he is about to lose his network connection or for
     * testing purposes.
     */
    public void goOffline() {
        getRpcProxy(OfflineModeClientRpc.class).goOffline();
    }

    /**
     * Instructs the client side to go into online mode if we sent a previous
     * offline order.
     */
    public void goOnline() {
        getRpcProxy(OfflineModeClientRpc.class).goOnline();
    }

    public void extend(UI ui) {
        super.extend(ui);
    }
}
