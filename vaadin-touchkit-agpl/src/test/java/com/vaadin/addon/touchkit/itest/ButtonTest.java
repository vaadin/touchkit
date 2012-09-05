package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.BaseTheme;

public class ButtonTest extends AbstractTouchKitIntegrationTest {
    public ButtonTest() {
        setDescription("This is a button test");
        Button button = new Button();
        button.setCaption("button caption");
        
        Button link = new Button();
        link.setCaption("link caption");
        link.setStyleName(BaseTheme.BUTTON_LINK);
        
        addComponent(button);
        addComponent(link);
    }

}
