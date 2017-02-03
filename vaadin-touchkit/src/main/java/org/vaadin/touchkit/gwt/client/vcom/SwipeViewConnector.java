package org.vaadin.touchkit.gwt.client.vcom;

import org.vaadin.touchkit.gwt.client.ui.VSwipeView;
import org.vaadin.touchkit.gwt.client.ui.VSwipeView.SwipeListener;
import org.vaadin.touchkit.ui.SwipeView;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractSingleComponentContainerConnector;
import com.vaadin.shared.ui.Connect;

@Connect(SwipeView.class)
public class SwipeViewConnector extends AbstractSingleComponentContainerConnector implements
        ScrollHandler, SwipeListener {

    SwipeViewRpc rpc = RpcProxy.create(SwipeViewRpc.class, this);

    public SwipeViewConnector() {
        getWidget().addHandler(this, ScrollEvent.getType());
        getWidget().setSwipeListener(this);
    }
    
    @Override
    public SwipeViewSharedState getState() {
        return (SwipeViewSharedState) super.getState();
    }
    
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        getWidget().setScrollTop(getState().scrollTop);
    }

    @Override
    public VSwipeView getWidget() {
        return (VSwipeView) super.getWidget();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(VSwipeView.class);
    }

    @Override
    public void onScroll(ScrollEvent event) {
         rpc.setScrollTop(getWidget().getScrollTop());
    }

    @Override
    public void onSwipeBack() {
        rpc.navigateBackward();
    }

    @Override
    public void onSwipeForward() {
        rpc.navigateForward();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // NOP, not supported
    }

    @Override
    public void onConnectorHierarchyChange(
            ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
        // We always have 1 child, unless the child is hidden
        getWidget().setWidget(getContentWidget());
    }

}
