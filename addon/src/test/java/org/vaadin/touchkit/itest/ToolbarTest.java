package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.Toolbar;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;

public class ToolbarTest extends AbstractTouchKitIntegrationTest {
    public ToolbarTest() {
        setDescription("This is Toolbar test");
        Toolbar tbar = new Toolbar();
        final Label label = new Label();
        label.setValue("Clicked: none");

        for (int loop = 0; loop < 10; loop++) {
            Button button = new Button();
            if (loop % 2 == 0) {
                button.setIcon(FontAwesome.HOME);
            } else {
                button.setIcon(new ThemeResource("../runo/icons/64/folder.png"));
            }
            if (loop % 3 == 0) {
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
