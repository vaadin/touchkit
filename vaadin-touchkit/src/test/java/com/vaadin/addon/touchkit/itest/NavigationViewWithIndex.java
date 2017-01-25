package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.extensions.FloatingIndex;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.Label;

public class NavigationViewWithIndex extends
        AbstractTouchKitIntegrationTest {


    public NavigationViewWithIndex() {
        setDescription("NavigationView with long list and index.");
        addComponent(createView());
    }

    NavigationView createView() {
        final CssLayout layout = new CssLayout();
        
        FloatingIndex floatingIndex = new FloatingIndex();
        
        NavigationView navView = new NavigationView();
        navView.setCaption("Index for quick scrolling");
        navView.setContent(layout);
        
        Person[] testPersons = Person.getTestPersons();

        VerticalComponentGroup alphaGroup = null;

        for (Person person : testPersons) {
            String firstLetter = person.getLastName().substring(0, 1);
            if (alphaGroup == null || !alphaGroup.getCaption().equals(firstLetter)) {
                alphaGroup = new VerticalComponentGroup(firstLetter);
                layout.addComponent(alphaGroup);
                floatingIndex.map(firstLetter, alphaGroup);
            }
            alphaGroup.addComponent(new Label(person.toString()));
        }
        
        floatingIndex.addTo(navView);
        
        return navView;
    }

}
