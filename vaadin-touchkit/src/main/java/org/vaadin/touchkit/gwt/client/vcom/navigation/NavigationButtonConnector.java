package org.vaadin.touchkit.gwt.client.vcom.navigation;

import org.vaadin.touchkit.gwt.client.ui.VNavigationButton;
import org.vaadin.touchkit.gwt.client.ui.VNavigationManager;
import org.vaadin.touchkit.ui.NavigationButton;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.Connect;

@Connect(NavigationButton.class)
public class NavigationButtonConnector extends AbstractComponentConnector {

    public NavigationButtonConnector() {
        getWidget().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                rpc.click();
            }
        });
    }

    @Override
    public boolean delegateCaptionHandling() {
        return false;
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(VNavigationButton.class);
    }

    @Override
    public NavigationButtonSharedState getState() {
        return (NavigationButtonSharedState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        String caption = getState().caption;
        getWidget().setText(caption);
        getWidget().setEnabled(getState().enabled);

        getWidget().setIcon(getIcon());

        String description = getState().description;
        getWidget().setDescription(description);

    }

    @Override
    protected void init() {
        super.init();
        getWidget().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (!getWidget().isEnabled()) {
                    return;
                }
                VNavigationManager panel = getWidget().findNavigationPanel();
                if (panel != null) {
                    if (getTargetWidget() != null) {
                        if (getTargetWidget().getParent() == panel) {
                            panel.setCurrentWidget(getTargetWidget());
                        } else {

                            NavigationManagerConnector navigationManager;
                            ServerConnector parent2 = getParent();
                            while (parent2 != null
                                    && !(parent2 instanceof NavigationManagerConnector)) {
                                parent2 = parent2.getParent();
                            }
                            navigationManager = (NavigationManagerConnector) parent2;

                            AbstractComponentConnector previousComponent = (AbstractComponentConnector) navigationManager
                                    .getState().getPreviousComponent();
                            AbstractComponentConnector nextComponent = (AbstractComponentConnector) navigationManager
                                    .getState().getNextComponent();

                            if (previousComponent != null
                                    && getState().getTargetView()
                                            .getConnectorId() == previousComponent
                                            .getConnectorId()) {
                                // See #11436 && #11437
                                // VConsole.error("Ehh, equal with previous based on identifiers, but widget is different"
                                // + (getTargetWidget() !=
                                // previousComponent.getWidget()));
                                // get target widget widget via hierarchy
                                panel.setCurrentWidget(previousComponent
                                        .getWidget());
                            } else if (nextComponent != null
                                    && getState().getTargetView()
                                            .getConnectorId() == nextComponent
                                            .getConnectorId()) {
                                // See #11436 && #11437
                                // VConsole.error("Ehh!, equal with next based on identifiers, but widget is different"
                                // + (getTargetWidget() !=
                                // nextComponent.getWidget()));
                                panel.setCurrentWidget(nextComponent
                                        .getWidget());
                            } else {
                                panel.setNextWidget(getTargetWidget());
                                panel.navigateForward();
                            }
                        }
                    } else {
                        VConsole.error("Placeholder navigation..");
                        panel.navigateToPlaceholder(getPlaceHolderCaption());
                    }
                }
            }

        });
    }

    protected String getPlaceHolderCaption() {
        return getState().getTargetViewCaption();
    }

    private Widget getTargetWidget() {
        Connector targetView = getState().getTargetView();
        if (targetView == null) {
            return null;
        }
        return ((AbstractComponentConnector) targetView).getWidget();
    }

    @Override
    public VNavigationButton getWidget() {
        return (VNavigationButton) super.getWidget();
    }

    NavigationButtonRpc rpc = RpcProxy.create(NavigationButtonRpc.class, this);

}
