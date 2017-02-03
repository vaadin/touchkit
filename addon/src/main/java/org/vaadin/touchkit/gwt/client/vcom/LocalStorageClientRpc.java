package org.vaadin.touchkit.gwt.client.vcom;

import com.vaadin.shared.communication.ClientRpc;

public interface LocalStorageClientRpc extends ClientRpc {

    public void detectValue(int requestId, String key);

    public void put(int requestId, String key, String value);

}
