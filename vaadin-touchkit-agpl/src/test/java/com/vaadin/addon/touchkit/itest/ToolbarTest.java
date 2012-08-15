package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;

public class ToolbarTest extends AbstractTouchKitIntegrationTest {
    public ToolbarTest() {
        setDescription("This is Toolbar test");
        Toolbar tbar = new Toolbar();

        for (int loop = 0; loop < 10; loop++) {
            Button button = new Button();
            button.setIcon(new ThemeResource("../runo/icons/32/folder.png"));
            tbar.addComponent(button);
        }

        addComponent(tbar);
    }
}
