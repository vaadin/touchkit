package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;

public class NavigationBarTest extends AbstractTouchKitIntegrationTest {
    private final Button leftButton = new Button("Button");

    public NavigationBarTest() {
        setDescription("NavigationBar test for dynamic changes");
        addComponent(makeNavigationManager());
    }

    public NavigationManager makeNavigationManager() {
        final NavigationManager navman = new NavigationManager();

        final NavigationView view = createView("Caption", "view", true);

        navman.setCurrentComponent(view);

        Button update = new Button("Update");
        update.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                leftButton.setVisible(!leftButton.isVisible());
            }
        });
        view.setRightComponent(update);
        view.setLeftComponent(leftButton);

        return navman;
    }

    private NavigationView createView(String caption, String debugId,
            boolean hasNext) {
        final CssLayout layout = new CssLayout();
        layout.setId(debugId);

        NavigationView navView = new NavigationView();
        navView.setCaption(caption);
        navView.setContent(layout);
        return navView;
    }

}
