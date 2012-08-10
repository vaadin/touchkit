package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.shared.ComponentState;
import com.vaadin.shared.Connector;

@SuppressWarnings("serial")
public class NavigationButtonSharedState extends ComponentState {
    
    private String targetViewCaption;
    private Connector targetView;

    public String getTargetViewCaption() {
        return targetViewCaption;
    }
    public void setTargetViewCaption(String targetViewCaption) {
        this.targetViewCaption = targetViewCaption;
    }
    public Connector getTargetView() {
        return targetView;
    }
    public void setTargetView(Connector targetView) {
        this.targetView = targetView;
    }

}
