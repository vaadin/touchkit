package com.vaadin.addon.touchkit.gwt.client.vcom.navigation;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.Connector;

public class NavigationManagerSharedState extends AbstractComponentState {

    private Connector previousComponent;
    private Connector nextComponent;
    private Connector currentComponent;

    public Connector getPreviousComponent() {
        return previousComponent;
    }

    public void setPreviousComponent(Connector previousComponent) {
        this.previousComponent = previousComponent;
    }

    public Connector getNextComponent() {
        return nextComponent;
    }

    public void setNextComponent(Connector nextComponent) {
        this.nextComponent = nextComponent;
    }

    public Connector getCurrentComponent() {
        return currentComponent;
    }

    public void setCurrentComponent(Connector currentComponent) {
        this.currentComponent = currentComponent;
    }

}
