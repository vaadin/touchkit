package com.vaadin.addon.touchkit.gwt.client.offlinemode;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This is a simple application cache monitor. It also notifies demo users when
 * offline mode is ready to be used.
 */
public class CacheManifestStatusIndicator implements EntryPoint {
    public static final String UPDATE_NOW_MSG_KEY = "updateNowMessage";
    public static final String UPDATE_CHECK_INTERVAL_KEY = "updateCheckInterval";

    private static final int UNCACHED = 0;
    private static final int IDLE = 1;
    private static final int CHECKING = 2;
    private static final int DOWNLOADING = 3;
    private static final int UPDATEREADY = 4;
    private static final int OBSOLETE = 5;

    private Element progressElement;
    private String updateNowMessage = "There are updates ready to be installed. Would you like to restart now?";
    private boolean updating;
    // Check for updates once every 30 min by default.
    private int updateCheckInterval = 1800000;

    public void onModuleLoad() {
        new CacheManifestStatusIndicator().init();
    }

    public void init() {
        loadSettingsFromLocalStorage();
        hookAllListeners(this);
        scheduleUpdateChecker();
        if (getStatus() == CHECKING || getStatus() == DOWNLOADING) {
            showProgress();
        }
    }

    /**
     * Loads the configurable settings from localstorage. The settings are
     * "updateNowMessage" for specifying the message to show when a new version
     * of the cache is ready, and "updateCheckInterval" for specifying how often
     * to check for new versions.
     */
    private void loadSettingsFromLocalStorage() {
        Storage localStorage = Storage.getLocalStorageIfSupported();
        if (localStorage != null) {
            String newMessage = localStorage.getItem(UPDATE_NOW_MSG_KEY);
            if (newMessage != null && !newMessage.isEmpty()) {
                updateNowMessage = newMessage;
            }
            String updateCheckIntervalStr = localStorage
                    .getItem(UPDATE_CHECK_INTERVAL_KEY);
            if (updateCheckIntervalStr != null
                    && !updateCheckIntervalStr.isEmpty()) {
                // The value in local storage is in seconds, but we need
                // milliseconds.
                updateCheckInterval = Integer.valueOf(updateCheckIntervalStr) * 1000;
            }
        }
    }

    /**
     * Check for updates to the application cache every 30 minutes
     */
    private void scheduleUpdateChecker() {
        Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {
            @Override
            public boolean execute() {
                if (!updating) {
                    updateCache();
                }
                return true;
            }
        }, updateCheckInterval);
    }

    protected void onCacheEvent(Event event) {
        // consoleLog(event.getType());
        if ("cached".equals(event.getType())) {
            hideProgress();
        } else if ("checking".equals(event.getType())) {
            showProgress();
        } else if ("downloading".equals(event.getType())) {
            updating = true;
            showProgress();
        } else if ("noupdate".equals(event.getType())) {
            hideProgress();
        } else if ("updateready".equals(event.getType())) {
            hideProgress();
            requestUpdate(false);
            updating = false;
        }
    }

    protected void onError(Event event) {
        consoleLog("An error occurred");
    }

    protected void showProgress() {
        if (progressElement == null) {
            progressElement = Document.get().createDivElement();
            progressElement.addClassName("v-cache-loading-indicator");
        }
        RootPanel.getBodyElement().appendChild(progressElement);
    }

    protected void hideProgress() {
        if (progressElement != null) {
            progressElement.removeFromParent();
        }
    }

    protected void requestUpdate(boolean force) {
        consoleLog("Requesting permission to update");
        if (force || Window.confirm(updateNowMessage)) {
            Window.Location.reload();
        }
    }

    protected final native void hookAllListeners(
            CacheManifestStatusIndicator instance)
    /*-{
        $wnd.applicationCache.addEventListener('cached',
            function(event) {
                instance.@com.vaadin.addon.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onCacheEvent(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
        $wnd.applicationCache.addEventListener('checking',
            function(event) {
                instance.@com.vaadin.addon.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onCacheEvent(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
        $wnd.applicationCache.addEventListener('downloading',
            function(event) {
                instance.@com.vaadin.addon.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onCacheEvent(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
        $wnd.applicationCache.addEventListener('noupdate',
            function(event) {
                instance.@com.vaadin.addon.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onCacheEvent(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
        $wnd.applicationCache.addEventListener('updateready',
            function(event) {
                instance.@com.vaadin.addon.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onCacheEvent(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
        $wnd.applicationCache.addEventListener('error',
            function(event) {
                instance.@com.vaadin.addon.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onError(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
    }-*/;

    private static native int getStatus()
    /*-{
        return $wnd.applicationCache.status;
    }-*/;

    protected static native void updateCache()
    /*-{
        return $wnd.applicationCache.update();
    }-*/;

    native void consoleLog(String message)
    /*-{
       console.log("CacheManifestStatusIndicator: " + message );
    }-*/;
}
