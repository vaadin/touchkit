package com.vaadin.addon.touchkit.gwt.client.vcom;

import com.google.gwt.storage.client.Storage;
import com.vaadin.addon.touchkit.rootextensions.LocalStorage;
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
                String value = null;
                Storage localStorageIfSupported = Storage.getLocalStorageIfSupported();
                if(localStorageIfSupported != null) {
                    value = localStorageIfSupported.getItem(key);
                }
                rpc.onValueDetected(requestId, value);
            }

            @Override
            public void put(String key, String value) {
                Storage localStorageIfSupported = Storage.getLocalStorageIfSupported();
                if(localStorageIfSupported != null) {
                    localStorageIfSupported.setItem(key, value);
                } else {
                    throw new RuntimeException("Local storage not supported!");
                }
            }
        });
    }

    @Override
    protected void extend(ServerConnector target) {
        // TODO WTF should be done here??
    }

}
