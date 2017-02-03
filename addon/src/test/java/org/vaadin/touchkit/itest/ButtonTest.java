package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.themes.BaseTheme;

public class ButtonTest extends AbstractTouchKitIntegrationTest implements
        ClickListener {
    private int i;

    public ButtonTest() {
        setDescription("This is a button test");
        Button button = new Button();
        button.setCaption("button caption");
        button.addClickListener(this);

        Button link = new Button();
        link.setCaption("link caption");
        link.setStyleName(BaseTheme.BUTTON_LINK);
        link.addClickListener(this);

        addComponent(button);
        addComponent(link);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Notification.show("Clicked " + ++i, "Longer content for notification",
                Type.ERROR_MESSAGE);
    }

}
