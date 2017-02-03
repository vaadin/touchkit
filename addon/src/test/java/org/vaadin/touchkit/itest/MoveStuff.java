package org.vaadin.touchkit.itest;

import java.util.Random;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationView;

import com.vaadin.ui.Button;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Label;

public class MoveStuff extends AbstractTouchKitIntegrationTest implements ClickListener {

    Random r = new Random(0);
    private Label c;
    private NavigationView another;
    private NavigationView navigationView;

    public MoveStuff() {
        setDescription("Move stuff from view to another");
        
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        
        navigationView = new NavigationView("First");
        navigationView.setWidth("40%");
        navigationView.setId("first ");
        
        
        c = new Label("Foo");
        c.setId("stuff");
        navigationView.setContent(c);
        
        Button button = new Button("Move", this);
        navigationView.setRightComponent(button);
        

        another = new NavigationView("Another");
        another.setId("another ");
        another.setWidth("40%");
        
        horizontalLayout.addComponents(navigationView, another);
        addComponent(horizontalLayout);
    }

    @Override
    public void buttonClick(ClickEvent event) {
//        navigationView.setContent(null);
        another.setContent(c);
        
    }

}
