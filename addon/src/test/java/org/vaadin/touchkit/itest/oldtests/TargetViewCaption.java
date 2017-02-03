package org.vaadin.touchkit.itest.oldtests;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

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
