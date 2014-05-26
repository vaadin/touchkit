package com.vaadin.addon.touchkit.gwt.client.offlinemode;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Window;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationEvent;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.OfflineEvent;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.OnlineEvent;
import com.vaadin.addon.touchkit.gwt.client.vcom.OfflineModeConnector;
import com.vaadin.client.ApplicationConfiguration;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ApplicationConnection.CommunicationErrorHandler;
import com.vaadin.client.ApplicationConnection.CommunicationHandler;
import com.vaadin.client.ApplicationConnection.RequestStartingEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingEndedEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingStartedEvent;
import com.vaadin.client.VConsole;

/**
 * When this entry point starts an OfflineMode application is started.
 *
 * When the online application goes available, it deactivates the offline
 * app.
 *
 * It listen for HTML5/Cordova online/off-line events activating/deactivating
 * the offline app.
 */
public class OfflineModeEntrypoint implements EntryPoint, CommunicationHandler,
        CommunicationErrorHandler {

    private static OfflineModeEntrypoint instance;
    private static OfflineMode offlineModeApp = GWT.create(OfflineMode.class);
    private static boolean online = true;

    private OfflineModeConnector offlineModeConn = null;
    private ActivationEvent lastOfflineEvent = null;
    private boolean forcedOffline = false;

    private HasHandlers eventBus = null;
    private ApplicationConnection connection = null;

    public static boolean isNetworkOnline() {
        return online;
    }

    public static OfflineModeEntrypoint get() {
        // Shoulden't happen unless someone does not inherits TK module
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

        // Configure HTML5 off-line listeners
        configureApplicationOfflineEvents();

        // We always go off-line at the beginning until we receive
        // a Vaadin online response
        goOffline(OfflineMode.APP_STARTING);

        // Loop until vaadin application connection is loaded.
        // Normally it should be done when the OfflineModeConnector is
        // instantiated, but there could be applications not using it
        // in server side. It seems there is not other way to do this.
        Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
            int counter = 0;
            public boolean execute() {
                if (!ApplicationConfiguration.getRunningApplications().isEmpty()) {
                    configureHandlers(ApplicationConfiguration
                            .getRunningApplications().iterator().next());
                }
                // If eventually we dont get the connection we consider
                // the app offline and when it is online we will ask the user
                // to reload it instead of showing the online UI.
                return counter++ < 50 && connection == null;
            }
        }, 100);
    }

    /**
     * Set the offlineModeConnector. It's optional to use it in server
     * side since we have a loop to get the connection when available.
     */
    public void setOfflineModeConnector(OfflineModeConnector oc) {
        offlineModeConn = oc;
        configureHandlers(oc.getConnection());
    }

    private void configureHandlers(ApplicationConnection conn) {
        if (connection == null) {
            VConsole.log("Online Application has been loaded.");
            connection = conn;
            eventBus = getEventBus(conn);
            conn.addHandler(RequestStartingEvent.TYPE, this);
            conn.addHandler(ResponseHandlingStartedEvent.TYPE, this);
            conn.addHandler(ResponseHandlingEndedEvent.TYPE, this);
            conn.setCommunicationErrorDelegate(this);

            forceOnline();
        }
    }

    // This is a hack, in 7.2 we will have access to the eventBus via ApplicationConnection
    // and Touchkit 4.0 will use that approach.
    // In this version we have to use JSNI to access protected fields.
    private static native EventBus getEventBus(ApplicationConnection conn) /*-{
        return conn.@com.vaadin.client.ApplicationConnection::eventBus;
    }-*/;

    /**
     * @return the OfflineMode application.
     */
    public static OfflineMode getOfflineMode() {
        return offlineModeApp;
    }

    private void forceResume() {
        online = false;
        resume();
    }

    /**
     * Go online if we were not, deactivating off-line UI and
     * reactivating online one.
     */
    public void resume() {
        VConsole.log("resume()");
        if (!online && !forcedOffline) {
            VConsole.log("Network Back ONLINE");
            online = true;
            if (connection != null) {
                lastOfflineEvent = null;
                if (offlineModeApp.isActive()) {
                    offlineModeApp.deactivate();
                }
                connection.setApplicationRunning(true);
                eventBus.fireEvent(new OnlineEvent());
            } else {
                lastOfflineEvent = OfflineMode.ONLINE_APP_NOT_STARTED;
                offlineModeApp.activate(lastOfflineEvent);
            }
        }
    }

    /**
     * Go off-line showing off-line UI, or notify it with the last off-line event.
     */
    public void goOffline(ActivationEvent event) {
        if (lastOfflineEvent == null
                || lastOfflineEvent.getActivationReason() != event
                        .getActivationReason()) {
            VConsole.log("Network OFFLINE (" + event.getActivationReason() + ")");
            online = false;

            // We activate the off-line app when it is not already active
            // or we update it when the reason changes.
            if (!offlineModeApp.isActive()
                    || (lastOfflineEvent != null && lastOfflineEvent
                            .getActivationReason() != event
                            .getActivationReason())) {
                offlineModeApp.activate(event);
            }
            lastOfflineEvent = event;

            if (connection != null) {
                connection.setApplicationRunning(false);
                eventBus.fireEvent(new OfflineEvent(event));
            }
        }
    }

    /**
     * Synthetic off-line, normally forced from server side or from JS
     * with the window.tkGoOffline() method.
     */
    public void forceOffline(ActivationEvent event) {
        VConsole.error("Going offline due to a force offline call.");
        forcedOffline = true;
        goOffline(event);
    }

    /**
     * Remove forced offline flag, normally from server side or from JS
     * calling the window.tkGoOnline() function
     */
    public void forceOnline() {
        VConsole.error("Going online due to a force online call.");
        forcedOffline = false;
        resume();
    }

    @Override
    public boolean onError(String details, int statusCode) {
        VConsole.error("onError " + details + " " + statusCode);
        goOffline(OfflineMode.BAD_RESPONSE);
        return true;
    }

    @Override
    public void onRequestStarting(RequestStartingEvent e) {
    }

    @Override
    public void onResponseHandlingStarted(ResponseHandlingStartedEvent e) {
    }

    @Override
    public void onResponseHandlingEnded(ResponseHandlingEndedEvent e) {
        resume();
    }

    /*
     * Using this JSNI block in order to listen to certain DOM events not available
     * in GWT: HTML-5 and Cordova online/offline.
     *
     * We also listen to hash fragment changes and window post-messages, so as the app
     * is notified with offline events from the parent when it is embedded in an iframe.
     *
     * This block has a couple of hacks to force the app to go off-line:
     *   tkGoOffline() tkGoOnline()
     *
     * NOTE: Most code here is for fixing android bugs firing wrong events and setting
     * erroneously online flags when it is inside webview.
     */
    private native void configureApplicationOfflineEvents() /*-{
        var _this = this;
        var hasCordovaEvents = false;

        function offline() {
          console.log(">>> going offline.");
          var ev = @com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode::NO_NETWORK;
          _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::goOffline(*)(ev);
        }
        function online() {
          if (!_this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::forcedOffline) {
            console.log(">>> going online.");
            _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::resume()();
          } else {
            console.log(">>> keep forced off-line.");
          }
        }
        function check() {
          console.log(">>> ckeck online flag " + $wnd.navigator.onLine);
          ($wnd.navigator.onLine ? online : offline)();
        }

        // Export a couple of functions for allowing developer to switch on/offline from JS console
        $wnd.tkGoOffline = function() {
          var ev = @com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode::ACTIVATED_BY_REQUEST;
          _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::forceOffline(*)(ev);
        }
        $wnd.tkGoOnline = function() {
          _this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::forceOnline()();
        }
        // When offline is forced make any XHR fail
        var realSend = $wnd.XMLHttpRequest.prototype.send;
        $wnd.XMLHttpRequest.prototype.send = function() {
          if (_this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::forcedOffline) {
            throw "OFF_LINE_MODE_FORCED";
          } else {
            realSend.apply(this, arguments);
          }
        }

        // Listen to HTML5 offline-online events
        if ($wnd.navigator.onLine != undefined) {
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
        // Note: Safari disallows changing the 'online' property of the $wnd although it
        // is possible to change the window one.
        if ($wnd.Object.getOwnPropertyDescriptor($wnd.navigator, 'onLine').configurable) {
          Object.defineProperty($wnd.navigator, 'onLine', {
            set: function() {},
            get: function() {
              return @com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::online &&
                    !_this.@com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint::forcedOffline;
            }
          });
        }

        // Sometimes events are not passed to the app because of paused, we use a timer as well.
        setInterval(check, 30000);

        // Listen to Cordova specific online/off-line stuff
        // this needs cordova.js to be loaded in the current page.
        // It has to be done overriding ApplicationCacheSettings.
        if ($wnd.navigator.network && $wnd.navigator.network.connection && $wnd.Connection) {
          hasCordovaEvents = true;
          $doc.addEventListener("offline", offline, false);
          $doc.addEventListener("online", online, false);
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
              } // TODO: handle pause & resume messages
            }
          }, false);
          // Notify parent cordova container about the app was loaded.
          $wnd.parent.window.postMessage("touchkit-ready", "*");
        }
    }-*/;
}
