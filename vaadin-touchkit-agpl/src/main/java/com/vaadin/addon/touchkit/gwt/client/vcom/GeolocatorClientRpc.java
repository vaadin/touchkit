package com.vaadin.addon.touchkit.gwt.client.vcom;

import com.vaadin.shared.communication.ClientRpc;

public interface GeolocatorClientRpc extends ClientRpc {

    public void detectCurrentPosition(int callbackid);

}
