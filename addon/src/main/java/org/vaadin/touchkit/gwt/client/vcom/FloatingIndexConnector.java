package org.vaadin.touchkit.gwt.client.vcom;

import org.vaadin.touchkit.extensions.FloatingIndex;
import org.vaadin.touchkit.gwt.client.ui.FloatingIndexWidget;
import org.vaadin.touchkit.gwt.client.ui.VNavigationView;
import org.vaadin.touchkit.gwt.client.vcom.navigation.NavigationViewConnector;

import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(FloatingIndex.class)
public class FloatingIndexConnector extends AbstractExtensionConnector {

    private FloatingIndexWidget widget;

    public FloatingIndexConnector() {

    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (widget != null) {
            widget.removeFromParent();
        }
        widget = new FloatingIndexWidget();

        VNavigationView navView = ((NavigationViewConnector) getParent())
                .getWidget();

        for (int i = 0; i < getState().keys.size(); i++) {
            String string = getState().keys.get(i);
            AbstractComponentConnector c = (AbstractComponentConnector) getState().connectors
                    .get(i);
            widget.map(string, c.getWidget(), navView.getContent().getElement()
                    .getParentElement());
        }
        navView.add(widget);
    }

    @Override
    public FloatingIndexSharedState getState() {
        return (FloatingIndexSharedState) super.getState();
    }

    @Override
    protected void extend(ServerConnector target) {
    }

}
