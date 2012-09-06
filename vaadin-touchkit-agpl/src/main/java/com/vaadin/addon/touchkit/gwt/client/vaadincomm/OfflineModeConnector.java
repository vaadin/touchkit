package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.vaadin.addon.touchkit.gwt.client.TouchKitOfflineApp;
import com.vaadin.client.ApplicationConnection.CommunicationErrorDelegate;
import com.vaadin.client.ApplicationConnection.CommunicationHandler;
import com.vaadin.client.ApplicationConnection.RequestEndedEvent;
import com.vaadin.client.ApplicationConnection.RequestStartedEvent;
import com.vaadin.client.ApplicationConnection.ResponseReceivedEvent;
import com.vaadin.client.VConsole;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(com.vaadin.addon.touchkit.rootextensions.OfflineModeSettings.class)
public class OfflineModeConnector extends AbstractExtensionConnector implements
        CommunicationHandler, CommunicationErrorDelegate {
    private static final int MAX_SUSPENDED_TIMEOUT = 5000;
    boolean online = false;
    private Timer requestTimeoutTracker = new Timer() {
        @Override
        public void run() {
            goOffline(
                    "Response from server seems to take very long time. "
                            + "Server is either down or there is an issue with network.",
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
                OfflineModeConnector.this.goOffline("Going offline", 0);
            }
        });
    }

    protected void init() {
        offlineTimeoutMillis = readOfflineTimeout() * 1000;
        getConnection().addHandler(RequestStartedEvent.TYPE, this);
        getConnection().addHandler(RequestEndedEvent.TYPE, this);
        getConnection().addHandler(ResponseReceivedEvent.TYPE, this);
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
    public void onRequestStarted(RequestStartedEvent e) {
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
    public void onRequestEnded(RequestEndedEvent e) {
        requestTimeoutTracker.cancel();
        if (getConnection().isApplicationRunning() && online
                && !onlineAppStarted) {
            onlineApplicationStarted();
        }
    }

    @Override
    public void onResponseReceived(ResponseReceivedEvent e) {
        requestTimeoutTracker.cancel();
        if (!online) {
            // We got a response although we were supposed to be offline.
            // Possibly a very sluggish xhr finally arrived. Skip paint phase as
            // resuming will repaint the screen anyways.
            getOfflineApp().deactivate();
        }
    }
}
