package com.vaadin.addon.touchkit.gwt.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.shared.ui.Connect;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;

@Connect(com.vaadin.addon.touchkit.ui.TabBarView.class)
public class TabBarConnector extends AbstractComponentContainerConnector {
    @Override
    protected Widget createWidget() {
        return GWT.create(VTabBar.class);
    }

    @Override
    public VTabBar getWidget() {
        return (VTabBar) super.getWidget();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // NOOP
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        List<ComponentConnector> children = getChildComponents();
        VTabBar widget = getWidget();

        if (children.size() > 0) {
            widget.clear();
            Widget toolbar = children.get(0).getWidget();
            Widget content = null;

            if (children.size() > 1) {
                content = children.get(1).getWidget();
            }

            widget.setContent(toolbar, content);
        }

        super.onConnectorHierarchyChange(event);
    }
}
