package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.VSwipeView;
import com.vaadin.addon.touchkit.gwt.client.VSwipeView.SwipeListener;
import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.shared.ui.Connect;
import com.vaadin.terminal.gwt.client.communication.RpcProxy;
import com.vaadin.terminal.gwt.client.ui.csslayout.CssLayoutConnector;

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
    public VSwipeView getWidget() {
        return (VSwipeView) super.getWidget();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(VSwipeView.class);
    }

    @Override
    public void onScroll(ScrollEvent event) {
        // FIXME don't know how to do this properly in Vaadin 7.
        // MethodInvocation? Should be
        // updated lazily to avoid disturbing stalls. Now disabled.
        // rpc.setScrollTop(getWidget().getScrollTop());
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
