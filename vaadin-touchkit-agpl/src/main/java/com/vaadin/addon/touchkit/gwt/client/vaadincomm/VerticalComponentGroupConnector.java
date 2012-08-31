package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.VerticalComponentGroupWidget;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;

@Connect(VerticalComponentGroup.class)
public class VerticalComponentGroupConnector extends
        AbstractComponentContainerConnector {

    private VerticalComponentGroupWidget theWidget;

    @Override
    public AbstractComponentGroupState getState() {
        return (AbstractComponentGroupState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        List<ComponentConnector> children = getChildComponents();
        VerticalComponentGroupWidget widget = (VerticalComponentGroupWidget) getWidget();
        widget.clear();
        for (ComponentConnector connector : children) {
            // FIXME
            // URLReference urlRef = connector.getState().getIcon();
            URLReference urlRef = null;
            String caption = connector.getState().getCaption();
            String width = connector.getState().getWidth();
            String url = "";
            if (urlRef != null) {
                url = urlRef.getURL();
            }

            ((VerticalComponentGroupWidget) getWidget()).add(
                    connector.getWidget(), url, caption, width);
        }
        super.onConnectorHierarchyChange(event);
    }

    @Override
    protected Widget createWidget() {
        theWidget = GWT.create(VerticalComponentGroupWidget.class);
        return theWidget;
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
    }
}
