package org.vaadin.touchkit.extensions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.vaadin.touchkit.gwt.client.vcom.GeolocatorClientRpc;
import org.vaadin.touchkit.gwt.client.vcom.GeolocatorServerRpc;
import org.vaadin.touchkit.gwt.client.vcom.Position;

import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Extension;
import com.vaadin.ui.UI;

/**
 * The Geolocator extension can be used to detect the client's geographical
 * location, direction, altitude, etc.
 * <p>
 * Under the hood, Geolocator is a UI extension and should be used via the
 * static {@link #detect(PositionCallback)} method. As the detection happens on
 * the client side, it is asynchronous and the result is passed to the
 * registered {@link PositionCallback}.
 */
@SuppressWarnings("serial")
public class Geolocator extends AbstractExtension {
    private Map<Integer, PositionCallback> callbacks = new HashMap<Integer, PositionCallback>();

    /**
     * Detects the current geographic location of the client. The detection
     * happens asynchronously and the position is reported to the callback given
     * in the callback argument. Note that this only checks the position once,
     * you need to call this method multiple times if you want to update the
     * location as the client moves.
     * 
     * @param callback
     *            The {@link PositionCallback} instance that is called when the
     *            position is available.
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

    /**
     * Constructs a GeoLocator, called by the {@link #detect(PositionCallback)}
     * method.
     */
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
     * Asks the client for the current location information and passes it along
     * to the provided callback.
     * 
     * @param callback
     *            the {@link PositionCallback} to call when the position data is
     *            available.
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
