package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.communication.ServerRpc;

public interface SwipeViewRpc extends ServerRpc {

    @Delayed(lastonly = true)
    void setScrollTop(int scrollTop);
    
    void navigateForward();
    
    void navigateBackward();

}
