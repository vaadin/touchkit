package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.shared.communication.ServerRpc;

public interface GeolocatorServerRpc extends ServerRpc {

    public void onGeolocationSuccess(Position position);

    public void onGeolocationError(int errorCode);
}
