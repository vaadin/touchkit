package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.shared.communication.ServerRpc;

public interface SwipeViewRpc extends ServerRpc {
    
    void setScrollTop(int scrollTop);
    
    void navigateForward();
    
    void navigateBackward();

}
