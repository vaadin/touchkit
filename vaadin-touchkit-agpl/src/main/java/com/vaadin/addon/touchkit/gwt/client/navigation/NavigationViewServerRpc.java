package com.vaadin.addon.touchkit.gwt.client.navigation;

import com.vaadin.shared.communication.ServerRpc;

public interface NavigationViewServerRpc extends ServerRpc {

    public void updateScrollPosition(int position);

}
