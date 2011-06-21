package com.vaadin.addons.touchkit;

import com.vaadin.addons.touchkit.ui.NavigationView;
import com.vaadin.addons.touchkit.ui.Toolbar;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;

class NavViewWithButtons extends NavigationView implements ClickListener {

    NavViewWithButtons() {
        setCaption("Test buttons");

        CssLayout cssLayout = new CssLayout();
        cssLayout.setStyleName("buttongroup");

        Button button = new Button(null, this);
        button.setIcon(new ClassResource("mail.png", NavPanelTestWithViews.app));

        cssLayout.addComponent(button);

        Button button2 = new Button(null, this);
        button2.setIcon(new ClassResource("mail.png", NavPanelTestWithViews.app));
        cssLayout.addComponent(button2);

        setLeftComponent(cssLayout);

        CssLayout cssLayout2 = new CssLayout();

        button2 = new Button(null, this);
        button2.setIcon(new ClassResource("mail.png", NavPanelTestWithViews.app));
        button2.setStyleName("no-decoration");

        cssLayout2.addComponent(button2);

        button2 = new Button(null, this);
        button2.setIcon(new ClassResource("mail.png", NavPanelTestWithViews.app));

        cssLayout2.addComponent(button2);

        setRightComponent(cssLayout2);

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