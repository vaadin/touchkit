package com.vaadin.addon.touchkit.itest;

import org.junit.Ignore;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.RowHeaderMode;

@SuppressWarnings("serial")
@Ignore
public class SwipeViewWithTablesTest extends AbstractTouchKitIntegrationTest {

    public SwipeViewWithTablesTest() {

        NavigationManager navigationManager = new NavigationManager();
        Table table1 = createTable();
        Table table2 = createTable();
        
        SwipeView newCurrentComponent = new SwipeView(table1);
        newCurrentComponent.setSizeFull();
        navigationManager.setCurrentComponent(newCurrentComponent);
        SwipeView nextComponent = new SwipeView(table2);
        nextComponent.setSizeFull();
        navigationManager.setNextComponent(nextComponent);
        addComponent(navigationManager);
    }

    private Table createTable() {
        Table table = new Table();
        table.addContainerProperty("Foo", String.class, "bar");
        table.setRowHeaderMode(RowHeaderMode.INDEX);
        for(int i = 0; i < 100; i++) {
            table.addItem();
        }
        table.setSizeFull();
        table.setHeight(50, Unit.PERCENTAGE);
        return table;
    }

}
