package com.vaadin.addon.touchkit.gwt.client.offlinemode;

/**
 * Applications that need to have an advanced offline mode use this interface to
 * connect to TouchKit. By default the framework uses an instance of
 * {@link DefaultOfflineMode} that simply displays a sad face and a message why
 * the online app cannot be used.
 * <p>
 * Add a GWT deferred binding rule in your widgetset to replace this with your
 * own implementation. An example: <code><pre>
        <replace-with
                class="com.example.widgetset.client.MyOfflineMode">
                <when-type-is
                        class="com.vaadin.addon.touchkit.gwt.client.OfflineMode" />
        </replace-with>
 * </pre></code>
 * <p>
 */
public interface OfflineMode {

    public enum ActivationReason {
        /**
         * The device has no network connection
         */
        NO_NETWORK,
        /**
         * The server responded, but the response couldn't be parsed.
         */
        BAD_RESPONSE,
        /**
         * The offline mode activation was requested by the server side
         * application.
         */
        ACTIVATED_BY_SERVER,
        /**
         * The reason is unknown.
         */
        UNKNOWN,
        /**
         * The online app is not started.
         */
        ONLINE_APP_NOT_STARTED
    }

    /**
     * Holds the reason for why the offline mode was activated. Passed to the
     * {@link #activate(ActivationEvent)} method.
     */
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
     * This method is called when a TouchKit app decides to go offline. This
     * most commonly happens if there is no network connection available.
     * Offline mode can also be activated e.g. due to an inaccessible server,
     * bad responses or as the result of a request by the server side
     * application.
     * 
     * @param event
     *            Details about the activation.
     */
    public abstract void activate(ActivationEvent event);

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
