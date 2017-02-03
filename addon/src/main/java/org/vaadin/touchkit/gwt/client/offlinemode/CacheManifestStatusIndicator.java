package org.vaadin.touchkit.gwt.client.offlinemode;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.client.BrowserInfo;

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
    private static boolean updating;

    // Check for updates once every 30 min by default.
    // TODO(manolo): should be configurable via offline connector
    private int updateCheckInterval = 1800000;

    private static final Logger logger = Logger.getLogger(CacheManifestStatusIndicator.class.getName());

    private static boolean confirmationRequired = true;

    @Override
    public void onModuleLoad() {
        // Do nothing if document has no manifest.
        if (hasManifest()) {
            init();
        }
    }

    /**
     * Let the indicator ask the user to reload the application
     * when a new version of the app has been downloaded.
     */
    public static void setConfirmationRequired(boolean b) {
        confirmationRequired = b;
    }

    /**
     * return true if we are downloading a new version of the app.
     */
    public static boolean isUpdating() {
        return updating || getStatus() == CHECKING || getStatus() == DOWNLOADING;
    }

    /**
     * Initializes and starts the monitoring.
     */
    public void init() {
        loadSettingsFromLocalStorage();
        hookAllListeners(this);
        scheduleUpdateChecker();
        if (getStatus() == CHECKING || getStatus() == DOWNLOADING) {
            showProgress();
        }
        // Sometimes android leaves the status indicator spinning and spinning
        // and spinning...
        pollForStatusOnAndroid();
    }

    private void pollForStatusOnAndroid() {
        if (BrowserInfo.get().isAndroid()) {
            Scheduler.get().scheduleFixedPeriod(
                    new Scheduler.RepeatingCommand() {
                        @Override
                        public boolean execute() {
                            if (updating) {
                                // The normal listeners are working correctly
                                return false;
                            }
                            switch (getStatus()) {
                            case IDLE:
                                hideProgress();
                                return false;
                            case UPDATEREADY:
                                requestUpdate();
                                return false;
                            default:
                                return true;
                            }
                        }
                    }, 500);
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
                // Don't try to update cache if already updating or app is
                // paused
                if (!isUpdating()
                        && OfflineModeEntrypoint.get().getNetworkStatus()
                                .isAppRunning()) {
                    updateCache();
                }
                return true;
            }
        }, updateCheckInterval);
    }

    /**
     * Called when a cache event is triggered. All events except for error
     * events are handled here.
     * 
     * @param event
     *            The event.
     */
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
            requestUpdate();
            updating = false;
        }
    }

    /**
     * Called when an error event is triggered.
     * 
     * @param event
     *            The error event.
     */
    protected void onError(Event event) {
        logger.severe("An error occurred");
    }

    /**
     * Shows the progress element, which, by default, is styled to be a small
     * animated spinner in the top right corner of the screen.
     */
    protected void showProgress() {
        if (progressElement == null) {
            progressElement = Document.get().createDivElement();
            progressElement.addClassName("v-cache-loading-indicator");
        }
        RootPanel.getBodyElement().appendChild(progressElement);
    }

    /**
     * Hides the progress element.
     */
    protected void hideProgress() {
        if (progressElement != null) {
            progressElement.removeFromParent();
        }
    }

    /**
     * Called when a new version of the application cache (i.e. the widgetset)
     * has been detected. The default implementation asks the user if we should
     * update now unless forced.
     * 
     * @param force
     *            true to force reloading the site without asking the user.
     */
    private void requestUpdate() {
        logger.info("Application cache updated, confirmationRequired=" + confirmationRequired);
        if (!confirmationRequired || Window.confirm(updateNowMessage)) {
            Window.Location.reload();
        }
    }

    /**
     * Hooks all listeners to the specified instance.
     * 
     * @param instance
     *            the instance to hook the listeners to.
     */
    protected final native void hookAllListeners(
            CacheManifestStatusIndicator instance)
    /*-{
        $wnd.applicationCache.addEventListener('cached',
            function(event) {
                instance.@org.vaadin.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onCacheEvent(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
        $wnd.applicationCache.addEventListener('checking',
            function(event) {
                instance.@org.vaadin.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onCacheEvent(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
        $wnd.applicationCache.addEventListener('downloading',
            function(event) {
                instance.@org.vaadin.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onCacheEvent(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
        $wnd.applicationCache.addEventListener('noupdate',
            function(event) {
                instance.@org.vaadin.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onCacheEvent(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
        $wnd.applicationCache.addEventListener('updateready',
            function(event) {
                instance.@org.vaadin.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onCacheEvent(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
        $wnd.applicationCache.addEventListener('error',
            function(event) {
                instance.@org.vaadin.touchkit.gwt.client.offlinemode.CacheManifestStatusIndicator::onError(Lcom/google/gwt/user/client/Event;)(event);
            }, false);
    }-*/;

    /**
     * @return The status of the application cache. See the constants in this
     *         class for possible values.
     */
    private static native int getStatus()
    /*-{
        return $wnd.applicationCache.status;
    }-*/;

    /**
     * Asks the application cache to update itself, i.e. visit the server and
     * check if there's an update available.
     */
    protected static native void updateCache()
    /*-{
        return $wnd.applicationCache.update();
    }-*/;

    // Return true if the document has the manifest attribute
    private static native boolean hasManifest()
    /*-{
      return $doc.documentElement.hasAttribute('manifest');
    }-*/;
}
