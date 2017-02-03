package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;

public class NavigationViewWithSaveCancelTest extends
        AbstractTouchKitIntegrationTest {

    public NavigationViewWithSaveCancelTest() {
        setDescription("NavigationView test with custom buttons to the left and right");
        addComponent(createView());
    }

    NavigationView createView() {
        final CssLayout layout = new CssLayout();
        int max = (int) (Math.random() * 100);
        for (int i = 0; i < max; i++) {
            Label l = new Label("Foo " + i);
            layout.addComponent(l);
        }

        NavigationView navView = new NavigationView();
        navView.setRightComponent(new Button("Save",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        Notification.show("Save! :)");
                    }
                }));
        navView.setCaption("Save or Cancel?");
        navView.setContent(layout);
        navView.setLeftComponent(new Button("Cancel",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        Notification.show("Cancel :(");
                    }
                }));
        return navView;
    }

}
