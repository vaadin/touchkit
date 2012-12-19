package com.vaadin.addon.touchkit.rootextensions;

import java.util.HashMap;

import com.google.gwt.storage.client.Storage;
import com.vaadin.addon.touchkit.gwt.client.vcom.LocalStorageClientRpc;
import com.vaadin.addon.touchkit.gwt.client.vcom.LocalStorageServerRpc;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Extension;
import com.vaadin.ui.UI;

/**
 * A server side proxy to browsers HTML5 local storage. Local storage is a
 * persistent string string map provided by modern browsers. Using this
 * extension, server side code can access that map from browsers.
 * <p>
 * For more details about HTML 5 storage see {@link Storage}.
 */
@SuppressWarnings("serial")
public class LocalStorage extends AbstractExtension {
    private int requests;
    private HashMap<Integer, LocalStorageValueCallback> callbacks;

    /**
     * Detects the value of given key on the client side HTML5 storage. The
     * value is detected asynchronously as the value detection requires a client
     * server round trip.
     * <p>
     * This method uses thread local to get currently used UI.
     * 
     * @see #getValue(String, LocalStorageValueCallback)
     */
    public static void detectValue(String key, LocalStorageValueCallback callback) {
        get().getValue(key, callback);
    }

    /**
     * @return existing or newly created instance for current UI
     */
    public static LocalStorage get() {
        UI ui = UI.getCurrent();
        return get(ui);
    }

    /**
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
                callbacks.get(requestId).onSuccess(value);
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
    public void getValue(String key, LocalStorageValueCallback callback) {
        int requestId = nextRequestId();
        if (callbacks == null) {
            callbacks = new HashMap<Integer, LocalStorageValueCallback>();
        }
        callbacks.put(requestId, callback);
        getRpcProxy(LocalStorageClientRpc.class).detectValue(requestId, key);
    }
    
   public void put(String key, String value) {
       getRpcProxy(LocalStorageClientRpc.class).put(key,value);
   }

    private int nextRequestId() {
        return requests++;
    }

}
