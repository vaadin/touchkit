package com.vaadin.addon.touchkit.extensions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.vaadin.addon.touchkit.gwt.client.vcom.GeolocatorClientRpc;
import com.vaadin.addon.touchkit.gwt.client.vcom.GeolocatorServerRpc;
import com.vaadin.addon.touchkit.gwt.client.vcom.Position;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Extension;
import com.vaadin.ui.UI;

/**
 * Geolocator extension can be used to detect clients geographical location,
 * direction, altitude etc.
 * <p>
 * Under the hood Geolocator is a UI extension and should be used via static
 * {@link #detect(PositionCallback)} method. As the detection happens on the
 * client side, detection is asynchronous and result is passed to given
 * {@link PositionCallback}.
 */
@SuppressWarnings("serial")
public class Geolocator extends AbstractExtension {
    private Map<Integer, PositionCallback> callbacks = new HashMap<Integer, PositionCallback>();

    /**
     * Detects the current geographic location of the client. The detection
     * happens asynchronously and the position is reported to the callback given
     * as an argument. Note that you may need to call this method multiple
     * times, as the location changes if the client moves.
     * 
     * @param positionCallback
     */
    public static void detect(PositionCallback callback) {
        Geolocator locator = null;
        // Do we already have a Geolocator attached?
        for (Extension e : UI.getCurrent().getExtensions()) {
            if (e instanceof Geolocator) {
                locator = (Geolocator) e;
            }
        }
        // Attach a Geolocator if none found.
        if (locator == null) {
            locator = new Geolocator();
            locator.extend(UI.getCurrent());
        }
        locator.detectCurrentPosition(callback);
    }

    private Geolocator() {
        registerRpc(new GeolocatorServerRpc() {
            @Override
            public void onGeolocationSuccess(int callbackId, Position position) {
                callbacks.remove(callbackId).onSuccess(position);
            }

            @Override
            public void onGeolocationError(int callbackId, int errorCode) {
                callbacks.remove(callbackId).onFailure(errorCode);
            }
        });
    }

    /**
     * Asks the client for the current location information.
     * 
     * @param callback
     */
    private void detectCurrentPosition(PositionCallback callback) {
        int callbackid = 0;
        while (callbacks.containsKey(callbackid)) {
            callbackid = new Random().nextInt();
        }
        callbacks.put(callbackid, callback);
        getRpcProxy(GeolocatorClientRpc.class)
                .detectCurrentPosition(callbackid);
    }

}
