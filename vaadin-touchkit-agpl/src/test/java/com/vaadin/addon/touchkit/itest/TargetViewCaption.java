package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;

public class TargetViewCaption extends TouchKitWindow {

    public TargetViewCaption() {

        NavigationManager navigationManager = new NavigationManager();
        setContent(navigationManager);

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
