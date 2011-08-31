package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class NavigationManagerView extends AbstractTouchKitIntegrationTest {

    public NavigationManagerView() {

        NavigationManager navigationManager = new NavigationManager();

        NavigationView navigationView = new NavigationView("TestRootView");
        NavigationView secondView = new NavigationView("SecondView");

        navigationManager.setCurrentComponent(navigationView);

        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup();
        verticalComponentGroup.setCaption("Grouped navigation options");
        NavigationButton b = new NavigationButton(secondView);
        b.setDebugId("toSecondView");
        verticalComponentGroup.addComponent(b);

        navigationView.setContent(verticalComponentGroup);

        CssLayout cssLayout = new CssLayout();
        cssLayout.addComponent(new Label("Nothing here really"));
        secondView.setContent(cssLayout);

        setContent(navigationManager);

    }
}
