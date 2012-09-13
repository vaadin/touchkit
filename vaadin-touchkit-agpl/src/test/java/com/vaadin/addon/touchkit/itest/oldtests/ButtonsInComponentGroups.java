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
//        l.setMargin(true);
        
        Button button = new Button("Button not in a component group");
        button.setWidth("100%");
        l.addComponent(button);

        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup(
                "Vertical component group");
//        verticalComponentGroup.setMargin(false);
        
        button = new Button("Button");
        button.setWidth("100%");
        verticalComponentGroup.addComponent(button);
        
        verticalComponentGroup.addComponent(new TextField("TextField"));
        
        button = new Button("Button too");
        button.setWidth("100%");
        verticalComponentGroup.addComponent(button);
        
        verticalComponentGroup.addComponent(new Label("FIXME: Label, between buttons bugs"));
        
        button = new Button("Button too");
        button.setWidth("100%");
        verticalComponentGroup.addComponent(button);

        l.addComponent(verticalComponentGroup);
        
        verticalComponentGroup = new VerticalComponentGroup();
//        verticalComponentGroup.setMargin(false);
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
            next.setWidth(""
                + 100.0/((double)horizontalGroup.getComponentCount()) + "%");
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
