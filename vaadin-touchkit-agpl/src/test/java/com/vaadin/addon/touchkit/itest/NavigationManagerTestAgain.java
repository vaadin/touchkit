package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;

public class NavigationManagerTestAgain extends AbstractTouchKitIntegrationTest {

    public NavigationManagerTestAgain() {
        setDescription("Forward and backward navigation with normal buttons should work.");
        final NavigationManager manager = new NavigationManager();
        addComponent(manager);

        final NavigationView first = new NavigationView();
        final NavigationView second = new NavigationView();

        second.setContent(new Button("Go back", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                manager.navigateBack();
            }
        }));

        first.setContent(new Button("Go to second level",
                new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent event) {
                        manager.navigateTo(second);
                    }
                }));

        manager.navigateTo(first);
    }
}
