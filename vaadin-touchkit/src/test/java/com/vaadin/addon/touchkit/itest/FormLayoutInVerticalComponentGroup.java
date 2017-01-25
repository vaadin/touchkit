package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.themes.BaseTheme;

public class FormLayoutInVerticalComponentGroup extends AbstractTouchKitIntegrationTest {
    public FormLayoutInVerticalComponentGroup() {
        
        NavigationView navigationView = new NavigationView("Form");
        
        setDescription("This is a test for FormLayou in VCGt");
        
        FormLayout formLayout = new FormLayout();
        
        TextField textField = new TextField("Foo");
        formLayout.addComponent(textField);
        textField = new TextField("Bar");
        textField.setRequired(true);
        textField.setComponentError(new UserError("This is error"));
        formLayout.addComponent(textField);
        
        Switch switch1 = new Switch();
        switch1.setCaption("Diipadaipa");
        formLayout.addComponent(switch1);
        
        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup("TouchKit form");
        verticalComponentGroup.addComponent(formLayout);
        
        navigationView.setContent(verticalComponentGroup);
        addComponent(navigationView);
    }


}
