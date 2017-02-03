package org.vaadin.touchkit.itest.navigationmanager;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationButton.NavigationButtonClickListener;

import com.vaadin.ui.Notification;

public class NavigationButtonStandaloneTest extends
        AbstractTouchKitIntegrationTest {

    @Override
    public String getDescription() {
        return "NavigationManager without navigation button, just server side calls.";
    }

    public NavigationButtonStandaloneTest() {

        NavigationButton navigationButton = new NavigationButton(
                "Buttoncaption");
        navigationButton.addClickListener(new NavigationButtonClickListener() {

            @Override
            public void buttonClick(
                    org.vaadin.touchkit.ui.NavigationButton.NavigationButtonClickEvent event) {
                Notification.show("clicked!");
            }
        });
        navigationButton.setDescription("Dippadai");
        addComponent(navigationButton);

    }

}
