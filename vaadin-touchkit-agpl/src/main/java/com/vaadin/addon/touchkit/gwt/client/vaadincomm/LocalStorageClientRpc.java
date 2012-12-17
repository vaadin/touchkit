package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.shared.communication.ClientRpc;

public interface LocalStorageClientRpc extends ClientRpc {

    public void detectValue(int id, String key);

    public void put(String key, String value);

}
