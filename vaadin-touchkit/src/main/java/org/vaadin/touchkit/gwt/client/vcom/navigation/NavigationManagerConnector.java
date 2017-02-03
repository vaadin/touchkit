package org.vaadin.touchkit.gwt.client.vcom.navigation;

import org.vaadin.touchkit.gwt.client.ui.VNavigationManager;
import org.vaadin.touchkit.gwt.client.ui.VNavigationManager.AnimationListener;
import org.vaadin.touchkit.ui.NavigationManager;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.ui.Connect;

@Connect(NavigationManager.class)
public class NavigationManagerConnector extends
        AbstractComponentContainerConnector implements AnimationListener {

    @Override
    protected Widget createWidget() {
        VNavigationManager widget = GWT.create(VNavigationManager.class);
        widget.addAnimationListener(this);
        return widget;
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        updateWidgets();
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        // No need to do anything here as all hierarchy changes are already
        // handled by #onStateChanged
    }

    private void updateWidgets() {
        if (getParent() == null) {
            // Component is removed, skip stuff to save user from JS exceptions
            // and some milliseconds of lost life
            return;
        }

        Widget current = ((ComponentConnector) getState().getCurrentComponent())
                .getWidget();
        getWidget().setCurrentWidget(current);

        Widget previous = null;
        if (getState().getPreviousComponent() != null) {
            previous = ((ComponentConnector) getState().getPreviousComponent())
                    .getWidget();
        }
        Widget next = null;
        if (getState().getNextComponent() != null) {
            next = ((ComponentConnector) getState().getNextComponent())
                    .getWidget();
        }
        getWidget().setPreviousWidget(previous);
        getWidget().setNextWidget(next);
    }

    @Override
    public NavigationManagerSharedState getState() {
        return (NavigationManagerSharedState) super.getState();
    }

    @Override
    public VNavigationManager getWidget() {
        return (VNavigationManager) super.getWidget();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // TODO Auto-generated method stub

    }

    private final ElementResizeListener listener = new ElementResizeListener() {
        @Override
        public void onElementResize(ElementResizeEvent e) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    getWidget().resetPositionsAndChildSizes();
                }
            });
        }
    };

    @Override
    protected void init() {
        super.init();
        getLayoutManager().addElementResizeListener(getWidget().getElement(),
                listener);
    }

    @Override
    public void onUnregister() {
        super.onUnregister();
        getLayoutManager().removeElementResizeListener(
                getWidget().getElement(), listener);
    }

    @Override
    public void animationWillStart() {
        getConnection().getMessageHandler().suspendReponseHandling(this);
    }

    @Override
    public void animationDidEnd() {
        getConnection().getMessageHandler().resumeResponseHandling(this);
    }

}
