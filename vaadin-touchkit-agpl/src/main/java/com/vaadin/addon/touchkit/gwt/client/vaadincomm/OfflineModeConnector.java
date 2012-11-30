package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.vaadin.addon.touchkit.gwt.client.TouchKitOfflineApp;
import com.vaadin.client.ApplicationConnection.CommunicationErrorHandler;
import com.vaadin.client.ApplicationConnection.CommunicationHandler;
import com.vaadin.client.ApplicationConnection.RequestStartingEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingEndedEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingStartedEvent;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(com.vaadin.addon.touchkit.rootextensions.OfflineModeSettings.class)
public class OfflineModeConnector extends AbstractExtensionConnector implements
        CommunicationHandler, CommunicationErrorHandler {
    private static final int MAX_SUSPENDED_TIMEOUT = 5000;
    boolean online = isNetworkOnline();
    boolean forcedOffline = false;
    private Timer requestTimeoutTracker = new Timer() {
        @Override
        public void run() {
            goOffline(
                    "The response from the server seems to take a very long time. "
                            + "Either the server is down or there's a network issue.",
                    -1);
        }
    };

    private TouchKitOfflineApp offlineApp;
    private boolean onlineAppStarted = false;
    private int offlineTimeoutMillis;
    private boolean applicationStarted = false;

    public OfflineModeConnector() {
        super();
        registerRpc(OfflineModeClientRpc.class, new OfflineModeClientRpc() {
            @Override
            public void goOffline() {
                forcedOffline = true;
                OfflineModeConnector.this.goOffline("Going offline", 0);
            }
        });
    }

    @Override
    public OfflineModeState getState() {
        return (OfflineModeState) super.getState();
    }

    protected void init() {
        offlineTimeoutMillis = readOfflineTimeout() * 1000;
        getConnection().addHandler(RequestStartingEvent.TYPE, this);
        getConnection().addHandler(ResponseHandlingStartedEvent.TYPE, this);
        getConnection().addHandler(ResponseHandlingEndedEvent.TYPE, this);
        getConnection().setCommunicationErrorDelegate(this);
    }

    private static native final int readOfflineTimeout()
    /*-{
        try {
            return $wnd.vaadin.touchkit.offlineTimeout;
        } catch(e) {
            return 10;
        }
     }-*/;

    private void onlineApplicationStarted() {
        onlineAppStarted = true;
        getOfflineApp().onlineApplicationStarted();
    }

    public TouchKitOfflineApp getOfflineApp() {
        if (offlineApp == null) {
            offlineApp = GWT.create(TouchKitOfflineApp.class);
            offlineApp.init(this);
        }
        return offlineApp;
    }

    public void goOffline(String details, int statusCode) {
        online = false;
        getConnection().setApplicationRunning(false);
        if (!getOfflineApp().isActive()) {
            getOfflineApp().activate(details, statusCode);
            if (!isNetworkOnline()) {
                Scheduler.get()
                        .scheduleFixedPeriod(new CheckForNetwork(), 1000);
            }
        }
    }

    public void resume() {
        getConnection().setApplicationRunning(true);
    }

    public void reload() {
        Window.Location.reload();
    }

    public static native boolean isNetworkOnline()
    /*-{
        if($wnd.navigator.onLine != undefined) {
            return $wnd.navigator.onLine;
        }
        return true;
    }-*/;

    /**
     * Polls whether network connection has returned
     */
    public class CheckForNetwork implements RepeatingCommand {
        public boolean execute() {
            boolean networkOnline = isNetworkOnline();

            if (networkOnline) {
                VConsole.log("The network connection returned, going back online.");
                getOfflineApp().deactivate();
            }
            return !networkOnline;
        }

    }

    @Override
    public boolean onError(String details, int statusCode) {
        VConsole.log("Going offline due to communication error");
        goOffline(details, statusCode);
        return true;
    }

    @Override
    public void onRequestStarting(RequestStartingEvent e) {
        if (!applicationStarted) {
            if (isNetworkOnline()) {
                online = true;
            } else {
                goOffline("No network connection", -1);
            }
            applicationStarted = true;
        }

        if (offlineTimeoutMillis >= 0) {
            requestTimeoutTracker.schedule(offlineTimeoutMillis);
        }
    }

    @Override
    public void onResponseHandlingStarted(ResponseHandlingStartedEvent e) {
        requestTimeoutTracker.cancel();
        if (getConnection().isApplicationRunning() && online
                && !onlineAppStarted) {
            onlineApplicationStarted();
        }
        if (forcedOffline && !getOfflineApp().isActive()) {
            forcedOffline = false;
        }
        deactivateOfflineAppIfOnline();
    }

    @Override
    public void onResponseHandlingEnded(ResponseHandlingEndedEvent e) {
        requestTimeoutTracker.cancel();
        updateSessionCookieExpiration();
    }

    private void deactivateOfflineAppIfOnline() {
        if (!online && !forcedOffline) {
            // We got a response although we were supposed to be offline.
            // Possibly a very sluggish xhr finally arrived. Skip paint phase as
            // resuming will repaint the screen anyways.
            VConsole.log("Recieved a response while offline, going back online");
            getOfflineApp().deactivate();
        }
    }

    private void updateSessionCookieExpiration() {
        if (getState().persistentSessionTimeout != null) {
            String cookie = Cookies.getCookie("JSESSIONID");
            Date date = new Date();
            date = new Date(date.getTime()
                    + getState().persistentSessionTimeout * 1000L);
            Cookies.setCookie("JSESSIONID", cookie, date);
        }
    }

    @Override
    protected void extend(ServerConnector target) {
        // TODO WTF should be be done here?
        
    }
}
