package com.vaadin.addon.touchkit.gwt.client.navigation;

import com.vaadin.shared.ComponentState;
import com.vaadin.shared.Connector;

@SuppressWarnings("serial")
public class NavigationButtonSharedState extends ComponentState {

    private String targetViewCaption;
    private Connector targetView;

    public String getTargetViewCaption() {
        if (targetViewCaption != null) {
            return targetViewCaption;
        }
        if (getTargetView() != null
                && ((ComponentState) getTargetView().getState()).getCaption() != null) {
            return ((ComponentState) getTargetView().getState()).getCaption();
        }
        return getCaption();
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

    @Override
    public String getCaption() {
        String caption2 = super.getCaption();
        if (caption2 == null) {
            /*
             * Use caption from target view unless explicitly set for this
             * button
             */
            if (getTargetView() != null
                    && ((ComponentState) getTargetView().getState())
                            .getCaption() != null) {
                return ((ComponentState) getTargetView().getState())
                        .getCaption();
            }
        }
        return caption2;
    }

}
