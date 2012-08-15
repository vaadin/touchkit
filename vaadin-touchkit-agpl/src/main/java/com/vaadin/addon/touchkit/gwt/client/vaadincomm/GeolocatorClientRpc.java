package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.shared.communication.ClientRpc;

public interface GeolocatorClientRpc extends ClientRpc {

    public void detectCurrentPosition();

}
