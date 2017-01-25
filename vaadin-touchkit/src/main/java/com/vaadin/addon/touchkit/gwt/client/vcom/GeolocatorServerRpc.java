package com.vaadin.addon.touchkit.gwt.client.vcom;

import com.vaadin.shared.communication.ServerRpc;

public interface GeolocatorServerRpc extends ServerRpc {

    public void onGeolocationSuccess(int callbackId, Position position);

    public void onGeolocationError(int callbackId, int errorCode);
}
