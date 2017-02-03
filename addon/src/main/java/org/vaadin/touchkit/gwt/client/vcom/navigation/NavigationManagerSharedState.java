package org.vaadin.touchkit.gwt.client.vcom.navigation;

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.AbstractComponentContainerState;

public class NavigationManagerSharedState
        extends AbstractComponentContainerState {

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
