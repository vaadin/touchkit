package com.vaadin.addon.touchkit.itest;

import java.util.Iterator;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.HorizontalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class VerticalComponentGroupTest extends AbstractTouchKitIntegrationTest {

    public VerticalComponentGroupTest() {
        setDescription("This is VerticalComponentGroup test");
        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup(
                "Vertical component group");
//        verticalComponentGroup.setMargin(true);
        verticalComponentGroup.addComponent(new Button("Button"));
        verticalComponentGroup.addComponent(new TextField("TextField"));

        NavigationButton one = new NavigationButton("Navigation button");
        NavigationButton too = new NavigationButton(
                "Navigation button with icon");
        too.setIcon(new ThemeResource("../runo/icons/32/ok.png"));
        verticalComponentGroup.addComponent(one);
        verticalComponentGroup.addComponent(too);
        verticalComponentGroup.addComponent(new Label(
                "FIXME: Label, between buttons bugs"));
        verticalComponentGroup.addComponent(new Button("Button too"));

        addComponent(verticalComponentGroup);

        verticalComponentGroup = new VerticalComponentGroup();
//        verticalComponentGroup.setMargin(false);
        verticalComponentGroup.setCaption("Horizontal in vertical");
        HorizontalComponentGroup horizontalGroup = getHorizontalGroup();
        horizontalGroup.setCaption("Caption");
        verticalComponentGroup.addComponent(horizontalGroup);

        horizontalGroup = getHorizontalGroup();
        horizontalGroup.addComponent(new Button("Third"));
        horizontalGroup.setWidth("300px");
        Iterator<Component> componentIterator = horizontalGroup
                .getComponentIterator();
        while (componentIterator.hasNext()) {
            Component next = componentIterator.next();
            next.setWidth(100 / horizontalGroup.getComponentCount() + "%");
        }
        verticalComponentGroup.addComponent(horizontalGroup);

        horizontalGroup = getHorizontalGroup();
        Iterator<Component> it = horizontalGroup.getComponentIterator();
        it.next().setCaption("Only one here");
        horizontalGroup.removeComponent(it.next());
        verticalComponentGroup.addComponent(horizontalGroup);

        addComponent(verticalComponentGroup);
    }

    private HorizontalComponentGroup getHorizontalGroup() {
        HorizontalComponentGroup horizontalComponentGroup = new HorizontalComponentGroup();
        horizontalComponentGroup.addComponent(new Button("First"));
        horizontalComponentGroup.addComponent(new Button("Another"));

        return horizontalComponentGroup;
    }
}
