package com.vaadin.addon.touchkit.gwt.client.vcom.navigation;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.ui.VNavigationBar;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.ui.Connect;

@Connect(com.vaadin.addon.touchkit.ui.NavigationBar.class)
public class NavigationBarConnector extends AbstractComponentContainerConnector {

    private ElementResizeListener resizeListener = new ElementResizeListener() {

        @Override
        public void onElementResize(ElementResizeEvent e) {
            NavigationBarConnector.this.getWidget().avoidCaptionOverlap();
        }
    };

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

        getWidget().setCaption(getState().caption);

        if (getState().getLeftComponent() != null) {
            Widget leftWidget = ((ComponentConnector) getState()
                    .getLeftComponent()).getWidget();
            getWidget().setLeftWidget(leftWidget);
            getLayoutManager().addElementResizeListener(
                    leftWidget.getElement(), resizeListener);
        }
        if (getState().getRightComponent() != null) {
            Widget rightWidget = ((ComponentConnector) getState()
                    .getRightComponent()).getWidget();
            getWidget().setRightWidget(rightWidget);
            getLayoutManager().addElementResizeListener(
                    rightWidget.getElement(), resizeListener);
        }
        getWidget().avoidCaptionOverlap();
    }

    @Override
    public void onConnectorHierarchyChange(
            ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
    }

}
