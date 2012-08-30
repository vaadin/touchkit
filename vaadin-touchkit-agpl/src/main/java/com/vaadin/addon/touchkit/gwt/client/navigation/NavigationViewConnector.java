package com.vaadin.addon.touchkit.gwt.client.navigation;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.shared.ui.Connect;
import com.vaadin.terminal.gwt.client.ComponentConnector;
import com.vaadin.terminal.gwt.client.ConnectorHierarchyChangeEvent;
import com.vaadin.terminal.gwt.client.communication.RpcProxy;
import com.vaadin.terminal.gwt.client.communication.StateChangeEvent;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentContainerConnector;

@Connect(com.vaadin.addon.touchkit.ui.NavigationView.class)
public class NavigationViewConnector extends
        AbstractComponentContainerConnector implements ScrollHandler {

    private NavigationBarConnector navigationBar;
    
    private HandlerRegistration scrollHandler;

    NavigationViewServerRpc rpc = RpcProxy.create(
            NavigationViewServerRpc.class, this);

    @Override
    protected Widget createWidget() {
        return GWT.create(VNavigationView.class);
    }

    @Override
    public VNavigationView getWidget() {
        return (VNavigationView) super.getWidget();
    }

    @Override
    public NavigationViewState getState() {
        return (NavigationViewState) super.getState();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // NOP not needed
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        super.onConnectorHierarchyChange(event);

        List<ComponentConnector> children = getChildComponents();
        navigationBar = (NavigationBarConnector) children.get(0);
        getWidget().setNavigationBar(navigationBar.getWidget());
        getWidget().updateContent(children.get(1).getWidget());
        getWidget().setToolbar(
                children.size() < 3 ? null : children.get(2).getWidget());
    }

    @Override
    protected void init() {
        scrollHandler = getWidget().addHandler(this, ScrollEvent.getType());
    }

    @Override
    public void onUnregister() {
        super.onUnregister();
        scrollHandler.removeHandler();
    }

    public void onScroll(ScrollEvent event) {
        if (getWidget().isAttached()) {
            // Disabled until Vaadin 7 supports delayed rpc. Sending this while
            // kinectic scrolling will cause new scroll value update back from
            // server --> earthquake effect
            //rpc.updateScrollPosition(getWidget().getScrollTop());
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        getWidget().setScrollTop(getState().getScrollPosition());
    }

}
