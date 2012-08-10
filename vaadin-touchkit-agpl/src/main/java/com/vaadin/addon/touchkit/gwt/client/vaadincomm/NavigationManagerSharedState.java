package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.shared.ComponentState;
import com.vaadin.shared.Connector;

public class NavigationManagerSharedState extends ComponentState {
    
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
