package org.vaadin.touchkit.gwt.client.vcom;

import java.util.List;

import org.vaadin.touchkit.gwt.client.ui.VerticalComponentGroupWidget;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractLayoutConnector;
import com.vaadin.client.ui.Icon;
import com.vaadin.shared.ComponentConstants;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.ComponentStateUtil;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.client.ui.AbstractFieldConnector;

@SuppressWarnings("serial")
@Connect(VerticalComponentGroup.class)
public class VerticalComponentGroupConnector extends AbstractLayoutConnector {

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
    }

    @Override
    public void onConnectorHierarchyChange(
            ConnectorHierarchyChangeEvent event) {
        if (getParent() == null) {
            // Component is removed, skip stuff to save user from JS exceptions
            // and some milliseconds of lost life
            return;
        }

        List<ComponentConnector> oldChildren = event.getOldChildren();

        List<ComponentConnector> children = getChildComponents();
        for (int i = 0; i < children.size(); ++i) {
            ComponentConnector connector = children.get(i);
            Widget widget = connector.getWidget();
            getWidget().addOrMove(widget, i);
            oldChildren.remove(connector);
        }

        for (ComponentConnector oldChild : event.getOldChildren()) {
            if (oldChild.getParent() != this) {
                getWidget().remove(oldChild.getWidget());
            }
        }
    }

    @Override
    protected VerticalComponentGroupWidget createWidget() {
        return GWT.create(VerticalComponentGroupWidget.class);
    }

    @Override
    public VerticalComponentGroupWidget getWidget() {
        return (VerticalComponentGroupWidget) super.getWidget();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        URLReference urlReference = connector.getState().resources
                .get(ComponentConstants.ICON_RESOURCE);
        Icon icon = null;
        if (urlReference != null) {
            icon = getConnection().getIcon(urlReference.getURL());
        }
        Widget child = connector.getWidget();
        String style = "v-caption";
        if (ComponentStateUtil.hasStyles(connector.getState())) {
            for (String customStyle : connector.getState().styles) {
                style += " " + "v-caption-" + customStyle;
            }
        }
        if (connector instanceof AbstractFieldConnector) {
            AbstractFieldConnector field = (AbstractFieldConnector) connector;
            if (field.isRequiredIndicatorVisible()) {
                style += " v-caption-required";
            }
        }
        getWidget().updateCaption(child, connector.getState().caption, icon,
                connector.getState().width, style);
    }
}
