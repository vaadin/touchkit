package org.vaadin.touchkit.gwt.client.vcom.navigation;

import java.util.List;

import org.vaadin.touchkit.gwt.client.ui.VNavigationView;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.touchkit.ui.NavigationView.class)
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
        if (getParent() == null) {
            // Component is removed, skip stuff to save user from JS exceptions
            // and some milliseconds of lost life
            return;
        }

        List<ComponentConnector> children = getChildComponents();
        navigationBar = (NavigationBarConnector) children.get(0);
        getWidget().setNavigationBar(navigationBar.getWidget());
        getWidget().setContent(children.get(1).getWidget());
        getWidget().setToolbar(
                children.size() < 3 ? null : children.get(2).getWidget());
    }

    @Override
    protected void init() {
        scrollHandler = getWidget().addScrollHandler(this);
    }

    @Override
    public void onUnregister() {
        super.onUnregister();
        scrollHandler.removeHandler();
    }

    public void onScroll(ScrollEvent event) {
        if (getWidget().isAttached()) {
            rpc.updateScrollPosition(getWidget().getScrollTop());
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        // Schedule to be the last thing to do in update batch as children
        // are not necessary update and there might not be enough stuff to
        // scroll at this point.
        Scheduler.get().scheduleFinally(new ScheduledCommand() {
            @Override
            public void execute() {
                getWidget().setScrollTop(getState().scrollPosition);
            }
        });
    }

}
