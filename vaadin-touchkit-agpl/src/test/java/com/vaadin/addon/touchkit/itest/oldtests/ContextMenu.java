package com.vaadin.addon.touchkit.itest.oldtests;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.ui.Table;

public class ContextMenu extends AbstractTouchKitIntegrationTest {

    public ContextMenu() {

        NavigationManager navigationManager = new NavigationManager();
        addComponent(navigationManager);

        NavigationView iv = new NavigationView();
        iv.setCaption("Table with context menu");

        final Table table = new Table();
        table.addContainerProperty("foo", String.class, "foo");
        table.addContainerProperty("bar", String.class, "bar");
        table.addContainerProperty("car", String.class, "car");
        table.setRowHeaderMode(Table.ROW_HEADER_MODE_INDEX);

        table.addActionHandler(new Handler() {

            private Action[] actions = new Action[] { new Action("Copy"),
                    new Action("Paste") };

            public void handleAction(Action action, Object sender, Object target) {
            	getRoot().showNotification("Just test");
            }

            public Action[] getActions(Object target, Object sender) {
                return actions;
            }
        });

        table.addItem();
        table.addItem();
        table.addItem();

        table.setSizeFull();

        iv.setContent(table);

        navigationManager.setCurrentComponent(iv);

    }

}
