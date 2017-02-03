package org.vaadin.touchkit.gwt.client.vcom;

import com.vaadin.shared.annotations.Delayed;
import com.vaadin.shared.communication.ServerRpc;

public interface SwipeViewRpc extends ServerRpc {

    @Delayed(lastOnly = true)
    void setScrollTop(int scrollTop);

    void navigateForward();

    void navigateBackward();

}
