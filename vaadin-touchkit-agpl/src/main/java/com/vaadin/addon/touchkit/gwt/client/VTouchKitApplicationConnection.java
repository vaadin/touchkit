package com.vaadin.addon.touchkit.gwt.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.vaadin.client.ApplicationConfiguration;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ValueMap;

/**
 * Adds support for suspending and removing UIDL handling. Helps to implement
 * animations so that UIDL rendering doesn't make it look jumpy and the
 * communication is still safe.
 * 
 * TODO consider moving this stuff to core. Having own AC in touchkit makes it
 * hard for end users to incorporate for e.g. DontPush for websocket
 * communication (which would otherwise be perfect match for mobile webkits).
 * 
 */
public class VTouchKitApplicationConnection extends ApplicationConnection {

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

    public VTouchKitApplicationConnection() {
        super();
        offlineTimeoutMillis = readOfflineTimeout() * 1000;
    }

    private static native final int readOfflineTimeout()
    /*-{
        try {
            return $wnd.vaadin.touchkit.offlineTimeout;
        } catch(e) {
            return 10;
        }
     }-*/;

    @Override
    protected void handleWhenCSSLoaded(String jsonText, ValueMap json) {
        requestTimeoutTracker.cancel();
        super.handleWhenCSSLoaded(jsonText, json);
    }

    @Override
    protected void handleUIDLMessage(Date start, String jsonText, ValueMap json) {
        requestTimeoutTracker.cancel();
        if (!online) {
            // We got a response although we were supposed to be offline.
            // Possibly a very sluggish xhr finally arrived. Skip paint phase as
            // resuming will repaint the screen anyways.
            getOfflineApp().deactivate();
        } else {
            super.handleUIDLMessage(start, jsonText, json);
        }
    }

    @Override
    public void endRequest() {
        super.endRequest();
        if (applicationRunning && online && !onlineAppStarted) {
            onlineApplicationStarted();
        }
    }

    private void onlineApplicationStarted() {
        onlineAppStarted = true;
        getOfflineApp().onlineApplicationStarted();
    }

    @Override
    protected void showCommunicationError(String details, int statusCode) {
        goOffline(details, statusCode);
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
        applicationRunning = false;
        if (!getOfflineApp().isActive()) {
            getOfflineApp().activate(details, statusCode);
            if (!isNetworkOnline()) {
                Scheduler.get()
                        .scheduleFixedPeriod(new CheckForNetwork(), 1000);
            }
            if (hasActiveRequest()) {
                endRequest();
            }
        }
    }

    @Override
    protected void makeUidlRequest(String requestData, String extraParams,
            boolean forceSync) {
        super.makeUidlRequest(requestData, extraParams, forceSync);
        if (offlineTimeoutMillis >= 0) {
            requestTimeoutTracker.schedule(offlineTimeoutMillis);
        }
    }

    public void resume() {
        ApplicationConfiguration.getRunningApplications().remove(this);
        resetInitializedFlag(getConfiguration().getRootPanelId());
        // ApplicationConfiguration.initConfigurations();
        // ApplicationConfiguration.startNextApplication();
    }

    private static native void resetInitializedFlag(String rootPanelId)
    /*-{
        delete $wnd.vaadin.vaadinConfigurations[rootPanelId].initialized;
    }-*/;

    @Override
    public void start() {
        if (isNetworkOnline()) {
            online = true;
            super.start();
        } else {
            goOffline("No network connection", -1);
        }
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

}
