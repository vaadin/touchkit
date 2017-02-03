package org.vaadin.touchkit.itest.oldtests;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.HorizontalButtonGroup;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.Toolbar;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Notification;


public class NavViewWithButtons extends AbstractTouchKitIntegrationTest {
    public NavViewWithButtons() {
        addComponent(new NavViewWithButtonsT());
    }
}

class NavViewWithButtonsT extends NavigationView implements ClickListener {

    NavViewWithButtonsT() {
        setCaption("Test buttons");

        HorizontalButtonGroup group = new HorizontalButtonGroup();

        Button button = new Button("Up", this);
        button.addStyleName("icon-arrow-up");

        group.addComponent(button);

        Button button2 = new Button("Down", this);
        button2.addStyleName("icon-arrow-down");
        group.addComponent(button2);

        setLeftComponent(group);

        HorizontalLayout right = new HorizontalLayout();

        HorizontalButtonGroup group2 = new HorizontalButtonGroup();

        button2 = new Button("No deco", this);
        button2.setStyleName("no-decoration");

        right.addComponent(button2);

        button2 = new Button("Left", this);
        button2.addStyleName("icon-arrow-left");

        group2.addComponent(button2);
        button2 = new Button("Right", this);
        button2.addStyleName("icon-arrow-right");

        group2.addComponent(button2);
        right.addComponent(group2);

        setRightComponent(right);

        Toolbar toolbar = new Toolbar();

        button2 = new Button();
        button2.setIcon(new ThemeResource("../runo/icons/64/document-web.png"));
        toolbar.addComponent(button2);

        button2 = new Button();
        button2.setIcon(new ThemeResource(
                "../runo/icons/64/document-delete.png"));
        toolbar.addComponent(button2);
        button2 = new Button();
        button2.setIcon(new ThemeResource("../runo/icons/64/document-edit.png"));
        toolbar.addComponent(button2);
        button2 = new Button();
        button2.setIcon(new ThemeResource("../runo/icons/64/document-doc.png"));
        toolbar.addComponent(button2);
        button2 = new Button();
        button2.setIcon(new ThemeResource("../runo/icons/64/document-ppt.png"));
        toolbar.addComponent(button2);

        setToolbar(toolbar);

    }

    public void buttonClick(ClickEvent event) {
        Notification.show("clicked");
    }
}