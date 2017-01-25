package com.vaadin.addon.touchkit.itest.navigationmanager;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent.Direction;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class SimpleTest extends AbstractTouchKitIntegrationTest {

    @Override
    public String getDescription() {
        return "NavigationManager without navigation button, just server side calls.";
    }

    public SimpleTest() {

        setSizeFull();

        final NavigationManager navigationManager = new NavigationManager();

        final CssLayout cL = new CssLayout();
        cL.setId("l1");
        final CssLayout nL = new CssLayout();
        nL.setId("l2");
        CssLayout pL = new CssLayout();
        pL.setId("l0");
        final CssLayout yetAnother = new CssLayout();
        yetAnother.setId("l3");

        cL.addComponent(new Label("CURR"));
        Button button = new Button("-->");
        cL.addComponent(button);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigationManager.navigateTo(nL);
            }
        });
        button = new Button("<--");
        cL.addComponent(button);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigationManager.navigateBack();
            }
        });

        nL.addComponent(new Label("NEXT"));
        button = new Button("-->");
        nL.addComponent(button);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigationManager.navigateTo(yetAnother);
            }
        });
        button = new Button("<--");
        nL.addComponent(button);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigationManager.navigateBack();
            }
        });

        yetAnother.addComponent(new Label("YET ANOTHER"));
        button = new Button("<--");
        yetAnother.addComponent(button);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigationManager.navigateBack();
            }
        });

        pL.addComponent(new Label("PREV"));
        button = new Button("-->");
        pL.addComponent(button);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                navigationManager.navigateTo(cL);
                navigationManager.setNextComponent(nL);
            }
        });

        navigationManager.setPreviousComponent(pL);
        navigationManager.setCurrentComponent(cL);
        navigationManager.setNextComponent(nL);

        navigationManager.addNavigationListener(new NavigationListener() {
            @Override
            public void navigate(NavigationEvent event) {
                if (navigationManager.getCurrentComponent() == nL) {
                    if (event.getDirection() == Direction.FORWARD) {
                        navigationManager.setNextComponent(yetAnother);
                    }
                }
            }
        });

        addComponent(navigationManager);

    }

}
