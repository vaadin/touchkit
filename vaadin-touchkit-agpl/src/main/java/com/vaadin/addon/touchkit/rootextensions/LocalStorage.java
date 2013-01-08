package com.vaadin.addon.touchkit.rootextensions;

import java.util.HashMap;

import com.google.gwt.storage.client.Storage;
import com.vaadin.addon.touchkit.gwt.client.vcom.LocalStorageClientRpc;
import com.vaadin.addon.touchkit.gwt.client.vcom.LocalStorageServerRpc;
import com.vaadin.addon.touchkit.rootextensions.LocalStorageCallback.FailureEvent;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Extension;
import com.vaadin.ui.UI;

/**
 * A server side proxy to browsers HTML5 local storage. Local storage is a
 * persistent string-string map provided by modern browsers. Using this
 * extension, server side code can access that "map" in browsers memory.
 * <p>
 * For more details about HTML 5 storage see {@link Storage}.
 */
@SuppressWarnings("serial")
public class LocalStorage extends AbstractExtension {
    private int requests;
    private HashMap<Integer, LocalStorageCallback> callbacks;

    /**
     * Detects the value of given key on the client side HTML5 storage. The
     * value is detected asynchronously as the value detection requires a client
     * server round trip.
     * <p>
     * This method uses thread local to get currently used UI.
     * 
     * @see #get(String, LocalStorageCallback)
     */
    public static void detectValue(String key, LocalStorageCallback callback) {
        get().get(key, callback);
    }

    /**
     * Returns a local storage proxy bound to current UI (detected via thread
     * local).
     * 
     * @return existing or newly created instance for current UI
     */
    public static LocalStorage get() {
        UI ui = UI.getCurrent();
        return get(ui);
    }

    /**
     * Returns a local storage proxy bound to given UI.
     * 
     * @param ui
     * @return LocalStorage extension bound to given UI. If extension is not yet
     *         used, a new one is created.
     */
    public static LocalStorage get(UI ui) {
        if (ui == null) {
            throw new IllegalArgumentException("UI must be provided");
        }
        LocalStorage locator = null;
        // Do we already have an extension attached?
        for (Extension e : ui.getExtensions()) {
            if (e instanceof LocalStorage) {
                locator = (LocalStorage) e;
            }
        }
        // Attach if none found.
        if (locator == null) {
            locator = new LocalStorage();
            locator.extend(UI.getCurrent());
        }

        return locator;

    }

    private LocalStorage() {
        registerRpc(new LocalStorageServerRpc() {

            @Override
            public void onValueDetected(int requestId, String value) {
                callbacks.remove(requestId).onSuccess(value);
            }

            @Override
            public void onValueDetectionFailure(int requestId,
                    final String message) {
                callbacks.remove(requestId).onFailure(
                        new LocalStorageCallback.FailureEvent() {

                            @Override
                            public String getMessage() {
                                return message;
                            }
                        });
            }

            @Override
            public void putSucceeded(int requestId, String value) {
                if (callbacks != null) {
                    LocalStorageCallback localStorageCallback = callbacks
                            .remove(requestId);
                    if (localStorageCallback != null) {
                        localStorageCallback.onSuccess(value);
                    }
                }
            }

            @Override
            public void putFailed(int requestId, final String message) {
                if (callbacks != null) {
                    LocalStorageCallback localStorageCallback = callbacks
                            .remove(requestId);
                    if (localStorageCallback != null) {
                        localStorageCallback.onFailure(new FailureEvent() {

                            @Override
                            public String getMessage() {
                                return message;
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * Detects the value of given key on the client side HTML5 storage. The
     * value is detected asynchronously as the value detection requires a client
     * server round trip.
     * 
     * @param callback
     */
    public void get(String key, LocalStorageCallback callback) {
        int requestId = nextRequestId();
        if (callbacks == null) {
            callbacks = new HashMap<Integer, LocalStorageCallback>();
        }
        callbacks.put(requestId, callback);
        getRpcProxy(LocalStorageClientRpc.class).detectValue(requestId, key);
    }

    /**
     * Puts a given key-value pair into browsers local storage. Possible
     * previously existing value is overridden.
     * <p>
     * Note, that if you wish to be sure the value is persisted properly, you
     * can use the version with callback.
     * 
     * @see #put(String, String, LocalStorageCallback)
     * 
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        getRpcProxy(LocalStorageClientRpc.class).put(nextRequestId(), key,
                value);
    }

    /**
     * Puts a given key-value pair into browsers local storage. Possible
     * previously existing value is overridden.
     * <p>
     * The callback given as parameter is notified if local storage access
     * succeeded or failed.
     * 
     * @param key
     * @param value
     * @param callback
     */
    public void put(String key, String value, LocalStorageCallback callback) {
        int requestId = nextRequestId();
        if (callbacks == null) {
            callbacks = new HashMap<Integer, LocalStorageCallback>();
        }
        callbacks.put(requestId, callback);
        getRpcProxy(LocalStorageClientRpc.class).put(requestId, key, value);
    }

    private int nextRequestId() {
        return requests++;
    }

}
