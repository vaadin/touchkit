package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.extensions.Html5InputSettings;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.TextField;

public class Html5InputExtensionTest extends AbstractTouchKitIntegrationTest {
    public Html5InputExtensionTest() {
        setDescription("Test TextField extension that controls various html5 stuff.");

        final TextField textField = new TextField("Field");
        addComponent(textField);

        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup(
                "Html5 settings");

        final Html5InputSettings html5InputSettings = new Html5InputSettings(
                textField);

        html5InputSettings.setPlaceholder("this is test field");

        BeanItem<Html5InputSettings> beanItem = new BeanItem<Html5InputSettings>(
                html5InputSettings);

        final FieldGroup fieldGroup = new FieldGroup(beanItem);
        verticalComponentGroup.addComponent(fieldGroup
                .buildAndBind("autoComplete"));
        verticalComponentGroup.addComponent(fieldGroup
                .buildAndBind("autoCorrect"));
        verticalComponentGroup.addComponent(fieldGroup
                .buildAndBind("autoCapitalize"));
        verticalComponentGroup.addComponent(fieldGroup
                .buildAndBind("placeholder"));
        verticalComponentGroup.addComponent(fieldGroup
                .buildAndBind("min"));
        verticalComponentGroup.addComponent(fieldGroup
                .buildAndBind("max"));
        verticalComponentGroup.addComponent(fieldGroup
                .buildAndBind("step"));

        final TextField any = new TextField();
        any.setNullRepresentation("");
        any.setCaption("Custom property");
        any.setInputPrompt("key:value");
        verticalComponentGroup.addComponent(any);
        Button button = new Button();
        button.setCaption("Apply");
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();
                    String value = any.getValue();
                    if (value != null) {
                        String[] split = value.split(":");
                        html5InputSettings.setProperty(split[0], split[1]);
                        any.setValue(null);
                    }
                } catch (CommitException e) {
                    e.printStackTrace();
                }

            }
        });
        verticalComponentGroup.addComponent(button);
        addComponent(verticalComponentGroup);

    }

}
