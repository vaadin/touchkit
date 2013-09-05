package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;

public class IconButtonsTest extends AbstractTouchKitIntegrationTest implements
        ClickListener {
    private int i;

    public IconButtonsTest() {

        setDescription("This is a button test");
        
        VerticalComponentGroup componentGroup = new VerticalComponentGroup();
        
        for(TouchKitIcon i : TouchKitIcon.values()) {
            NavigationButton button = new NavigationButton(i.name());
            i.add(button);
            componentGroup.addComponent(button);
        }
        addComponent(componentGroup);
        
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Notification.show("Clicked " + ++i);
    }

}
