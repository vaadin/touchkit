package com.vaadin.addon.touchkit.gwt.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.vaadin.terminal.gwt.client.ApplicationConfiguration;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ValueMap;

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

    private static final int MAX_TIMEOUT = 5000;
    private Collection<Object> locks = new ArrayList<Object>();
    private Date start;
    private String jsonText;
    private ValueMap json;
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

    /**
     * TODO remove this before release.
     */
    Timer forceHandleMessage = new Timer() {
        @Override
        public void run() {
            VConsole.log("WARNING: rendering was never resumed, forcing reload...");
            Location.reload();
        }
    };

    private TouchKitOfflineApp offlineApp;
    private boolean onlineAppStarted = false;

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

            if (locks.isEmpty()) {
                super.handleUIDLMessage(start, jsonText, json);
            } else {
                VConsole.log("Postponing UIDL handling due to lock...");
                this.start = start;
                this.jsonText = jsonText;
                this.json = json;
                forceHandleMessage.schedule(MAX_TIMEOUT);
            }
            if (getView() instanceof VTouchKitView) {
                ((VTouchKitView) getView()).updateSessionCookieExpiration();
            }
        }
    }

    @Override
    protected void endRequest() {
        super.endRequest();
        if (applicationRunning && online && !onlineAppStarted) {
            onlineApplicationStarted();
        }
    }

    private void onlineApplicationStarted() {
        onlineAppStarted = true;
        getOfflineApp().onlineApplicationStarted();
    }

    /**
     * This method can be used to postpone rendering of a response for a short
     * period of time (e.g. to avoid rendering process during animation).
     * 
     * @param lock
     */
    public void suspendRendering(Object lock) {
        locks.add(lock);
    }

    public void resumeRendering(Object lock) {
        VConsole.log("...resuming UIDL handling.");
        locks.remove(lock);
        if (locks.isEmpty()) {
            VConsole.log("locks empty, resuming really");
            forceHandleMessage.cancel();
            handlePendingMessage();
        }
    }

    private void handlePendingMessage() {
        if (json != null) {
            super.handleUIDLMessage(start, jsonText, json);
            json = null;
        }
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
        requestTimeoutTracker.schedule(MAX_TIMEOUT * 2);
    }

    public void resume() {
        ApplicationConfiguration.getRunningApplications().remove(this);
        resetInitializedFlag(getConfiguration().getRootPanelId());
        ApplicationConfiguration.initConfigurations();
        ApplicationConfiguration.startNextApplication();
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
