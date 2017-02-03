package org.vaadin.touchkit.gwt.client.offlinemode;

import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.APP_STARTED;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.APP_STARTING;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.BAD_RESPONSE;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.FORCE_OFFLINE;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.FORCE_ONLINE;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.NETWORK_ONLINE;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.NO_NETWORK;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.ONLINE_APP_NOT_STARTED;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.RESPONSE_TIMEOUT;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.SERVER_AVAILABLE;

import java.util.logging.Logger;

import org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason;
import org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.OfflineEvent;
import org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.OnlineEvent;
import org.vaadin.touchkit.gwt.client.vcom.OfflineModeConnector;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.vaadin.client.ApplicationConfiguration;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ApplicationConnection.CommunicationHandler;
import com.vaadin.client.ApplicationConnection.RequestStartingEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingEndedEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingStartedEvent;

/**
 * When this entry point starts an OfflineMode application is started.
 * 
 * When the online application goes available, it deactivates the offline
 * application.
 * 
 * It listen for HTML5 and Cordova online/off-line events
 * activating/deactivating the offline app.
 * 
 * It also observes any request to check whether the server goes unreachable,
 * and reconfigures heartbeat intervals depending on the connection status.
 */
public class OfflineModeEntrypoint implements EntryPoint, CommunicationHandler,
        RequestCallback {

    /**
     * We maintain three flags for defining application statuses.
     * 
     * To know whether the app is online anywhere, use:
     * <code>
     * OfflineModeEntrypoint.get().getNetworkStatus().isAppOnline()
     * </code>
     */
    public class NetworkStatus {
        private boolean forcedOffline = false;
        private boolean serverReachable = false;
        private boolean networkOnline = true;
        private boolean paused = false;

        public boolean isAppOnline() {
            return !forcedOffline && networkOnline && serverReachable;
        }

        public boolean isNetworkOnline() {
            return !forcedOffline && networkOnline;
        }

        public boolean isServerReachable() {
            return !forcedOffline && serverReachable;
        }

        public boolean isAppRunning() {
            return isAppOnline() && !paused;
        }
    }

    private static OfflineModeEntrypoint instance = null;
    private static OfflineMode offlineModeApp;

    private static JavaScriptObject appConf = null;

    private NetworkStatus status = new NetworkStatus();

    private OfflineModeConnector offlineModeConnector = null;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private ActivationReason lastReason = null;
    private ApplicationConnection applicationConnection = null;

    // TODO: make more parameters configurable from server-side.
    private int onlinePingInterval = 60000;
    private int offlinePingInterval = 10000;

    /*
     * We use a ping request instead of core heartbeat when the app is not
     * available. Normally when starting it with network off-line.
     */
    private final Timer pingToServer = new Timer() {
        String url;

        @Override
        public void run() {
            RequestBuilder rq = new RequestBuilder(RequestBuilder.POST, getPingUrl());
            rq.setTimeoutMillis(offlinePingInterval);
            rq.setCallback(OfflineModeEntrypoint.this);
            try {
                logger.info("Sending a ping request to the server.");
                rq.send();
            } catch (Exception e) {
                onError(null, e);
            }
        }

        private String getPingUrl() {
            if (url == null) {
                // Try to find the serviceUrl.
                // Only needed when widgetset is local or it is in a CDN.
                url = getVaadinConfValue("serviceUrl");
                if (url == null) {
                    url = GWT.getHostPageBaseURL();
                }
                url += "/PING";
                logger.info("Ping URL " + url);
            }
            return url;
        }
    };

    /**
     * @return the singletone instance of the OfflineModeEntrypoint
     */
    public static OfflineModeEntrypoint get() {
        // Shouldn't happen unless someone does not inherits TK module
        if (instance == null) {
            new OfflineModeEntrypoint().onModuleLoad();
        }
        return instance;
    }

    @Override
    public void onModuleLoad() {
        // Do not run twice.
        if (instance != null) {
            return;
        }
        instance = this;

        // Read the Javascript object with the vaadin root configuration
        appConf = getVaadinConf();

        // If application is not extending TK servlet offline-mode is not reliable
        if (!isTouchKitServlet()) {
            logger.severe("OfflineMode disabled because Servlet is not extending TouchKitServlet.");
            return;
        }

        // Application can disable offline.
        if (!isOfflineModeEnabled()) {
            logger.info("OfflineMode disabled because of server configuration.");
            return;
        }

        // We always go off-line at the beginning until we receive
        // a Vaadin online response
        dispatch(APP_STARTING);

        // Configure HTML5 and Cordova off-line listeners
        configureApplicationOfflineEvents();

        // Connection takes a while to be available
        waitForConnectionAvailable();

        // Realize soon that server is offline.
        pingToServer.schedule(2000);
    }

    /*
     * Loop until vaadin application connection is loaded.
     * Normally it should be done when the OfflineModeConnector is
     * instantiated, but there could be applications not using it
     * in server side. It seems there is not other way to do this.
     */
    private void waitForConnectionAvailable() {
        Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
            int counter = 0;
            @Override
            public boolean execute() {
                if (!ApplicationConfiguration.getRunningApplications()
                        .isEmpty()) {
                    configureHandlers(ApplicationConfiguration
                            .getRunningApplications().iterator().next());
                }
                return counter++ < 50 && applicationConnection == null;
            }
        }, 100);
    }

    /**
     * Return a network status object, so as other part of the app could have
     * info about whether the device network is online and the Vaadin server is
     * reachable.
     */
    public NetworkStatus getNetworkStatus() {
        return status;
    }

    /**
     * Set the offlineModeConnector. It's optional to use it in server side.
     */
    public void setOfflineModeConnector(OfflineModeConnector oc) {
        logger.info("Vaadin OfflineModeConnector has been started.");
        offlineModeConnector = oc;
        configureHandlers(oc.getConnection());
        onResponseHandlingEnded(null);
    }

    /*
     * When applicationConnection is available we listen to certain handlers.
     * This never happens if the app starts in offline-mode.
     */
    private void configureHandlers(ApplicationConnection conn) {
        if (applicationConnection == null) {
            logger.info("Vaadin ApplicationConnection has been started.");
            applicationConnection = conn;
            applicationConnection.addHandler(RequestStartingEvent.TYPE, this);
            applicationConnection.addHandler(ResponseHandlingStartedEvent.TYPE,
                    this);
            applicationConnection.addHandler(ResponseHandlingEndedEvent.TYPE,
                    this);
            dispatch(APP_STARTED);
        }
    }

    /**
     * Receive any activation or deactivation reason, setting the appropriate
     * flags and going Off-line or On-line in case.
     */
    public void dispatch(ActivationReason reason) {
        // If server failed when returning the widgetset configuration, we do nothing
        if (getRootResponseStatus() >= 400) {
            logger.severe("OfflineMode disabled because a bad response when fetching root configuration.");
            return;
        }
        // Only dispatch events when something changes
        if (lastReason != reason) {
            if (reason == NETWORK_ONLINE && status.isNetworkOnline()) {
                // Avoid logging a frequent case.
                return;
            }

            logger.info("Dispatching: " + lastReason + " -> " + reason
                    + " flags=" + status.forcedOffline + " "
                    + status.networkOnline + " " + status.serverReachable);

            if (reason == NETWORK_ONLINE) {
                status.networkOnline = true;
                configureHeartBeat();
                ping();
            } else if (reason == NO_NETWORK) {
                status.networkOnline = false;
                if (status.isServerReachable() || lastReason == APP_STARTING) {
                    goOffline(reason);
                }
                status.serverReachable = false;
            } else if (reason == SERVER_AVAILABLE || reason == APP_STARTED) {
                status.serverReachable = true;
                status.networkOnline = true;
                goOnline(reason);
            } else if (reason == FORCE_OFFLINE) {
                status.forcedOffline = true;
                goOffline(reason);
            } else if (reason == FORCE_ONLINE) {
                status.forcedOffline = false;
                ping();
            } else if (CacheManifestStatusIndicator.isUpdating()) {
                // When network is slow and a new version of the app is being downloaded
                // we could have unreachable responses, hence it's better to ignore it.
            } else {
                // Offline cases
                status.serverReachable = false;
                goOffline(reason);
            }
            lastReason = reason;
        }
    }

    /*
     * Configure application heartbeat depending on the status. If application
     * is not ready we use a timer instead.
     */
    private void configureHeartBeat() {
        if (status.isAppOnline() || !isOfflineModeEnabled()) {
            setHeartBeatInterval(onlinePingInterval);
        } else if (!status.isNetworkOnline()) {
            stopHeartBeat();
        } else {
            if (offlineModeConnector != null
                    && offlineModeConnector.getOfflineModeTimeout() > -1) {
                // This parameter is configurable from server via connector
                offlinePingInterval = offlineModeConnector
                        .getOfflineModeTimeout();
            }
            setHeartBeatInterval(offlinePingInterval);
        }
    }

    private void stopHeartBeat() {
        setHeartBeatInterval(-1);
    }

    private void resume() {
        status.paused = false;
        configureHeartBeat();
    }

    private void pause() {
        status.paused = true;
        stopHeartBeat();
    }

    /*
     * Set application heartbeat, When application is not ready we use a timer
     * instead.
     */
    private void setHeartBeatInterval(int ms) {
        pingToServer.cancel();
        if (applicationConnection != null) {
            applicationConnection.getHeartbeat().setInterval(ms > 0 ? ms / 1000 : -1);
        } else if (ms > 0) {
            pingToServer.scheduleRepeating(ms);
        }
    }

    /**
     * @return the OfflineMode application.
     */
    public static OfflineMode getOfflineMode() {
        if (offlineModeApp == null) {
            offlineModeApp = GWT.create(OfflineMode.class);
        }
        return offlineModeApp;
    }

    /*
     * Go online if we were not, deactivating off-line UI and reactivating the
     * online one.
     */
    private void goOnline(ActivationReason reason) {
        if (status.isAppOnline()) {
            logger.info("Network Back ONLINE (" + reason + ")");
            if (applicationConnection != null) {
                if (getOfflineMode().isActive()) {
                    getOfflineMode().deactivate();
                }
                configureHeartBeat();
                applicationConnection.fireEvent(new OnlineEvent());
            } else {
                retryApplicationConnection();
            }
        }
    }

    /*
     * This method is called when the device becomes online but the
     * app was started from cache. We rerun vaadinBootstrap in order to
     * fetch root configuration.
     */
    private void retryApplicationConnection() {
        fetchRootConfiguration(new Callback<JavaScriptObject, JavaScriptObject>() {
            @Override
            public void onSuccess(JavaScriptObject result) {
                waitForConnectionAvailable();
            }
            @Override
            public void onFailure(JavaScriptObject reason) {
                goOffline(ONLINE_APP_NOT_STARTED);
            }
        });
    }

    /*
     * Go off-line showing the off-line UI, or notify it with the last off-line
     * reason.
     */
    private void goOffline(ActivationReason reason) {
        logger.info("Network OFFLINE (" + reason + ")");
        if (!isOfflineModeEnabled()) {
            return;
        }
        getOfflineMode().activate(reason);
        if (applicationConnection != null) {
            applicationConnection.getLoadingIndicator().hide();
            applicationConnection.fireEvent(new OfflineEvent(reason));
        }
        configureHeartBeat();
    }

    @Override
    public void onResponseReceived(Request request, Response response) {
        if (response != null && response.getStatusCode() == Response.SC_OK) {
            dispatch(SERVER_AVAILABLE);
        } else {
            dispatch(RESPONSE_TIMEOUT);
        }
    }

    @Override
    public void onError(Request request, Throwable exception) {
        dispatch(exception instanceof RequestTimeoutException ? RESPONSE_TIMEOUT
                : BAD_RESPONSE);
    }

    @Override
    public void onRequestStarting(RequestStartingEvent e) {
    }

    @Override
    public void onResponseHandlingStarted(ResponseHandlingStartedEvent e) {
        dispatch(SERVER_AVAILABLE);
    }

    @Override
    public void onResponseHandlingEnded(ResponseHandlingEndedEvent e) {
        if (lastReason == APP_STARTING) {
            dispatch(SERVER_AVAILABLE);
        }
    }

    /**
     * Check whether the server is reachable setting the status on the response.
     */
    public void ping() {
        if (applicationConnection != null) {
            applicationConnection.getHeartbeat().send();
        } else {
            pingToServer.run();
        }
    }

    /*
     * Using this JSNI block in order to listen to certain DOM events not
     * available in GWT: HTML-5 and Cordova online/offline.
     * 
     * We also listen to hash fragment changes and window post-messages, so as
     * the app is notified with offline events from the parent when it is
     * embedded in an iframe.
     * 
     * This block has a couple of hacks to make the app or network go off-line:
     * tkGoOffline() tkGoOnline() tkServerDown() tkServerUp()
     * 
     * NOTE: Most code here is for fixing android bugs firing wrong events and
     * setting erroneously online flags when it is inside webview.
     */
    private native void configureApplicationOfflineEvents()
    /*-{
        var _this = this;
        // Cordova installed in current window
        var hasCordovaEvents = $wnd.navigator.network && $wnd.navigator.network.connection && $wnd.Connection;
        // Cordova installed in parent window sending network events via postMessage
        var maybeCordova = !hasCordovaEvents && $wnd.parent !== $wnd && $wnd.postMessage;
        // Use html5 online events as the last resource because of bugs in
        // android webview which sends unexpected online/offline events when actually
        // there weren't real network changes we have detected these issues when rotating
        // the screen, hiding the keyboard, focusing an input text, etc.
        var useHtml5Events = !hasCordovaEvents && $wnd.navigator.onLine != undefined;

        function offline() {
          var ev = @org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason::NO_NETWORK;
          _this.@org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::dispatch(*)(ev);
        }
        function online() {
          var ev = @org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason::NETWORK_ONLINE;
          _this.@org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::dispatch(*)(ev);
        }
        function pause() {
          _this.@org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::pause()();
        }
        function resume() {
          _this.@org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::resume()();
        }

        // Export some functions for allowing developer to switch network and server on/off from JS console
        var forceFailure = false;
        $wnd.tkServerDown = function() {
          forceFailure = true;
        }
        $wnd.tkServerUp = function() {
          forceFailure = false;
        }
        $wnd.tkGoOffline = function() {
          // We only set the server Down if we force off-line from console, because setting off-line from
          // server needs connection for setting on-line back.
          $wnd.tkServerDown();
          var ev = @org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason::FORCE_OFFLINE;
          _this.@org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::dispatch(*)(ev);
        }
        $wnd.tkGoOnline = function() {
          $wnd.tkServerUp();
          var ev = @org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason::FORCE_ONLINE;
          _this.@org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::dispatch(*)(ev);
        }
        // When offline is forced make any XHR fail
        var realSend = $wnd.XMLHttpRequest.prototype.send;
        $wnd.XMLHttpRequest.prototype.send = function() {
          if (forceFailure) {
            throw "NETWORK_FAILURE_FORCED";
          } else {
            realSend.apply(this, arguments);
          }
        }

        // Listen to HTML5 offline-online events.
        if (useHtml5Events) {
          $wnd.addEventListener("offline", offline, false);
          $wnd.addEventListener("online", online, false);
          // use HTML5 to test whether connection is available when the app starts
          if (!$wnd.navigator.onLine) {
            offline();
          }

          // Redefine the HTML-5 onLine indicator.
          // This fixes the issue of android inside phonegap returning erroneus values.
          // It allows old vaadin apps based on testing 'onLine' flag continuing working.
          // Note: Safari disallows changing the 'online' property of the $wnd.
          if (!$wnd.navigator.hasOwnProperty('onLine') || $wnd.Object.getOwnPropertyDescriptor($wnd.navigator, 'onLine').configurable) {
            Object.defineProperty($wnd.navigator, 'onLine', {
              set: function() {},
              get: function() {
                var sts = _this.@org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::getNetworkStatus()();
                return sts.@org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint.NetworkStatus::isAppOnline()();
              }
            });
          }
        }

        // Listen to Cordova specific online/off-line stuff
        // this needs cordova.js to be loaded in the current page.
        if (hasCordovaEvents) {
          $doc.addEventListener("offline", offline, false);
          $doc.addEventListener("online", online, false);
          $doc.addEventListener("pause", pause, false);
          $doc.addEventListener("resume", resume, false);
          // use Cordova to test whether connection is available when the app starts
          if ($wnd.navigator.network.connection.type == $wnd.Connection.NONE) {
            offline();
          }
        }

        // Use postMessage approach to go online-offline, useful when the
        // application is embedded in a Cordova iframe.
        if (maybeCordova) {
          $wnd.addEventListener("message", function(ev) {
            var msg = ev.data;
            console.log(">>> received window message " + msg);
            if (/^(cordova-.+)$/.test(msg)) {
              // Remove HTML5 events to avoid android devices sending erroneous events
              if (!hasCordovaEvents) {
                console.log(">>> Cordova is present, removing HTML5 events.");
                $wnd.removeEventListener("offline", offline, false);
                $wnd.removeEventListener("online", online, false);
              }
              hasCordovaEvents = true;
              // Take an action depending on the message
              if (msg == 'cordova-offline') {
                offline();
              } else if (msg == 'cordova-online') {
                online();
              } else if (msg == 'cordova-pause') {
                pause();
              } else if (msg == 'cordova-resume') {
                resume();
              }
            }
          }, false);
          // Notify parent cordova container about the app was loaded.
          $wnd.parent.window.postMessage("touchkit-ready", "*");
        }
    }-*/;

    // Try to find the vaadin config js-object when the app is
    // off-line and it has not been initialized yet
    private static native JavaScriptObject getVaadinConf()
    /*-{
      return $wnd.vaadin.getApp($wnd.vaadin.getAppIds()[0]);
    }-*/;

    // Get a vaadin config value
    private static native String getVaadinConfValue(String key)
    /*-{
      var app = @org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::appConf;
      var r = app && app.getConfig(key);
      // return null only in the case the value does not exist
      // otherwise return the string representation of the value.
      return r == null ? null : ('' + r);
    }-*/;

    // Make vaadinBootstrap rerun the fetchRootConfiguration request.
    private static native void fetchRootConfiguration(Callback<JavaScriptObject, JavaScriptObject> callback)
    /*-{
      var app = @org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::appConf;
      app && app.fetchRootConfig(function(r) {
          if (callback) {
            if (r && r.status == 200)
              callback.@com.google.gwt.core.client.Callback::onSuccess(*)(r);
            else
              callback.@com.google.gwt.core.client.Callback::onFailure(*)(r);
          }
        });
    }-*/;

    // Return true if offline mode is enabled in this app.
    // When true we never show the offline UI when the server is unreachable.
    public boolean isOfflineModeEnabled() {
        return Boolean.valueOf(getVaadinConfValue("offlineEnabled"));
    }

    // Return true if servlet is extending TouchKitServlet.
    // the 'widgetsetUrl'  attribute is set by TK servlet.
    private static boolean isTouchKitServlet() {
        return getVaadinConfValue("widgetsetUrl") != null;
    }

    // Return the http status of the fetchRootConfiguration call
    private static int getRootResponseStatus() {
        String code = getVaadinConfValue("rootResponseStatus");
        return code == null ? -1 : Integer.valueOf(code);
    }
}
