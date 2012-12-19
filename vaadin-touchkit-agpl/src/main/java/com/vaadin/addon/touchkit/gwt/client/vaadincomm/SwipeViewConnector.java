package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.ui.VSwipeView;
import com.vaadin.addon.touchkit.gwt.client.ui.VSwipeView.SwipeListener;
import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.csslayout.CssLayoutConnector;
import com.vaadin.shared.ui.Connect;

@Connect(SwipeView.class)
public class SwipeViewConnector extends CssLayoutConnector implements
        ScrollHandler, SwipeListener {

    SwipeViewRpc rpc = RpcProxy.create(SwipeViewRpc.class, this);

    public SwipeViewConnector() {
        getWidget().addHandler(this, ScrollEvent.getType());
        getWidget().setSwipeListener(this);

        // TODO needs system to disable swipes when there is ongoing
        // communication with server.
    }
    
    @Override
    public SwipeViewSharedState getState() {
        return (SwipeViewSharedState) super.getState();
    }
    
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        VConsole.error("onStateChanged for " + getState().id);
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

}
