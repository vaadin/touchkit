package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.gwt.client.theme.StyleNames;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.Label;

public class NavigationButtonTest extends AbstractTouchKitIntegrationTest {

    public static class FirstView extends NavigationView {
        public FirstView() {
            super();
            setCaption("First");
            CssLayout layout = new CssLayout();
            addNavigationButtons(layout);

            VerticalComponentGroup group = new VerticalComponentGroup();
            addNavigationButtons(group);
            layout.addComponent(group);

            setContent(layout);
        }

        private void addNavigationButtons(ComponentContainer layout) {
            NavigationButton button = new NavigationButton(
                    "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    new SecondView());
            layout.addComponent(button);

            button = new NavigationButton("NavigationButton with description",
                    new GenericView("Fourth"));
            button.setDescription("4");
            layout.addComponent(button);

            button = new NavigationButton(
                    "NavigationButton with description, styled",
                    new GenericView("Fifth"));
            button.setDescription("5");
            button.addStyleName(StyleNames.NAVIGATION_BUTTON_DESC_PILL);
            layout.addComponent(button);
        }
    }

    public static class SecondView extends NavigationView {
        public SecondView() {
            super();
            setCaption("Second");
            NavigationButton button = new NavigationButton(
                    "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    new GenericView("Third"));
            button.setDescription("3");
            setContent(button);
        }
    }

    public static class GenericView extends NavigationView {
        public GenericView(String viewName) {
            super();
            setCaption(viewName);
            setContent(new Label("This is the '" + viewName + "' view"));
        }
    }

    public NavigationButtonTest() {
        setDescription("Testing the features of the NavigationButton");
        NavigationManager navMan = new NavigationManager(new FirstView());
        addComponent(navMan);
    }

}
