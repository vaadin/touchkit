package com.vaadin.addon.touchkit.itest.oldtests;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;

public class TargetViewCaption extends AbstractTouchKitIntegrationTest {

    public TargetViewCaption() {

        NavigationManager navigationManager = new NavigationManager();
        addComponent(navigationManager);

        NavigationView iv = new NavigationView();
        iv.setCaption("Test placeholder caption");

        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup();

        NavigationView targetView = new NavigationView();
        targetView.setCaption("FOO");
        NavigationButton navigationButton = new NavigationButton(targetView);
        navigationButton.setCaption("Button and view differs");

        verticalComponentGroup.addComponent(navigationButton);

        targetView = new NavigationView();
        targetView.setCaption("Normal situation");
        navigationButton = new NavigationButton(targetView);
        verticalComponentGroup.addComponent(navigationButton);

        targetView = new NavigationView();
        targetView.setCaption("FOO");
        navigationButton = new NavigationButton(targetView);
        navigationButton.setCaption("Explicit");
        navigationButton.setTargetViewCaption("BAR");
        verticalComponentGroup.addComponent(navigationButton);

        iv.setContent(verticalComponentGroup);

        navigationManager.setCurrentComponent(iv);

    }

}
