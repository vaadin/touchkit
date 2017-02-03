package org.vaadin.touchkit.itest.example;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;

public class ExampleTest extends AbstractTouchKitIntegrationTest {

    public ExampleTest() {
        setDescription("This is an example of a component test app. This description should give an overview of what this test does. Used in test summary table.");

        Button button = new Button("Böö");
        button.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                Notification.show("Hi di hou!");
            }
        });
        addComponent(button);

    }

}
