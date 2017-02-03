package org.vaadin.touchkit.gwt.client.vcom;

import org.vaadin.touchkit.extensions.Geolocator;

import com.google.gwt.core.client.Callback;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(Geolocator.class)
public class GeolocatorConnector extends AbstractExtensionConnector {

    private GeolocatorServerRpc rpc = RpcProxy.create(
            GeolocatorServerRpc.class, this);

    public GeolocatorConnector() {
        registerRpc(GeolocatorClientRpc.class, new GeolocatorClientRpc() {
            @Override
            public void detectCurrentPosition(final int callbackId) {
                Geolocation.getIfSupported().getCurrentPosition(
                        new Callback<Position, PositionError>() {
                            @Override
                            public void onSuccess(Position r) {
                                org.vaadin.touchkit.gwt.client.vcom.Position position = new org.vaadin.touchkit.gwt.client.vcom.Position();
                                Coordinates c = r.getCoordinates();
                                position.setLatitude(c.getLatitude());
                                position.setLongitude(c.getLongitude());
                                position.setAccuracy(c.getAccuracy());
                                // TODO This should be worked around in GWT,
                                // without this hack we'll get some sort of
                                // reference in ios that gets set to undefined at some
                                // point.
                                try {
                                    position.setAltitude(safeget("altitude", c));
                                } catch (Exception e) {
                                }
                                try {
                                    position.setAltitude(safeget(
                                            "altitudeAccuracy", c));
                                } catch (Exception e) {
                                }
                                try {
                                    position.setAltitude(safeget("heading", c));
                                } catch (Exception e) {
                                }
                                try {
                                    position.setAltitude(safeget("speed", c));
                                } catch (Exception e) {
                                }
                                rpc.onGeolocationSuccess(callbackId, position);
                            }

                            @Override
                            public void onFailure(PositionError reason) {
                                rpc.onGeolocationError(callbackId,
                                        reason.getCode());
                            }
                        });
            }
        });
    }

    @Override
    protected void extend(ServerConnector target) {
    }

    private static native double safeget(String string, Coordinates c)
    /*-{
        return c[string]/1;
    }-*/;

}
