package org.vaadin.touchkit.gwt.client.vcom;

import com.vaadin.shared.communication.ServerRpc;

public interface LocalStorageServerRpc extends ServerRpc {

   void onValueDetected(int requestId, String value);

    void onValueDetectionFailure(int requestId, String message);

    void putSucceeded(int requestId, String value);

    void putFailed(int requestId, String message);
}
