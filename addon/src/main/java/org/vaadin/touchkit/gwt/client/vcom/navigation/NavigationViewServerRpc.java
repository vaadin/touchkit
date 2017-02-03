package org.vaadin.touchkit.gwt.client.vcom.navigation;

import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.communication.ServerRpc;

public interface NavigationViewServerRpc extends ServerRpc {

    @Delayed(lastOnly = true)
    public void updateScrollPosition(int position);

}
