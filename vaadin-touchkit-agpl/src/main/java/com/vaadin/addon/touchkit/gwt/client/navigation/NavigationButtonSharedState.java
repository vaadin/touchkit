package com.vaadin.addon.touchkit.gwt.client.navigation;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.Connector;

@SuppressWarnings("serial")
public class NavigationButtonSharedState extends AbstractComponentState {

    public static final String MY_ICON_RESOURCE = "navbutton_icon_resource_key";
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
