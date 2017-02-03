package com.vaadin.addon.touchkit.itest;

import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.TabBarView;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.itest.oldtests.TestUtils;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.TabSheet.Tab;

public class TabBarInNavigationViewTest extends AbstractTouchKitIntegrationTest {
    public TabBarInNavigationViewTest() {

        TestUtils.injectCss(".green {background-color: green;}");
        setDescription("This is TabBar test");

        final TabBarView bar = new TabBarView();

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

            if (loop == 0) {
                label.setSizeFull();
                label.addStyleName("green");
                label.setValue(label.getValue()
                        + ", green color should fill the whole content area");
            } else if (loop == 1) {
                StringBuilder sb = new StringBuilder();
                sb.append(label.getValue());
                sb.append("Content on this tab should be scrollable");
                for (int i = 0; i < 200; i++) {
                    sb.append("Row " + i + "</br>");
                }
                label.setContentMode(ContentMode.HTML);
                label.setValue(sb.toString());
            }
        }
        
        NavigationView navigationView = new NavigationView();
        navigationView.setCaption("NavView contains TabBarView");
        navigationView.setContent(bar);
        makeSmallTabletSize(navigationView);
        addComponent(navigationView);
        
    }

}