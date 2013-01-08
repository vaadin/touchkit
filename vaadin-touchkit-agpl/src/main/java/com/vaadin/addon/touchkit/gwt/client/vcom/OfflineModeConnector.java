package com.vaadin.addon.touchkit.gwt.client.vcom;

import java.util.Date;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeActivationEventImpl;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationEvent;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason;
import com.vaadin.client.ApplicationConnection.CommunicationErrorHandler;
import com.vaadin.client.ApplicationConnection.CommunicationHandler;
import com.vaadin.client.ApplicationConnection.RequestStartingEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingEndedEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingStartedEvent;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(com.vaadin.addon.touchkit.extensions.OfflineMode.class)
public class OfflineModeConnector extends AbstractExtensionConnector implements
        CommunicationHandler, CommunicationErrorHandler {
    private static final int MAX_SUSPENDED_TIMEOUT = 5000;
    boolean online = isNetworkOnline();
    boolean forcedOffline = false;
    private Timer requestTimeoutTracker = new Timer() {
        @Override
        public void run() {
            goOffline(new OfflineModeActivationEventImpl(
                    "The response from the server seems to take a very long time. "
                            + "Either the server is down or there's a network issue.",
                    ActivationReason.BAD_RESPONSE));
        }
    };

    private int offlineTimeoutMillis;
    private boolean applicationStarted = false;

    public OfflineModeConnector() {
        super();
        registerRpc(OfflineModeClientRpc.class, new OfflineModeClientRpc() {
            @Override
            public void goOffline() {
                forcedOffline = true;
                OfflineModeConnector.this.goOffline(new OfflineModeActivationEventImpl(
                        "Offline mode started by a request",
                        ActivationReason.ACTIVATED_BY_SERVER));
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

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
    }

    private static native final int readOfflineTimeout()
    /*-{
        try {
            return $wnd.vaadin.touchkit.offlineTimeout;
        } catch(e) {
            return 10;
        }
     }-*/;

    public OfflineMode getOfflineApp() {
        return OfflineModeEntrypoint.getOfflineMode();
    }

    public void goOffline(ActivationEvent event) {
        online = false;
        getConnection().setApplicationRunning(false);
        if (!getOfflineApp().isActive()) {
            getOfflineApp().activate(event);
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
        goOffline(new OfflineModeActivationEventImpl(
                "Goind offline due to a communication error.",
                ActivationReason.BAD_RESPONSE));
        return true;
    }

    @Override
    public void onRequestStarting(RequestStartingEvent e) {
        if (!applicationStarted) {
            if (isNetworkOnline()) {
                online = true;
            } else {
                goOffline(new OfflineModeActivationEventImpl("No network connection",
                        ActivationReason.NO_NETWORK));
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
