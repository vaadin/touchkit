package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.Tab;

public class TabBarTest extends AbstractTouchKitIntegrationTest {
    public TabBarTest() {
        setDescription("This is TabBar test");
        TabBarView bar = new TabBarView();

        for (int loop = 0; loop < 5; loop++) {
            String content = String.format("Tab%d", loop);
            Label label = new Label(content);
            // label.setCaption(content);
            Tab tab = bar.addTab(label);

            if (loop % 3 == 0) {
                tab.setIcon(new ThemeResource("../runo/icons/32/folder.png"));
                tab.setCaption(content);
            } else if (loop % 3 == 1) {
                tab.setCaption(content);
            } else {
                tab.setIcon(new ThemeResource("../runo/icons/32/folder.png"));
            }
        }

        addComponent(bar);
    }
}