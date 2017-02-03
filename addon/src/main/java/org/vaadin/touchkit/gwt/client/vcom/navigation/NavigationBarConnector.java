package org.vaadin.touchkit.gwt.client.vcom.navigation;

import org.vaadin.touchkit.gwt.client.ui.VNavigationBar;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.touchkit.ui.NavigationBar.class)
public class NavigationBarConnector extends AbstractComponentContainerConnector {

    private final ElementResizeListener resizeListener = new ElementResizeListener() {
        @Override
        public void onElementResize(ElementResizeEvent e) {
            if(!alreadyLayouted) {
                NavigationBarConnector.this.getWidget().avoidCaptionOverlap();
                Scheduler.get().scheduleFinally(doResetLayouting);
                alreadyLayouted = true;
            }
        }
    };

    private boolean alreadyLayouted;
    
    private ScheduledCommand doResetLayouting = new ScheduledCommand() {
        @Override
        public void execute() {
            alreadyLayouted = false;
        }
    };

    Element[] listenedElements = new Element[3];

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
    protected void init() {
        super.init();
        listenedElements[0] = getWidget().getElement();
        listenedElements[1] = (Element) listenedElements[0]
                .getFirstChildElement().getNextSiblingElement();
        listenedElements[2] = (Element) listenedElements[1]
                .getNextSiblingElement();
        for (Element element : listenedElements) {
            getLayoutManager()
                    .addElementResizeListener(element, resizeListener);
        }

    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setCaption(getState().caption);

        if (getState().getLeftComponent() != null) {
            Widget leftWidget = ((ComponentConnector) getState()
                    .getLeftComponent()).getWidget();
            getWidget().setLeftWidget(leftWidget);
        }
        if (getState().getRightComponent() != null) {
            Widget rightWidget = ((ComponentConnector) getState()
                    .getRightComponent()).getWidget();
            getWidget().setRightWidget(rightWidget);
        }
    }

    @Override
    public void onConnectorHierarchyChange(
            ConnectorHierarchyChangeEvent connectorHierarchyChangeEvent) {
    }

    @Override
    public void onUnregister() {
        super.onUnregister();
        for (Element element : listenedElements) {
            getLayoutManager().removeElementResizeListener(element,
                    resizeListener);
        }
    }
}
