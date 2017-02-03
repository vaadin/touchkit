package org.vaadin.touchkit.gwt.client.vcom;

import org.vaadin.touchkit.extensions.LocalStorage;

import com.google.gwt.storage.client.Storage;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(LocalStorage.class)
public class LocalStorageConnector extends AbstractExtensionConnector {

    private LocalStorageServerRpc rpc = RpcProxy.create(
            LocalStorageServerRpc.class, this);

    public LocalStorageConnector() {
        registerRpc(LocalStorageClientRpc.class, new LocalStorageClientRpc() {
            @Override
            public void detectValue(int requestId, String key) {
                try {
                    String value = null;
                    Storage localStorageIfSupported = Storage
                            .getLocalStorageIfSupported();
                    if (localStorageIfSupported != null) {
                        value = localStorageIfSupported.getItem(key);
                        rpc.onValueDetected(requestId, value);
                    } else {
                        throw new Exception("Local storage not supported");
                    }
                } catch (Exception e) {
                    rpc.onValueDetectionFailure(requestId, e.getMessage());
                }
            }

            @Override
            public void put(int requestId, String key, String value) {
                try {
                    Storage localStorageIfSupported = Storage
                            .getLocalStorageIfSupported();
                    if (localStorageIfSupported != null) {
                        localStorageIfSupported.setItem(key, value);
                        rpc.putSucceeded(requestId, value);
                    } else {
                        throw new Exception("Local storage not supported!");
                    }
                } catch (Exception e) {
                    rpc.putFailed(requestId, e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    protected void extend(ServerConnector target) {
    }

}
