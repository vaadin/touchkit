package com.vaadin.addon.touchkit.itest;

import java.util.Random;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationListener;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.RowHeaderMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

public class TableAndNavigationManager extends AbstractTouchKitIntegrationTest {
    
    Random r = new Random(0);

    public TableAndNavigationManager() {
        setDescription("NavigationView and -Bar test");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.addComponent(makeTable());
        horizontalLayout.addComponent(makeNavigationManager());
        addComponent(horizontalLayout);
    }

    private Component makeTable() {
        Table table = new Table();
        table.setSizeFull();
        table.addContainerProperty("Dii", String.class, "pa");
        table.addContainerProperty("Dai", String.class, "ba");
        table.setRowHeaderMode(RowHeaderMode.INDEX);
        for(int i = 0;  i < 100; i++) {
            table.addItem();
        }
        return table;
    }

    NavigationManager makeNavigationManager() {
        final NavigationManager navman = new NavigationManager();

        final NavigationView one = createView("One", "one", true);
        final NavigationView two = createView("Two", "two", true);
        final NavigationView three = createView("Three", "three", true);
        final NavigationView four = createView("Four", "four", false);

        navman.setCurrentComponent(one);
        navman.setNextComponent(two);

        addNextButton(navman, one, two);
        addNextButton(navman, two, three);
        addNextButton(navman, three, four);

        navman.addNavigationListener(new NavigationListener() {
            @Override
            public void navigate(NavigationEvent event) {
                if (navman.getCurrentComponent() == one) {
                    navman.setNextComponent(two);
                } else if (navman.getCurrentComponent() == two) {
                    navman.setNextComponent(three);
                } else if (navman.getCurrentComponent() == three) {
                    navman.setNextComponent(four);
                }
            }
        });
        return navman;
    }

    private static void addNextButton(final NavigationManager navman,
            final NavigationView view, final NavigationView next) {
        ((Button) view.getRightComponent())
                .addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        navman.navigateTo(next);
                    }
                });
    }

    NavigationView createView(String caption, String debugId,
            boolean hasNext) {
        final CssLayout layout = new CssLayout();
        layout.setId(debugId);
        int max = (int) (r.nextInt(100));
        for (int i = 0; i < max; i++) {
            Label l = new Label("Foo " + i);
            layout.addComponent(l);
        }

        NavigationView navView = new NavigationView();
        if (hasNext) {
            navView.setRightComponent(new Button("Next"));
        }
        navView.setCaption(caption);
        navView.setContent(layout);
        navView.getLeftComponent().setCaption("Back");
        return navView;
    }
}
