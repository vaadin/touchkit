package com.vaadin.addon.touchkit.gwt.client.vcom;

import com.google.gwt.core.client.Callback;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.vaadin.addon.touchkit.extensions.Geolocator;
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
                            public void onSuccess(Position result) {
                                com.vaadin.addon.touchkit.gwt.client.vcom.Position position = new com.vaadin.addon.touchkit.gwt.client.vcom.Position(
                                        result);
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
        // TODO WTF should be done here??
    }

}
