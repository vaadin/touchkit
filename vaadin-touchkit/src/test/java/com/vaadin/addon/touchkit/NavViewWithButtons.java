package com.vaadin.addon.touchkit;

import com.vaadin.addon.touchkit.ui.ComponentGroup;
import com.vaadin.addon.touchkit.ui.ComponentGroup.Orientation;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;

class NavViewWithButtons extends NavigationView implements ClickListener {

    NavViewWithButtons() {
        setCaption("Test buttons");

        ComponentGroup group = new ComponentGroup(Orientation.HORIZONTAL);

        Button button = new Button("Up", this);
        button.addStyleName("icon-arrow-up");

        group.addComponent(button);

        Button button2 = new Button("Down", this);
        button2.addStyleName("icon-arrow-down");
        group.addComponent(button2);

        setLeftComponent(group);

        CssLayout right = new CssLayout();

        ComponentGroup group2 = new ComponentGroup(Orientation.HORIZONTAL);

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

        button2 = new Button(null, this);
        button2.setIcon(new ClassResource("mail.png", NavPanelTestWithViews.app));
        toolbar.addComponent(button2);

        button2 = new Button(null, this);
        button2.setIcon(new ClassResource("mail.png", NavPanelTestWithViews.app));
        toolbar.addComponent(button2);
        button2 = new Button(null, this);
        button2.setIcon(new ClassResource("mail.png", NavPanelTestWithViews.app));
        toolbar.addComponent(button2);
        button2 = new Button(null, this);
        button2.setIcon(new ClassResource("mail.png", NavPanelTestWithViews.app));
        toolbar.addComponent(button2);
        button2 = new Button(null, this);
        button2.setIcon(new ClassResource("mail.png", NavPanelTestWithViews.app));
        toolbar.addComponent(button2);

        setToolbar(toolbar);

    }

    public void buttonClick(ClickEvent event) {
        getWindow().showNotification("Clicked");
    }
}