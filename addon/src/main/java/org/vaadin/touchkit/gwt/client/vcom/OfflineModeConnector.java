package org.vaadin.touchkit.gwt.client.vcom;

import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.FORCE_OFFLINE;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.FORCE_ONLINE;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.RESPONSE_TIMEOUT;

import java.util.Date;

import org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode;
import org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.vaadin.client.ApplicationConnection.CommunicationHandler;
import com.vaadin.client.ApplicationConnection.RequestStartingEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingEndedEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingStartedEvent;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

/**
 * This class is thought for interacting with the online/off-line mode of a
 * TouchKit application. Only a few operations can be set from server side like
 * forcing off-line or changing the timeout to consider that the server is
 * unreachable.
 * 
 * Might be you could be interested on extending this class for doing certain
 * things when the app goes off-line, if so consider to use
 * {@link OfflineMode.OfflineEvent} and {@link OfflineMode.OnlineEvent}
 */
@SuppressWarnings("serial")
@Connect(org.vaadin.touchkit.extensions.OfflineMode.class)
public class OfflineModeConnector extends AbstractExtensionConnector implements
        CommunicationHandler {

    private static final String SESSION_COOKIE = "JSESSIONID";

    private Timer requestTimeoutTracker = new Timer() {
        @Override
        public void run() {
            offlineEntrypoint.dispatch(RESPONSE_TIMEOUT);
        }
    };

    private int offlineTimeoutMillis;
    private boolean applicationStarted = false;
    private boolean persistenCookieSet;
    private static OfflineModeEntrypoint offlineEntrypoint;

    public OfflineModeConnector() {
        super();
        registerRpc(OfflineModeClientRpc.class, new OfflineModeClientRpc() {
            @Override
            public void goOffline() {
                offlineEntrypoint.dispatch(FORCE_OFFLINE);
            }

            @Override
            public void goOnline() {
                offlineEntrypoint.dispatch(FORCE_ONLINE);
            }
        });
    }

    @Override
    public OfflineModeState getState() {
        return (OfflineModeState) super.getState();
    }

    @Override
    protected void init() {
        offlineEntrypoint = OfflineModeEntrypoint.get();
        offlineEntrypoint.setOfflineModeConnector(this);
        offlineTimeoutMillis = getState().offlineModeTimeout * 1000;
        getConnection().addHandler(RequestStartingEvent.TYPE, this);
        getConnection().addHandler(ResponseHandlingStartedEvent.TYPE, this);
        getConnection().addHandler(ResponseHandlingEndedEvent.TYPE, this);
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        offlineTimeoutMillis = getState().offlineModeTimeout * 1000;
    }

    /**
     * @deprecated use {@link OfflineModeEntrypoint#getOfflineMode()}
     */
    public OfflineMode getOfflineApp() {
        return OfflineModeEntrypoint.getOfflineMode();
    }

    /**
     * @deprecated use
     *             <code>OfflineModeEntrypoint.getOfflineMode().getNetworkStatus().isAppOnline()</code>
     */
    @Deprecated
    public static boolean isNetworkOnline() {
        return OfflineModeEntrypoint.get().getNetworkStatus().isAppOnline();
    }

    @Override
    public void onRequestStarting(RequestStartingEvent e) {
        if (!applicationStarted) {
            applicationStarted = true;
        } else if (persistenCookieSet && getSessionCookie() == null) {
            // Session expired, add fake id -> server side visit will cause
            // normal session expired message instead of disabled cookies
            // warning. See #11420 && VaadinServlet.ensureCookiesEnabled...
            // method
            Cookies.setCookie(SESSION_COOKIE, "invalidateme");
        }

        if (offlineTimeoutMillis >= 0) {
            requestTimeoutTracker.schedule(offlineTimeoutMillis);
        }
    }

    @Override
    public void onResponseHandlingStarted(ResponseHandlingStartedEvent e) {
        requestTimeoutTracker.cancel();
    }

    @Override
    public void onResponseHandlingEnded(ResponseHandlingEndedEvent e) {
        updateSessionCookieExpiration();
    }

    private void updateSessionCookieExpiration() {
        if (getState().persistentSessionTimeout != null) {
            String cookie = getSessionCookie();
            if (cookie != null) {
                Date date = new Date();
                date = new Date(date.getTime()
                        + getState().persistentSessionTimeout * 1000L);
                Cookies.setCookie(SESSION_COOKIE, cookie, date);
                persistenCookieSet = true;
            }
            // else httpOnly, noop
        }
    }

    private String getSessionCookie() {
        return Cookies.getCookie(SESSION_COOKIE);
    }

    public int getOfflineModeTimeout() {
        return offlineTimeoutMillis;
    }

    @Override
    protected void extend(ServerConnector target) {
        // Empty implementation
    }
}
