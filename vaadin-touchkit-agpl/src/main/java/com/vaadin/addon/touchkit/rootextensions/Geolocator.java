package com.vaadin.addon.touchkit.rootextensions;

import com.vaadin.addon.touchkit.gwt.client.vaadincomm.GeolocatorClientRpc;
import com.vaadin.addon.touchkit.gwt.client.vaadincomm.GeolocatorServerRpc;
import com.vaadin.addon.touchkit.gwt.client.vaadincomm.Position;
import com.vaadin.addon.touchkit.service.PositionCallback;
import com.vaadin.terminal.AbstractExtension;
import com.vaadin.terminal.Extension;
import com.vaadin.ui.Root;

@SuppressWarnings("serial")
public class Geolocator extends AbstractExtension {
    private PositionCallback callback;

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
        for (Extension e : Root.getCurrent().getExtensions()) {
            if (e instanceof Geolocator) {
                locator = (Geolocator) e;
            }
        }
        // Attach a Geolocator if none found.
        if (locator == null) {
            locator = new Geolocator();
            locator.extend(Root.getCurrent());
        }
        locator.detectCurrentPosition(callback);
    }

    private Geolocator() {
        registerRpc(new GeolocatorServerRpc() {
            @Override
            public void onGeolocationSuccess(Position position) {
                callback.onSuccess(position);
            }

            @Override
            public void onGeolocationError(int errorCode) {
                callback.onFailure(errorCode);
            }
        });
    }

    /**
     * Asks the client for the current location information.
     * 
     * @param callback
     */
    private void detectCurrentPosition(PositionCallback callback) {
        this.callback = callback;
        getRpcProxy(GeolocatorClientRpc.class).detectCurrentPosition();
    }

}
