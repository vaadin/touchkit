package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class NavigationViewBarTest extends AbstractTouchKitIntegrationTest {

    public NavigationViewBarTest() {
        setDescription("NavigationView and -Bar test");
        addComponent(makeNavigationManager());
    }

    static NavigationManager makeNavigationManager() {
        final NavigationManager navman = new NavigationManager();

        final NavigationView one = createView("One", "one", true);
        final NavigationView two = createView("Two", "two", true);
        final NavigationView three = createView("Three", "three", true);
        final NavigationView four = createView("Four", "four", false);

        navman.setCurrentComponent(one);
        navman.setNextComponent(two);

        addNextButton(navman, one, two);
        addNextButton(navman, two, three);
        addNextButton(navman, three, four);

        navman.addListener(new NavigationListener() {
            @Override
            public void navigate(NavigationEvent event) {
                if (navman.getCurrentComponent() == one) {
                    navman.setNextComponent(two);
                } else if (navman.getCurrentComponent() == two) {
                    navman.setNextComponent(three);
                } else if (navman.getCurrentComponent() == three) {
                    navman.setNextComponent(four);
                }
            }
        });
        return navman;
    }

    private static void addNextButton(final NavigationManager navman,
            final NavigationView view, final NavigationView next) {
        ((Button) view.getRightComponent())
                .addListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        navman.navigateTo(next);
                    }
                });
    }

    static NavigationView createView(String caption, String debugId,
            boolean hasNext) {
        final CssLayout layout = new CssLayout();
        layout.setDebugId(debugId);
        int max = (int) (Math.random() * 100);
        for (int i = 0; i < max; i++) {
            Label l = new Label("Foo " + i);
            layout.addComponent(l);
        }

        NavigationView navView = new NavigationView();
        if (hasNext) {
            navView.setRightComponent(new Button("Next"));
        }
        navView.setCaption(caption);
        navView.setContent(layout);
        navView.getLeftComponent().setCaption("Back");
        return navView;
    }
}
