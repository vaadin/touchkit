package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.VerticalComponentGroupWidget;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;
import com.vaadin.terminal.gwt.client.ComponentConnector;
import com.vaadin.terminal.gwt.client.ConnectorHierarchyChangeEvent;
import com.vaadin.terminal.gwt.client.communication.StateChangeEvent;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentContainerConnector;

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
//        ((VerticalComponentGroupWidget) getWidget())
//                .setMarginStyles(new VMarginInfo(getState().getMarginsBitmask()));
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        List<ComponentConnector> children = getChildComponents();
        VerticalComponentGroupWidget widget = (VerticalComponentGroupWidget) getWidget();
        widget.setCaption(this.getState().getVisibleCaption());
        widget.clear();
        for (ComponentConnector connector : children) {
            URLReference urlRef = connector.getState().getIcon();
            String caption = connector.getState().getCaption();
            String url = "";
            if (urlRef != null) {
                url = urlRef.getURL();
            }

            ((VerticalComponentGroupWidget) getWidget()).add(
                    connector.getWidget(), url, caption);
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
        theWidget.setCaption(this.getState().getVisibleCaption());
    }

}
