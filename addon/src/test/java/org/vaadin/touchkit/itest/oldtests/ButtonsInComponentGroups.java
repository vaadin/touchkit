package org.vaadin.touchkit.itest.oldtests;

import java.util.Iterator;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.gwt.client.theme.StyleNames;
import org.vaadin.touchkit.ui.HorizontalButtonGroup;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextField;

public class ButtonsInComponentGroups extends AbstractTouchKitIntegrationTest {

    public ButtonsInComponentGroups() {
        NavigationView navigationView = new NavigationView();
        navigationView.setCaption("Buttons in various places");

        CssLayout l = new CssLayout();

        Button button = new Button("Button not in a component group");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Notification.show("Clicked");
            }
        });
        l.addComponent(button);
        
        button = new Button("Primary button");
        button.setStyleName(StyleNames.BUTTON_BLUE);
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Notification.show("Clicked");
            }
        });
        l.addComponent(button);

        button = new Button("Danger button");
        button.setStyleName(StyleNames.BUTTON_RED);
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Notification.show("Clicked");
            }
        });
        l.addComponent(button);


        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup(
                "Vertical component group");
        verticalComponentGroup.setWidth("100%");

        button = new Button("Button");
        button.setWidth("100%");
        verticalComponentGroup.addComponent(button);

        verticalComponentGroup.addComponent(new TextField("TextField"));

        button = new Button("Button too");
        button.setWidth("100%");
        verticalComponentGroup.addComponent(button);

        verticalComponentGroup.addComponent(new Label(
                "FIXME: Label, between buttons bugs"));

        button = new Button("Button too");
        button.setWidth("100%");
        verticalComponentGroup.addComponent(button);

        l.addComponent(verticalComponentGroup);

        verticalComponentGroup = new VerticalComponentGroup();
        verticalComponentGroup.setWidth("100%");
        verticalComponentGroup.setCaption("Horizontal in vertical");
        HorizontalButtonGroup horizontalGroup = getHorizontalGroup();
        horizontalGroup.setCaption("Caption");
        verticalComponentGroup.addComponent(horizontalGroup);

        horizontalGroup = getHorizontalGroup();
        horizontalGroup.addComponent(new Button("Third"));
        horizontalGroup.setWidth("300px");
        Iterator<Component> componentIterator = horizontalGroup
                .getComponentIterator();
        while (componentIterator.hasNext()) {
            Component next = componentIterator.next();
            next.setWidth("" + 100.0 / (horizontalGroup.getComponentCount())
                    + "%");
        }
        verticalComponentGroup.addComponent(horizontalGroup);

        horizontalGroup = getHorizontalGroup();
        Iterator<Component> it = horizontalGroup.getComponentIterator();
        it.next().setCaption("Only one here");
        horizontalGroup.removeComponent(it.next());
        verticalComponentGroup.addComponent(horizontalGroup);

        l.addComponent(verticalComponentGroup);

        l.addComponent(getHorizontalGroup());

        navigationView.setContent(l);

        HorizontalButtonGroup horizontalComponentGroup = new HorizontalButtonGroup();
        horizontalComponentGroup.addComponent(new Button("Up"));
        horizontalComponentGroup.addComponent(new Button("Down"));
        navigationView.setRightComponent(horizontalComponentGroup);
        horizontalComponentGroup.setCaption("Horizontal straight to layout");

        navigationView.setLeftComponent(new Button("Left"));

        addComponent(navigationView);
        TestUtils.makeSmallTabletSize(navigationView);

    }

    private HorizontalButtonGroup getHorizontalGroup() {
        HorizontalButtonGroup horizontalComponentGroup = new HorizontalButtonGroup();
        horizontalComponentGroup.addComponent(new Button("First"));
        horizontalComponentGroup.addComponent(new Button("Another"));

        return horizontalComponentGroup;
    }

}
