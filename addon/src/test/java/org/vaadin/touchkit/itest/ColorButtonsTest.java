package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.gwt.client.theme.StyleNames;
import org.vaadin.touchkit.ui.HorizontalButtonGroup;
import org.vaadin.touchkit.ui.NavigationView;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.v7.ui.VerticalLayout;

public class ColorButtonsTest extends AbstractTouchKitIntegrationTest {

    public ColorButtonsTest() {
        
        NavigationView navigationView = new NavigationView("RGB");
        
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setMargin(true);
        
        addButtons(content);
        
        HorizontalButtonGroup horizontalButtonGroup = new HorizontalButtonGroup();
        
        addButtons(horizontalButtonGroup);
        
        content.addComponent(horizontalButtonGroup);
        
        navigationView.setContent(content);
        addComponent(navigationView);
        
        horizontalButtonGroup = new HorizontalButtonGroup();
        addButtons(horizontalButtonGroup);
        navigationView.setRightComponent(horizontalButtonGroup);
        
        Button red = new Button("Red");
        red.addStyleName(StyleNames.BUTTON_RED);
        navigationView.setLeftComponent(red);
        
    }

    private void addButtons(ComponentContainer content) {
        Button normal = new Button("Normal");
        content.addComponent(normal);

        Button red = new Button("Red");
        red.addStyleName(StyleNames.BUTTON_RED);
        content.addComponent(red);

        Button green = new Button("Green");
        green.addStyleName(StyleNames.BUTTON_GREEN);
        content.addComponent(green);

        Button blue = new Button("Blue");
        blue.addStyleName(StyleNames.BUTTON_BLUE);
        content.addComponent(blue);
    }
}
