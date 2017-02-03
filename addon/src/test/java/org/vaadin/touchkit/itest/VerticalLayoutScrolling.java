package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.Toolbar;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.ui.Button;
import com.vaadin.v7.ui.VerticalLayout;

public class VerticalLayoutScrolling extends AbstractTouchKitIntegrationTest {

    public VerticalLayoutScrolling() {
        setDescription("Displays a VerticalLayout inside a NavigationView to test scrolling and issue #17251");

        final NavigationManager layout = new NavigationManager();
        addComponent(layout);
        layout.setSizeFull();

        layout.setCurrentComponent(new NavigationTestView());
    }

    public static class NavigationTestView extends NavigationView {

        public NavigationTestView() {

            final VerticalLayout vl = new VerticalLayout();
            vl.setMargin(true);
            setContent(vl);

            final VerticalComponentGroup g = new VerticalComponentGroup();
            vl.addComponent(g);

            for (int i = 0; i < 100; i++) {
                g.addComponent(new NavigationButton("test"));
            }

            getNavigationBar().setCaption("List");
            setToolbar(new Toolbar() {
                {
                    addComponent(new Button("dadasd"));
                }
            });
        }
    }
}
