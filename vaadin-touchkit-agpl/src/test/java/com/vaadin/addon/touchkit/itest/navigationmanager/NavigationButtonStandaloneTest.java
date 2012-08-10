package com.vaadin.addon.touchkit.itest.navigationmanager;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.ui.Notification;

public class NavigationButtonStandaloneTest extends AbstractTouchKitIntegrationTest {
    
    @Override
    public String getDescription() {
        return "NavigationManager without navigation button, just server side calls.";
    }
    
    public NavigationButtonStandaloneTest() {
      
        NavigationButton navigationButton = new NavigationButton("Buttoncaption");
        navigationButton.addClickListener(new NavigationButtonClickListener() {
            
            @Override
            public void buttonClick(
                    com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent event) {
                Notification.show("clicked!");
            }
        });
        navigationButton.setDescription("Dippadai");
        addComponent(navigationButton);
        
    }

}
