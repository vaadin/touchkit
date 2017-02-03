package org.vaadin.touchkit.gwt.client.offlinemode;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Applications that need to have an advanced offline mode use this interface to
 * connect to TouchKit. By default the framework uses an instance of
 * {@link DefaultOfflineMode} that simply displays a sad face and a message why
 * the online app cannot be used.
 * <p>
 * Add a GWT deferred binding rule in your widgetset to replace this with your
 * own implementation. An example: <code>
        &lt;replace-with
                class="com.example.widgetset.client.MyOfflineMode"&gt;
                &lt;when-type-is
                        class="org.vaadin.touchkit.gwt.client.OfflineMode" /&gt;
        &lt;/replace-with&gt;
 * </code>
 * <p>
 */
public interface OfflineMode {

    /**
     * A set of activation reasons for setting the connection offline or online.
     */
    public static enum ActivationReason {
        // The device has a network connection
        NETWORK_ONLINE("The network is online."),
        // Server is accessible
        SERVER_AVAILABLE("The server is available."),
        // The device has no network connection
        NO_NETWORK("There was a network failure."),
        // The server responded, but the response couldn't be parsed.
        RESPONSE_TIMEOUT("The response from the server seems to take a very long time."
                + " Either the server is down or there's a network issue."),
        // The offline mode activation was requested by server or in console.
        FORCE_OFFLINE("Forced offline mode started by server or by a developer request."),
        // The offline mode activation was finished.
        FORCE_ONLINE("Forced offline mode finished."),
        // The reason is unknown.
        BAD_RESPONSE("The server is unreachable."),
        // The online app is still starting
        APP_STARTING("Loading the application."),
        // The vaadin app was started successfully
        APP_STARTED("Vaadin app connection has been inititalized."),
        // The online app never started
        ONLINE_APP_NOT_STARTED("The application didn't start properly."),
        // Unknown
        UNKNOWN("");

        String message;
        ActivationReason(String msg) {
            message = msg;
        }

        public String getMessage() {
            return message;
        }

        /**
         * Use this method to change or internationalize the message.
         */
        public ActivationReason setMessage(String msg) {
            message = msg;
            return this;
        }
    }

    /**
     * Holds the reason for why the offline mode was activated. Passed to the
     * {@link #activate(ActivationReason)} method.
     */
    @Deprecated
    public interface ActivationEvent {

        /**
         * @return A human readable message telling why the offline mode was
         *         activated.
         */
        String getActivationMessage();

        /**
         * @return the ActivationReason code for why the offline mode was
         *         activated.
         */
        ActivationReason getActivationReason();
    }

    /**
     * Event triggered when the application goes online
     */
    public static class OnlineEvent extends GwtEvent<OnlineEvent.OnlineHandler> {
        public static interface OnlineHandler extends EventHandler {
            public void onOnline(OnlineEvent event);
        }

        public final static Type<OnlineHandler> TYPE = new Type<OnlineHandler>();

        @Override
        public Type<OnlineHandler> getAssociatedType() {
            return TYPE;
        }

        @Override
        protected void dispatch(OnlineHandler handler) {
            handler.onOnline(this);
        }
    }

    /**
     * Event triggered when the device goes off-line
     */
    public static class OfflineEvent extends GwtEvent<OfflineEvent.OfflineHandler> {
        public static interface OfflineHandler extends EventHandler {
            public void onOffline(OfflineEvent event);
        }

        private ActivationReason reason;

        public ActivationReason getReason() {
            return reason;
        }

        public final static Type<OfflineHandler> TYPE = new Type<OfflineHandler>();

        public OfflineEvent(ActivationReason reason) {
            this.reason = reason;
        }

        @Override
        public Type<OfflineHandler> getAssociatedType() {
            return TYPE;
        }

        @Override
        protected void dispatch(OfflineHandler handler) {
            handler.onOffline(this);
        }
    }

    /**
     * This method is called when a TouchKit app decides to go offline. This
     * most commonly happens if there is no network connection available.
     * Offline mode can also be activated e.g. due to an inaccessible server,
     * bad responses or as the result of a request by the server side
     * application.
     *
     * @param event
     *            Details about the activation.
     */
    public abstract void activate(ActivationReason event);

    /**
     * This method is called when TouchKit detects that it might be possible to
     * go online again (e.g. the network connection has returned). The
     * implementation should e.g. remove or hide offline related elements from
     * the document.
     *
     * <p>
     * If you have implemented a more advanced offline mode, override this
     * method and gracefully return to normal operation. In that case return
     * false
     *
     * @return true if offline mode was shut down, false if offline mode was not
     *         shut down
     */
    public abstract boolean deactivate();

    /**
     * @return true if offline mode is currently active
     */
    public abstract boolean isActive();
}
