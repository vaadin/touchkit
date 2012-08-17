package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class PopoverTest extends AbstractTouchKitIntegrationTest {

    public PopoverTest() {
        setDescription("Popover test");

        Button button = new Button("Contains buttons");
        button.addListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                Popover popover = new Popover();
                popover.setWidth("360px");
                Button b = new Button("Save Draft");
                b.addStyleName("white");
                b.setWidth("100%");
                popover.addComponent(b);
                b = new Button("Delete Draft");
                b.addStyleName("red");
                b.setWidth("100%");
                popover.addComponent(b);
                b = new Button("Cancel");
                b.setWidth("100%");
                popover.addComponent(b);

                popover.showRelativeTo(event.getButton());
            }
        });
        addComponent(button);

        button = new Button("Contains a NavigationView");
        button.addListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                Popover popover = new Popover();
                popover.setWidth("360px");
                popover.setHeight("400px");

                popover.setContent(NavigationViewBarTest.createView("foo",
                        "foo", false));

                popover.showRelativeTo(event.getButton());
            }
        });
        addComponent(button);

        button = new Button("Contains a NavigationManager");
        button.addListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                Popover popover = new Popover();
                popover.setWidth("360px");
                popover.setHeight("400px");

                popover.setContent(NavigationViewBarTest
                        .makeNavigationManager());

                popover.showRelativeTo(event.getButton());
            }
        });
        addComponent(button);
    }
}
