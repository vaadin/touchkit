package com.vaadin.addon.touchkit.gwt.client;

/**
 * Applications that need a to have advanced offline mode use this interface to
 * connect to TouchKit. By default framework uses {@link DefaultOfflineMode}
 * that simply displays a sad face and a message why the online app cannot be
 * used.
 * <p>
 * To replace this with your own implementation use a GWT deferred binding
 * rule in your widgetset. An example: <code><pre>
        <replace-with
                class="com.example.widgetset.client.MyOfflineMode">
                <when-type-is
                        class="com.vaadin.addon.touchkit.gwt.client.OfflineMode" />
        </replace-with>
 * </pre></code>
 * <p>
 * 
 * 
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
        ACTIVATED_BY_SERVER, UNKNOWN, ONLINE_APP_NOT_STARTED
    }

    public interface ActivationEvent {

        /**
         * Human readable message why offline mode was activated.
         * 
         * @return
         */
        String getActivationMessage();

        ActivationReason getActivationReason();

    }

    /**
     * This method is called when TouchKit app desides to go offline. This
     * happens most commonly if there is no network connection available.
     * Offline mode can also be activated due to e.g. inaccessible server, bad
     * responses or due to a request by the server side application.
     * <p>
     * 
     * @param event
     *            containing details about the activation
     */
    public abstract void activate(ActivationEvent event);

    /**
     * This method is called when the touch kit detects that now it might be
     * possible to get online again (e.g. network connection has returned).
     * Implementation should e.g. remove or hide offline related elements from
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