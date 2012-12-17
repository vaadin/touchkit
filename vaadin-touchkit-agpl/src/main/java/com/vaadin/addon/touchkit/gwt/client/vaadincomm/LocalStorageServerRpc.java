package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.shared.communication.ServerRpc;

public interface LocalStorageServerRpc extends ServerRpc {

   void onValueDetected(int requestId, String value);
}
