package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.HorizontalComponentGroupWidget;
import com.vaadin.addon.touchkit.ui.HorizontalComponentGroup;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;

@Connect(HorizontalComponentGroup.class)
public class HorizontalComponentGroupConnector extends
        AbstractComponentContainerConnector {

    private HorizontalComponentGroupWidget theWidget;

    @Override
    public AbstractComponentGroupState getState() {
        return (AbstractComponentGroupState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        // ((HorizontalComponentGroupWidget) getWidget())
        // .setMarginStyles(new VMarginInfo(getState().getMarginsBitmask()));
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        List<ComponentConnector> children = getChildComponents();
        HorizontalComponentGroupWidget widget = (HorizontalComponentGroupWidget) getWidget();
        widget.clear();
        for (ComponentConnector connector : children) {
            // FIXME
            // URLReference urlRef = connector.getState().getIcon();
            URLReference urlRef = null;
            String caption = connector.getState().getCaption();
            String url = "";
            if (urlRef != null) {
                url = urlRef.getURL();
            }

            ((HorizontalComponentGroupWidget) getWidget()).add(connector
                    .getWidget());
        }
        super.onConnectorHierarchyChange(event);
    }

    @Override
    protected Widget createWidget() {
        theWidget = GWT.create(HorizontalComponentGroupWidget.class);
        return theWidget;
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
    }

}
