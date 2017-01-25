package com.vaadin.addon.touchkit.itest.oldtests;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.gwt.client.theme.StyleNames;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

public class NavigationManagerView extends AbstractTouchKitIntegrationTest {

    public NavigationManagerView() {

        NavigationManager navigationManager = new NavigationManager();

        NavigationView navigationView = new NavigationView("TestRootView");
        NavigationView secondView = new NavigationView("SecondView");

        navigationManager.setCurrentComponent(navigationView);

        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup();
        verticalComponentGroup.setCaption("Grouped navigation options");
        NavigationButton b = new NavigationButton(secondView);
        b.setId("toSecondView");
        verticalComponentGroup.addComponent(b);
        
        Button button2 = new Button("Normal Button (with nav theme)");
        button2.setStyleName(StyleNames.BUTTON_NAVIGATION);
        verticalComponentGroup.addComponent(button2);
        button2.addClickListener(new ClickListener() {
            
            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show("Hi!");
            }
        });

        navigationView.setContent(verticalComponentGroup);

        CssLayout cssLayout = new CssLayout();
        cssLayout.addComponent(new Label("Nothing here really"));
        secondView.setContent(cssLayout);

        secondView.setRightComponent(new Button("Action1"));

        Toolbar toolbar = new Toolbar();
        Button button = new Button();
        button.setIcon(new ThemeResource("../runo/icons/64/folder.png"));
        toolbar.addComponent(button);
        button = new Button();
        button.setIcon(new ThemeResource("../runo/icons/64/document.png"));
        toolbar.addComponent(button);
        button = new Button();
        button.setIcon(new ThemeResource("../runo/icons/64/email.png"));
        toolbar.addComponent(button);
        button = new Button();
        button.setIcon(new ThemeResource("../runo/icons/64/reload.png"));
        toolbar.addComponent(button);
        secondView.setToolbar(toolbar);

        
        makeSmallTabletSize(navigationManager);
        addComponent(navigationManager);
    }
}
