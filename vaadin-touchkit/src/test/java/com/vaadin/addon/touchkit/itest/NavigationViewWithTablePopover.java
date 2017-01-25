package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

public class NavigationViewWithTablePopover extends
        AbstractTouchKitIntegrationTest {

    public NavigationViewWithTablePopover() {
        setDescription("Popover positioning test");

        NavigationView view = new NavigationView("Click xxx");
        view.setContent(new Label("Content"));

        final Button button = new Button("XXX");
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Table table = new Table();
                for (int i = 0; i < 10; i++) {
                    table.addContainerProperty("property " + i, String.class,
                            "HOLA!");
                }
                for (int i = 0; i < 200; i++) {
                    table.addItem();
                }
                table.setSizeFull();

                CssLayout content = new CssLayout(table);
                content.setSizeFull();
                Popover popover = new Popover(content);
                popover.setWidth("50%");
                popover.setHeight("60%");

                popover.showRelativeTo(button);

            }
        });
        view.setRightComponent(button);

        addComponent(view);

    }

}
