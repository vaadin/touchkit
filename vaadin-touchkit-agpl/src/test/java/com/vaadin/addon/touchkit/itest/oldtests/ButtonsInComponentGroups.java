package com.vaadin.addon.touchkit.itest.oldtests;

import java.util.Iterator;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.HorizontalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class ButtonsInComponentGroups extends AbstractTouchKitIntegrationTest {

    public ButtonsInComponentGroups() {
        NavigationView navigationView = new NavigationView();
        navigationView.setCaption("Buttons in various places");

        CssLayout l = new CssLayout();
        l.setMargin(true);
        
        l.addComponent(new Button("Button not in a component group"));

        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup(
                "Vertical component group");
        verticalComponentGroup.setMargin(false);
        verticalComponentGroup.addComponent(new Button("Button"));
        verticalComponentGroup.addComponent(new TextField("TextField"));
        verticalComponentGroup.addComponent(new Button("Button too"));
        verticalComponentGroup.addComponent(new Label("FIXME: Label, between buttons bugs"));
        verticalComponentGroup.addComponent(new Button("Button too"));

        l.addComponent(verticalComponentGroup);
        
        verticalComponentGroup = new VerticalComponentGroup();
        verticalComponentGroup.setMargin(false);
        verticalComponentGroup.setCaption("Horizontal in vertical");
        HorizontalComponentGroup horizontalGroup = getHorizontalGroup();
        horizontalGroup.setCaption("Caption");
        verticalComponentGroup.addComponent(horizontalGroup);
        
        
        horizontalGroup = getHorizontalGroup();
        horizontalGroup.addComponent(new Button("Third"));
        horizontalGroup.setWidth("300px");
        Iterator<Component> componentIterator = horizontalGroup.getComponentIterator();
        while(componentIterator.hasNext()) {
            Component next = componentIterator.next();
            next.setWidth(100/horizontalGroup.getComponentCount() + "%");
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
        
        HorizontalComponentGroup horizontalComponentGroup = new HorizontalComponentGroup();
        horizontalComponentGroup.addComponent(new Button("Up"));
        horizontalComponentGroup.addComponent(new Button("Down"));
        navigationView.setRightComponent(horizontalComponentGroup);
        horizontalComponentGroup.setCaption("Horizontal straight to layout");
        
        navigationView.setLeftComponent(new Button("Left"));
        
        addComponent(navigationView);
        TestUtils.makeSmallTabletSize(navigationView);

    }

    private HorizontalComponentGroup getHorizontalGroup() {
        HorizontalComponentGroup horizontalComponentGroup = new HorizontalComponentGroup();
        horizontalComponentGroup.addComponent(new Button("First"));
        horizontalComponentGroup.addComponent(new Button("Another"));
        
        return horizontalComponentGroup;
    }

}
