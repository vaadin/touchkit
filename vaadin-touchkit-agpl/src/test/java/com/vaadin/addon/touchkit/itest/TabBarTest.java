package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.Tab;

public class TabBarTest extends AbstractTouchKitIntegrationTest {
    public TabBarTest() {
        setDescription("This is TabBar test");
        TabBarView bar = new TabBarView();

        for (int loop = 0; loop < 5; loop++) {
            String content = String.format("Tab%d", loop);
            Label label = new Label(content);
            label.setCaption(content);
            Tab tab = bar.addTab(label);
            tab.setCaption(content);
        }

        addComponent(bar);
    }
}