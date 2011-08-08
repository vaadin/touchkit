package com.vaadin.addon.touchkit;

import com.vaadin.addon.touchkit.ui.AbstractComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.TabBar;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.Tab;

public class TabsheetTest extends TabBar implements ClickListener {

    public TabsheetTest() {

        CssLayout tab1 = new CssLayout() {
            @Override
            protected String getCss(Component c) {
                return "background: yellow;";
            }
        };
        tab1.setSizeFull();
        tab1.setCaption("Foo1");
        Label label = new Label("Some content for tabsheet");
        label.setSizeFull();
        tab1.addComponent(label);

        CssLayout tab2 = new CssLayout();
        tab2.setSizeFull();
        tab2.setCaption("Bar1");
        tab2.addComponent(new Label("Some content for tabsheet"));

        CssLayout tab3 = new CssLayout();
        tab3.setSizeFull();
        tab3.setCaption("Car1");
        tab3.addComponent(new Label("Some content for tabsheet"));

        CssLayout tab4 = new CssLayout();
        tab4.setSizeFull();
        tab4.setCaption("Far1");
        tab4.addComponent(new Label("Some content for tabsheet"));

        Tab tab = addTab(tab1);
        tab.setIcon(TouchKitTestApp.getRndRunoIconResource());
        tab.setCaption("Playlists");
        tab = addTab(tab2);
        tab.setCaption("Artists");
        tab.setIcon(TouchKitTestApp.getRndRunoIconResource());
        tab = addTab(tab3);
        tab.setCaption("Vaadin");
        tab.setIcon(TouchKitTestApp.getRndRunoIconResource());
        tab = addTab(tab4);
        tab.setCaption("IT mill");
        tab.setIcon(TouchKitTestApp.getRndRunoIconResource());

        NavigationManager navigationManager = new NavigationManager();

        NavigationView navigationView = new NavigationView("FirstView");
        Button button = new Button("Böö");
        button.setWidth("60px");
        navigationView.setRightComponent(button);
        VerticalComponentGroup componentGroup = new VerticalComponentGroup();

        NavigationButton navigationButton = new NavigationButton("Test me");
        navigationButton.setDescription("no yep");
        navigationButton.addListener(this);
        componentGroup.addComponent(navigationButton);
        navigationButton = new NavigationButton("Me too");
        navigationButton.addListener(this);
        componentGroup.addComponent(navigationButton);

        navigationView.setContent(componentGroup);

        navigationManager.navigateTo(navigationView);

        Tab addTab = addTab(navigationManager);
        addTab.setCaption("Option");
        addTab.setIcon(TouchKitTestApp.getRndRunoIconResource());

        setSelectedTab(tab2);

    }

    public void buttonClick(ClickEvent event) {

        String caption2 = event.getButton().getCaption();
        NavigationView view = (NavigationView) event.getButton().getParent()
                .getParent();

        NavigationView navigationView = new NavigationView(caption2);
        Button button = new Button("Böö");
        button.setWidth("60px");
        navigationView.setRightComponent(button);
        VerticalComponentGroup componentGroup = new VerticalComponentGroup();

        NavigationButton navigationButton = new NavigationButton("Test me");
        navigationButton.setDescription("no yep");
        navigationButton.addListener(this);
        componentGroup.addComponent(navigationButton);
        navigationButton = new NavigationButton("Me too");
        navigationButton.addListener(this);
        componentGroup.addComponent(navigationButton);

        navigationView.setContent(componentGroup);

        ((NavigationManager) view.getParent()).navigateTo(navigationView);

    }

}
