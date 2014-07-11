package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;

public class ProgrammaticNavigation extends AbstractTouchKitIntegrationTest {

    public ProgrammaticNavigation() {
        setDescription("Going back&forth repeatedly should work and be animated.");
        final NavigationManager manager = new NavigationManager();

        final NavigationView first = new NavigationView();

        final NavigationView second = new NavigationView("Second view");
        second.setContent(new NavigationButton(first));
        second.setContent(new Label("Second view"));
        second.setLeftComponent(new Button("Back", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                manager.navigateBack();
            }
        }));

        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup();
        first.setContent(verticalComponentGroup);

        NavigationButton nb = new NavigationButton(
                "NavigationButton, target, no listener", second);
        verticalComponentGroup.addComponent(nb);

        nb = new NavigationButton("NavigationButton, navigateTo()");
        nb.addClickListener(new NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButtonClickEvent event) {
                manager.navigateTo(second);
            }
        });
        verticalComponentGroup.addComponent(nb);

        Button b = new Button("Button, navigateTo()",
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        manager.navigateTo(second);
                    }
                });
        verticalComponentGroup.addComponent(b);

        manager.navigateTo(first);

        addComponent(manager);
    }
}
