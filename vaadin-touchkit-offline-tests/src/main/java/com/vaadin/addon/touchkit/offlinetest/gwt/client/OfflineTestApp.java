package com.vaadin.addon.touchkit.offlinetest.gwt.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.vaadin.addon.touchkit.gwt.client.TouchKitOfflineApp;
import com.vaadin.addon.touchkit.gwt.client.VVerticalComponentGroup;
import com.vaadin.addon.touchkit.gwt.client.navigation.VNavigationBar;
import com.vaadin.addon.touchkit.gwt.client.navigation.VNavigationView;
import com.vaadin.addon.touchkit.gwt.client.vaadincomm.OfflineModeConnector;
import com.vaadin.client.ui.button.VButton;

public class OfflineTestApp extends TouchKitOfflineApp implements
        RepeatingCommand {
    private VButton goOnlineButton;
    private Label networkStatus;

    /**
     * Instead of the basic "no connection" screen, Vornitologist uses
     * overridden offline app that can be used to fill observation.
     */
    @Override
    protected void buildDefaultContent() {
        createUi();
    }

    private void createUi() {
        /*
         * We'll mostly use TouchKit's client side components to build to UI and
         * some of TouchKit's style names to build the offline UI. This way we
         * can get similar look and feel with the rest of the application.
         */
        VNavigationView navigationView = new VNavigationView();
        getPanel().add(navigationView);
        navigationView.setHeight("100%");
        VNavigationBar navigationBar = new VNavigationBar();
        navigationBar.setCaption("Vornitologist is offline");
        navigationView.setNavigationBar(navigationBar);

        FlowPanel content = new FlowPanel();
        navigationView.updateContent(content);

        Label label = new Label("Offline Mode");
        content.add(label);

        showRestartButton(content);
    }

    private void goOnline() {
        super.deactivate();
    }

    private void showRestartButton(Panel panel) {
        Label label = new Label("Connetion status");
        label.setStyleName("v-label-grey-title");
        panel.add(label);
        VVerticalComponentGroup vVerticalComponentGroup = new VVerticalComponentGroup();
        vVerticalComponentGroup
                .addStyleName("v-touchkit-verticalcomponentgroup");
        Panel p = (Panel) vVerticalComponentGroup.getWidget();

        goOnlineButton = new VButton();
        goOnlineButton.setText("Go online");
        goOnlineButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                goOnline();
            }
        });

        goOnlineButton.setVisible(false);

        networkStatus = new Label();
        networkStatus.getElement().getStyle().setPaddingTop(10, Unit.PX);
        networkStatus.getElement().getStyle().setPaddingBottom(10, Unit.PX);

        Scheduler.get().scheduleFixedPeriod(this, 1000);

        p.add(networkStatus);
        p.add(goOnlineButton);

        panel.add(vVerticalComponentGroup);
    }

    public boolean execute() {
        if (isActive()) {
            if (networkStatus != null) {
                if (OfflineModeConnector.isNetworkOnline()) {
                    networkStatus.setText("Your network connection is online.");
                    networkStatus.getElement().getStyle().setColor("green");
                    goOnlineButton.setVisible(true);
                } else {
                    networkStatus
                            .setText("Your network connection is offline.");
                    networkStatus.getElement().getStyle().setColor("");
                    goOnlineButton.setVisible(false);
                }
            }
            return true;
        }
        return false;
    }

}