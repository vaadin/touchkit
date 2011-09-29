package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.OptionGroup;

public class Selects extends TouchKitWindow {

    public Selects() {
        NavigationView navigationView = new NavigationView();
        navigationView.setCaption("Various selection possibilities");

        CssLayout l = new CssLayout();

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addItem("One");
        optionGroup.addItem("Two");
        optionGroup.addItem("Three");
        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup(
                "OptionGroup");
        verticalComponentGroup.addComponent(optionGroup);

        l.addComponent(verticalComponentGroup);
        
        optionGroup = new OptionGroup();
        optionGroup.addItem("One");
        optionGroup.addItem("Two");
        optionGroup.addItem("Three");
        optionGroup.setMultiSelect(true);
        
        verticalComponentGroup = new VerticalComponentGroup(
                "OptionGroup (multiselect)");
        verticalComponentGroup.addComponent(optionGroup);
        verticalComponentGroup.addComponent(optionGroup);

        l.addComponent(verticalComponentGroup);
        
        

        navigationView.setContent(l);
        setContent(navigationView);
        
        TestUtils.makeSmallTabletSize(navigationView);

    }

}
