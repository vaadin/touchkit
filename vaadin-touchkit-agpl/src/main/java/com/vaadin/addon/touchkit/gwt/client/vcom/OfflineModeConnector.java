package com.vaadin.addon.touchkit.gwt.client.vcom;

import java.util.Date;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.OfflineEvent;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.OfflineEvent.OfflineHandler;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.OnlineEvent;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.OnlineEvent.OnlineHandler;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint;
import com.vaadin.client.ApplicationConnection.CommunicationHandler;
import com.vaadin.client.ApplicationConnection.RequestStartingEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingEndedEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingStartedEvent;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(com.vaadin.addon.touchkit.extensions.OfflineMode.class)
public class OfflineModeConnector extends AbstractExtensionConnector implements
        CommunicationHandler, OfflineHandler, OnlineHandler {

    private static final String SESSION_COOKIE = "JSESSIONID";

    private Timer requestTimeoutTracker = new Timer() {
        @Override
        public void run() {
            offlineEntrypoint.goOffline(OfflineMode.BAD_RESPONSE);
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
                offlineEntrypoint.forceOffline(OfflineMode.ACTIVATED_BY_SERVER);
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
        getConnection().addHandler(OnlineEvent.TYPE, this);
        getConnection().addHandler(OfflineEvent.TYPE, this);
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        offlineTimeoutMillis = getState().offlineModeTimeout * 1000;
    }

    public OfflineMode getOfflineApp() {
        return OfflineModeEntrypoint.getOfflineMode();
    }

    /**
     * @deprecated use {@link OfflineModeEntrypoint.isNetworkOnline}
     */
    @Deprecated
    public static boolean isNetworkOnline() {
        return OfflineModeEntrypoint.isNetworkOnline();
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

    @Override
    protected void extend(ServerConnector target) {
        // Empty implementation
    }

    @Override
    public void onOnline(OnlineEvent ev) {
    }

    @Override
    public void onOffline(OfflineEvent ev) {
    }
}
