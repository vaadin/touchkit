package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import org.vaadin.touchkit.ui.NavigationButton.NavigationButtonClickListener;

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
