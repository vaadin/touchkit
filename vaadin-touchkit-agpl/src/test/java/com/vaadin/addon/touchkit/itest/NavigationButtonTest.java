package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class NavigationButtonTest extends AbstractTouchKitIntegrationTest {

    public static class FirstView extends NavigationView {
        public FirstView() {
            super();
            setCaption("First");
            CssLayout layout = new CssLayout();
            NavigationButton button = new NavigationButton(
                    "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    new SecondView());
            layout.addComponent(button);

            button = new NavigationButton("NavigationButton with description",
                    new FourthView());
            button.setDescription("4");
            layout.addComponent(button);

            setContent(layout);
        }
    }

    public static class SecondView extends NavigationView {
        public SecondView() {
            super();
            setCaption("Second");
            NavigationButton button = new NavigationButton(
                    "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    new ThirdView());
            button.setDescription("3");
            setContent(button);
        }
    }

    public static class ThirdView extends NavigationView {
        public ThirdView() {
            super();
            setCaption("Third");
            setContent(new Label("This is the third view"));
        }
    }

    public static class FourthView extends NavigationView {
        public FourthView() {
            super();
            setCaption("Fourth");
            setContent(new Label("This is the fourth view"));
        }
    }

    public NavigationButtonTest() {
        setDescription("Testing the features of the NavigationButton");
        NavigationManager navMan = new NavigationManager(new FirstView());
        addComponent(navMan);
    }

}
