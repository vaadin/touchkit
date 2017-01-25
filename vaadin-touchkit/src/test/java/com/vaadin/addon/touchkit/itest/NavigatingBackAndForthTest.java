package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.v7.ui.Label;

public class NavigatingBackAndForthTest extends AbstractTouchKitIntegrationTest {

    public NavigatingBackAndForthTest() {
        final NavigationManager m = new NavigationManager();

        NavigationView view = new NavigationView("Initial");
        view.setContent(new Label(
                "Go next, then back, then next, should not fail"));
        m.navigateTo(view);

        final NavigationView another = new NavigationView("Another");

        NavigationButton navigationButton = new NavigationButton("Next");
        navigationButton.addClickListener(new NavigationButtonClickListener() {

            @Override
            public void buttonClick(NavigationButtonClickEvent event) {
                m.navigateTo(another);
            }
        });

        view.setRightComponent(navigationButton);

        addComponent(m);

    }

}
