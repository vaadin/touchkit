package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.VNavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.shared.ComponentState;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.communication.RpcProxy;
import com.vaadin.terminal.gwt.client.communication.StateChangeEvent;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentConnector;
import com.vaadin.terminal.gwt.client.ui.Icon;
import com.vaadin.terminal.gwt.client.ui.nativebutton.VNativeButton;

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
        getWidget().setText(getState().getCaption());

        Connector targetView = getState().getTargetView();
        if (targetView == null) {
            VConsole.error("Targetview null, using placeholder target.");
            getWidget().setTargetWidget(null);
            getWidget()
                    .setPlaceHolderCaption(getState().getTargetViewCaption());
        } else {
            VConsole.error("Targetview set.");
            getWidget().setPlaceHolderCaption(null);
            getWidget().setTargetWidget(
                    ((AbstractComponentConnector) targetView).getWidget());
            VConsole.log(((ComponentState)targetView.getState()).getDebugId());
            VConsole.log("W"+ ((AbstractComponentConnector) targetView).getWidget().getElement().getInnerText());
        }

        if (getState().getIcon() != null) {
            Icon newIcon = new Icon(getConnection());
            newIcon.setUri(getConnection().translateVaadinUri(getState().getIcon().getURL()));
            getWidget().setIcon(newIcon);
        }
        
        String description = getState().getDescription();
        getWidget().setDescription(description);

    }

    @Override
    public VNavigationButton getWidget() {
        return (VNavigationButton) super.getWidget();
    }

    NavigationButtonRpc rpc = RpcProxy.create(NavigationButtonRpc.class, this);

}
