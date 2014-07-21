package com.vaadin.addon.touchkit.gwt.client.offlinemode;

import static com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.APP_STARTING;
import static com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.BAD_RESPONSE;
import static com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.FORCE_OFFLINE;
import static com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.FORCE_ONLINE;
import static com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.NETWORK_ONLINE;
import static com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.NO_NETWORK;
import static com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.ONLINE_APP_NOT_STARTED;
import static com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.RESPONSE_TIMEOUT;
import static com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.SERVER_AVAILABLE;

import java.util.logging.Logger;

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
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.OfflineEvent;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.OnlineEvent;
import com.vaadin.addon.touchkit.gwt.client.vcom.OfflineModeConnector;
import com.vaadin.client.ApplicationConfiguration;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ApplicationConnection.CommunicationErrorHandler;
import com.vaadin.client.ApplicationConnection.CommunicationHandler;
import com.vaadin.client.ApplicationConnection.ConnectionStatusEvent;
import com.vaadin.client.ApplicationConnection.ConnectionStatusEvent.ConnectionStatusHandler;
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
        CommunicationErrorHandler, ConnectionStatusHandler, RequestCallback {

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

        public boolean isAppOnline() {
            return !forcedOffline && networkOnline && serverReachable;
        }

        public boolean isNetworkOnline() {
            return !forcedOffline && networkOnline;
        }

        public boolean isServerReachable() {
            return !forcedOffline && serverReachable;
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

        // Configure HTML5 off-line listeners
        configureApplicationOfflineEvents();

        // Loop until vaadin application connection is loaded.
        // Normally it should be done when the OfflineModeConnector is
        // instantiated, but there could be applications not using it
        // in server side. It seems there is not other way to do this.
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

        // Realize soon that server is offline.
        pingToServer.schedule(2000);
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
        logger.info("Vaadin OfflineModeConnector has been started");
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
            logger.info("Vaadin online application has been started.");
            applicationConnection = conn;
            applicationConnection.addHandler(RequestStartingEvent.TYPE, this);
            applicationConnection.addHandler(ResponseHandlingStartedEvent.TYPE,
                    this);
            applicationConnection.addHandler(ResponseHandlingEndedEvent.TYPE,
                    this);
            applicationConnection.addHandler(ConnectionStatusEvent.TYPE, this);
            applicationConnection.setCommunicationErrorDelegate(this);
            dispatch(SERVER_AVAILABLE);
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
            logger.info("Dispatching: " + lastReason + " -> " + reason
                    + " flags=" + status.forcedOffline + " "
                    + status.networkOnline + " " + status.serverReachable);
            if (reason == NETWORK_ONLINE) {
                boolean sendPing = !status.isNetworkOnline();
                status.networkOnline = true;
                if (sendPing) {
                    ping();
                }
            } else if (reason == NO_NETWORK) {
                status.networkOnline = false;
                if (status.isServerReachable() || lastReason == APP_STARTING) {
                    goOffline(reason);
                }
            } else if (reason == SERVER_AVAILABLE) {
                status.serverReachable = true;
                status.networkOnline = true;
                goOnline(reason);
            } else if (reason == FORCE_OFFLINE) {
                status.forcedOffline = true;
                goOffline(reason);
            } else if (reason == FORCE_ONLINE) {
                status.forcedOffline = false;
                ping();
            } else {
                status.serverReachable = false;
                goOffline(reason);
            }
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

    /*
     * Set application heartbeat, When application is not ready we use a timer
     * instead.
     */
    private void setHeartBeatInterval(int ms) {
        if (ms <= 0) {
            if (applicationConnection != null) {
                applicationConnection.getHeartbeat().setInterval(-1);
            }
            pingToServer.cancel();
        } else {
            if (applicationConnection != null) {
                applicationConnection.getHeartbeat().setInterval(ms / 1000);
            } else {
                pingToServer.scheduleRepeating(ms);
            }
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
            lastReason = reason;
            logger.info("Network Back ONLINE (" + reason + ")");
            if (applicationConnection != null) {
                if (getOfflineMode().isActive()) {
                    getOfflineMode().deactivate();
                }
                applicationConnection.setApplicationRunning(true);
                applicationConnection.fireEvent(new OnlineEvent());
            } else {
                // TODO(manolo), we can call fetchRootConfiguration instead of
                // making the user reload the app since patch to bootstrap.js
                // exports a reference to the function.

                // Notify offline UI that the user has to reload the app.
                lastReason = ONLINE_APP_NOT_STARTED;
                getOfflineMode().activate(lastReason);
            }
            configureHeartBeat();
        }
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
        lastReason = reason;
        getOfflineMode().activate(reason);
        if (applicationConnection != null) {
            applicationConnection.setApplicationRunning(false);
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
    public boolean onError(String details, int statusCode) {
        dispatch(BAD_RESPONSE);
        return true;
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

    @Override
    public void onConnectionStatusChange(ConnectionStatusEvent event) {
        if (event.getStatus() == Response.SC_OK) {
            dispatch(SERVER_AVAILABLE);
        } else {
            dispatch(BAD_RESPONSE);
        }
    }

    /**
     * Check whether the server is reachable setting the status on the response.
     */
    public void ping() {
        logger.fine("Sending ping to server.");
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
        var hasCordovaEvents = false;

        function offline() {
          console.log(">>> Network flag is offline.");
          var ev = @com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason::NO_NETWORK;
          _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::dispatch(*)(ev);
        }
        function online() {
          console.log(">>> Network flag is online.");
          var ev = @com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason::NETWORK_ONLINE;
          _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::dispatch(*)(ev);
        }
        function pause() {
          _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::stopHeartBeat()();
        }
        function resume() {
          _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::configureHeartBeat()();
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
          var ev = @com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason::FORCE_OFFLINE;
          _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::dispatch(*)(ev);
        }
        $wnd.tkGoOnline = function() {
          $wnd.tkServerUp();
          var ev = @com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason::FORCE_ONLINE;
          _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::dispatch(*)(ev);
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

        // Listen to HTML5 offline-online events
        if ($wnd.navigator.onLine != undefined) {
          // android webview sends online/offline events when actually
          // there weren't real network changes like rotate the
          // screen, hide the keyboard, etc. So if we have detected that
          // cordova is available, we ignore html5 network events.
          $wnd.addEventListener("offline", function() {
            if (!hasCordovaEvents) offline();
          }, false);
          $wnd.addEventListener("online", function() {
            if (!hasCordovaEvents) online();
          }, false);
          // use HTML5 to test whether connection is available when the app starts
          if (!$wnd.navigator.onLine) {
            offline();
          }
        }
        // Redefine the HTML-5 onLine indicator.
        // This fixes the issue of android inside phonegap returning erroneus values.
        // It allows old vaadin apps based on testing 'onLine' flag continuing working.
        // Note: Safari disallows changing the 'online' property of the $wnd.
        if (!$wnd.navigator.hasOwnProperty('onLine') || $wnd.Object.getOwnPropertyDescriptor($wnd.navigator, 'onLine').configurable) {
          Object.defineProperty($wnd.navigator, 'onLine', {
            set: function() {},
            get: function() {
              var sts = _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::getNetworkStatus()();
              return sts.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint.NetworkStatus::isAppOnline()();
            }
          });
        }

        // Listen to Cordova specific online/off-line stuff
        // this needs cordova.js to be loaded in the current page.
        if ($wnd.navigator.network && $wnd.navigator.network.connection && $wnd.Connection) {
          hasCordovaEvents = true;
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
        // application is embedded in a Cordova iframe, so as it
        // can pass network status messages to the iframe.
        if ($wnd.postMessage) {
          $wnd.addEventListener("message", function(ev) {
            var msg = ev.data;
            console.log(">>> received window message " + msg);
            if (/^(cordova-.+)$/.test(msg)) {
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
      var app = @com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::appConf;
      var r = app && app.getConfig(key);
      // return null only in the case the value does not exist
      // otherwise return the string representation of the value.
      return r == null ? null : ('' + r);
    }-*/;

    // Return true if offline mode is enabled in this app.
    // When true we never show the offline UI when the server is unreachable.
    private boolean isOfflineModeEnabled() {
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
