package org.vaadin.touchkit.gwt.client.vcom.navigation;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.Connector;

@SuppressWarnings("serial")
public class NavigationButtonSharedState extends AbstractComponentState {

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
