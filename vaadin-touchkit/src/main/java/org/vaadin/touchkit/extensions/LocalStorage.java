package org.vaadin.touchkit.extensions;

import java.util.HashMap;

import org.vaadin.touchkit.extensions.LocalStorageCallback.FailureEvent;
import org.vaadin.touchkit.gwt.client.vcom.LocalStorageClientRpc;
import org.vaadin.touchkit.gwt.client.vcom.LocalStorageServerRpc;

import com.google.gwt.storage.client.Storage;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Extension;
import com.vaadin.ui.UI;

/**
 * A server side proxy for the browser's HTML5 local storage. Local storage is a
 * persistent string-string map provided by modern browsers. Using this
 * extension, server side code can access that map.
 * <p>
 * Local storage can be used bit like traditional cookies to store data on the
 * browser, but with LocalStorage one can store much larger data and the stored
 * data is not transferred in each HTTP request. Also if you have hybrid
 * client-server application, you can use this class to easily fetch date stored
 * by your client side module.
 * <p>
 * For more details about HTML 5 storage see {@link Storage}.
 * <p>
 * To save a value in browser use {@link #put(String, String)} method.
 * <p>
 * As the values are behind network, retrieving a value must be done using
 * asynchronous AP. E.g. like this:
 * <code>
LocalStorage.detectValue("myproperty",
        new LocalStorageCallback() {
            public void onSuccess(String value) {
                Notification.show("Value received:" + value);
            }

            public void onFailure(FailureEvent error) {
                Notification.show("Value retrieval failed: " + error.getMessage());
            }
        });
 * </code>
 * 
 */
@SuppressWarnings("serial")
public class LocalStorage extends AbstractExtension {
    private int requests;
    private HashMap<Integer, LocalStorageCallback> callbacks;

    /**
     * Detects the value of the given key in the client side HTML5 storage. The
     * value is detected asynchronously, as the value detection requires a
     * client server round trip.
     * <p>
     * This method uses thread local to get the currently active UI.
     * 
     * @see #get(String, LocalStorageCallback)
     */
    public static void detectValue(String key, LocalStorageCallback callback) {
        get().get(key, callback);
    }

    /**
     * Returns a local storage proxy bound to the currently active UI (detected
     * via a thread local).
     * 
     * @return an existing or newly created instance for the currently active UI
     */
    public static LocalStorage get() {
        UI ui = UI.getCurrent();
        return get(ui);
    }

    /**
     * Returns a local storage proxy bound to the given UI.
     * 
     * @param ui
     *            the UI to bind to.
     * @return A LocalStorage extension bound to the given UI. If an extension
     *         is not yet applied, a new one is created and applied to the UI.
     */
    public static LocalStorage get(UI ui) {
        if (ui == null) {
            throw new IllegalArgumentException("A UI must be provided");
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
     * Detects the value of the given key in the client side HTML5 storage. The
     * value is detected asynchronously, as the value detection requires a
     * client server round trip.
     * 
     * @param callback
     *            The {@link LocalStorageCallback} called when the value is
     *            available.
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
     * Stores a given key-value pair in the browser's local storage. Any
     * previous value is overridden.
     * <p>
     * Note that if you wish to be sure that the value is persisted properly,
     * you can use {@link #put(String, String, LocalStorageCallback)}.
     * 
     * @param key
     *            The key
     * @param value
     *            The value for the key
     */
    public void put(String key, String value) {
        getRpcProxy(LocalStorageClientRpc.class).put(nextRequestId(), key,
                value);
    }

    /**
     * Stores a given key-value pair in the browser's local storage. Any
     * previous value is overridden.
     * <p>
     * The callback given as parameter is notified if local storage access
     * succeeded or failed.
     * 
     * @param key
     *            The key
     * @param value
     *            The value for the key
     * @param callback
     *            A {@link LocalStorageCallback} to notify of success or
     *            failure.
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
