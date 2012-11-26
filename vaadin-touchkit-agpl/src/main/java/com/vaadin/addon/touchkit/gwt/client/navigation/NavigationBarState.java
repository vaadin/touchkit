package com.vaadin.addon.touchkit.gwt.client.navigation;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.Connector;

public class NavigationBarState extends AbstractComponentState {
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
