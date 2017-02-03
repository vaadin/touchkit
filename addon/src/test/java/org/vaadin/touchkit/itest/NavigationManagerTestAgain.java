package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.Popover;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class NavigationManagerTestAgain extends AbstractTouchKitIntegrationTest {

    public NavigationManagerTestAgain() {
        setDescription("Forward and backward navigation with normal buttons should work.");
        final NavigationManager manager = new NavigationManager();

        final NavigationView first = new NavigationView();
        final NavigationView second = new NavigationView();
        
        second.setContent(new Label("Content"));
        second.setLeftComponent(new Button("Finnish", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                final Popover popover = new Popover();
                VerticalLayout l = new VerticalLayout();
                l.setSpacing(true);
                l.addComponent(new Button("Save and go back", new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        manager.navigateBack();
                        popover.close();
                    }
                }));
                l.addComponent(new Button("Cancel", new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        popover.close();
                    }
                }));
                popover.setContent(l);
                popover.showRelativeTo(event.getButton());
            }
        }));

        NavigationButton c = new NavigationButton("Go to second level",second);
        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup();
        verticalComponentGroup.addComponent(c);
        first.setContent(verticalComponentGroup);

        manager.navigateTo(first);
        
        addComponent(manager);
    }
}
