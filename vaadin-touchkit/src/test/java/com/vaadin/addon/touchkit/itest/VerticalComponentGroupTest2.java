package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class VerticalComponentGroupTest2 extends AbstractTouchKitIntegrationTest {

    public VerticalComponentGroupTest2() {
        setDescription("This is VerticalComponentGroup test");
        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup(
                "Vertical component group");

        TextField tf = new TextField(
                "A TextField with long caption text and 100% width textfield component");
        verticalComponentGroup.addComponent(tf);

        addComponent(verticalComponentGroup);
        
        
        verticalComponentGroup = new VerticalComponentGroup(
                "VCG with FormLayout");

        tf = new TextField(
                "My field");
        tf.setWidth("100%");
        FormLayout formLayout = new FormLayout();
        formLayout.addComponent(tf);
        tf = new TextField(
                "Another");
        tf.setWidth("100%");
        formLayout.addComponent(tf);
        verticalComponentGroup.addComponent(formLayout);

        addComponent(verticalComponentGroup);

        
    }

}
