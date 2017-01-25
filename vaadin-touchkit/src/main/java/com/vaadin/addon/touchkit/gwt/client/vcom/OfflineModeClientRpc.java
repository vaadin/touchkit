package com.vaadin.addon.touchkit.gwt.client.vcom;

import com.vaadin.shared.communication.ClientRpc;

public interface OfflineModeClientRpc extends ClientRpc {
    /**
     * Make the application go offline from server side
     */
    public void goOffline();

    /**
     * Make the application go online from server side
     */
    public void goOnline();
}
