package com.vaadin.addon.touchkit.gwt.client.navigation;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.shared.ui.Connect;
import com.vaadin.terminal.gwt.client.ComponentConnector;
import com.vaadin.terminal.gwt.client.communication.StateChangeEvent;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentContainerConnector;

@Connect(com.vaadin.addon.touchkit.ui.NavigationBar.class)
public class NavigationBarConnector extends AbstractComponentContainerConnector {
    
    @Override
    public boolean delegateCaptionHandling() {
        return false;
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(VNavigationBar.class);
    }

    @Override
    public VNavigationBar getWidget() {
        return (VNavigationBar) super.getWidget();
    }

    @Override
    public NavigationBarState getState() {
        return (NavigationBarState) super.getState();
    }

    @Override
    public void updateCaption(ComponentConnector child) {
        // NOP, doesn't support delegated caption rendering.
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setCaption(getState().getCaption());

        if (getState().getLeftComponent() != null) {
            getWidget().setLeftWidget(
                    ((ComponentConnector) getState().getLeftComponent())
                            .getWidget());
        }
        if (getState().getRightComponent() != null) {
            getWidget().setRightWidget(
                    ((ComponentConnector) getState().getRightComponent())
                            .getWidget());
        }
        getWidget().avoidCaptionOverlap();
    }
}
