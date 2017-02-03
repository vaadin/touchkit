package org.vaadin.touchkit.gwt.client.vcom;

import java.util.List;

import org.vaadin.touchkit.gwt.client.ui.VTabBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.shared.ui.Connect;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;

@Connect(org.vaadin.touchkit.ui.TabBarView.class)
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
        if (getParent() == null) {
            // Component is removed, skip stuff to save user from JS exceptions
            // and some milliseconds of lost life
            return;
        }
        List<ComponentConnector> children = getChildComponents();
        
        getWidget().setToolbar(children.get(0).getWidget());

        getWidget().setContent(
                children.size() > 1 ? children.get(1).getWidget() : null);

    }
}
