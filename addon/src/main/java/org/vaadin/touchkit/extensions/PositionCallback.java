package org.vaadin.touchkit.extensions;

import org.vaadin.touchkit.gwt.client.vcom.Position;

/**
 * Callback interface containing methods called by the {@link Geolocator}
 * extension when the result of an asynchronous geolocation request is
 * available.
 */
public interface PositionCallback {

    /**
     * Called when a geolocation request succeeds.
     * 
     * @param position
     *            a {@link Position} object holding the position data.
     */
    void onSuccess(Position position);

    /**
     * Called when a geolocation request fails. See
     * {@link com.google.gwt.geolocation.client.PositionError} for the error
     * code specifications.
     * 
     * @param errorCode
     *            the error code for the failure.
     */
    void onFailure(int errorCode);

}
