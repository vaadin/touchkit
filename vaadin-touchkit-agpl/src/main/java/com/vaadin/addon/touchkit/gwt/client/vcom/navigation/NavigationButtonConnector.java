package com.vaadin.addon.touchkit.gwt.client.vcom.navigation;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.ui.VNavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(NavigationButton.class)
public class NavigationButtonConnector extends AbstractComponentConnector {

    public NavigationButtonConnector() {
        getWidget().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                rpc.click();
            }
        });
    }

    @Override
    public boolean delegateCaptionHandling() {
        return false;
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(VNavigationButton.class);
    }

    @Override
    public NavigationButtonSharedState getState() {
        return (NavigationButtonSharedState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        String caption = getState().caption;
        getWidget().setText(caption);

        ServerConnector targetView = (ServerConnector) getState()
                .getTargetView();
        String targetViewCaption = getState().getTargetViewCaption();
        if (targetView == null) {
            getWidget().setTargetWidget(null);
            getWidget().setPlaceHolderCaption(targetViewCaption);
        } else {
            getWidget().setPlaceHolderCaption(null);
            getWidget().setTargetWidget(
                    ((AbstractComponentConnector) targetView).getWidget());
        }

        if (getResourceUrl(NavigationButtonSharedState.MY_ICON_RESOURCE) != null) {
            getWidget()
                    .setIcon(
                            getResourceUrl(NavigationButtonSharedState.MY_ICON_RESOURCE));
        }

        String description = getState().description;
        getWidget().setDescription(description);

    }

    @Override
    public VNavigationButton getWidget() {
        return (VNavigationButton) super.getWidget();
    }

    NavigationButtonRpc rpc = RpcProxy.create(NavigationButtonRpc.class, this);

}
