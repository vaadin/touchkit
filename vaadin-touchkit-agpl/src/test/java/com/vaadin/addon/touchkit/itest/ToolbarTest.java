package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.extensions.FontAwesomeIcon;
import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;

public class ToolbarTest extends AbstractTouchKitIntegrationTest {
    public ToolbarTest() {
        setDescription("This is Toolbar test");
        Toolbar tbar = new Toolbar();
        final Label label = new Label();
        label.setValue("Clicked: none");

        for (int loop = 0; loop < 10; loop++) {
            Button button = new Button();
            if(loop%2==0) {
            	FontAwesomeIcon.HOME.add(button);
            } else {
                button.setIcon(new ThemeResource("../runo/icons/64/folder.png"));
            }
            if(loop%3==0) {
            	button.setCaption("Diipa");
            }

            final int identifier = loop;
            button.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    label.setValue("Clicked: " + identifier);
                }
            });
            tbar.addComponent(button);
        }

        addComponent(tbar);
        addComponent(label);
    }
}
