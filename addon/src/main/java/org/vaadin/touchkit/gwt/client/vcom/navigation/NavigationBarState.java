package org.vaadin.touchkit.gwt.client.vcom.navigation;

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.AbstractComponentContainerState;

public class NavigationBarState extends AbstractComponentContainerState {
    private Connector leftComponent;
    private Connector rightComponent;

    public Connector getLeftComponent() {
        return leftComponent;
    }

    public void setLeftComponent(Connector leftComponent) {
        this.leftComponent = leftComponent;
    }

    public Connector getRightComponent() {
        return rightComponent;
    }

    public void setRightComponent(Connector rightComponent) {
        this.rightComponent = rightComponent;
    }
}
