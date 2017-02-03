package org.vaadin.touchkit.itest;

import java.util.Date;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.gwt.client.theme.StyleNames;
import org.vaadin.touchkit.ui.DatePicker;
import org.vaadin.touchkit.ui.EmailField;
import org.vaadin.touchkit.ui.HorizontalButtonGroup;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.NumberField;
import org.vaadin.touchkit.ui.Popover;
import org.vaadin.touchkit.ui.Switch;
import org.vaadin.touchkit.ui.Toolbar;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;

public class FieldsTest extends AbstractTouchKitIntegrationTest implements
        ClickListener {

    private final Switch switchComponent;
    private final OptionGroup optionGroup;
    private final TextField textField;
    private final TextArea textArea;
    private final Toolbar toolbar;

    public FieldsTest() {
        setDescription("Test various fields in typical setup.");

        NavigationView navigationView = new NavigationView("Fields");

        navigationView.setPreviousComponent(new Panel("Previous"));

        HorizontalButtonGroup group = new HorizontalButtonGroup();
        Button button = new Button("Save", this);
        button.setStyleName(StyleNames.BUTTON_BLUE);
        group.addComponent(button);
        button = new Button("X");
        button.setStyleName(StyleNames.BUTTON_RED);
        group.addComponent(button);

        navigationView.setRightComponent(group);

        addComponent(navigationView);

        VerticalComponentGroup g = new VerticalComponentGroup("Basic fields");

        NavigationButton navigationButton = new NavigationButton("Navi button");
        navigationButton.setDescription("Stuff that matters");
        g.addComponent(navigationButton);
        navigationButton.setIcon(FontAwesome.HOME);

        textField = new TextField("Notification title");
        textField.setWidth("100%");
        textField.setValue("Foo Bar");
        textArea = new TextArea("Notification content");
        textArea.setWidth("100%");
        textArea.setValue("Sen on muuten parempi pyy pivossa kuin kymmenen oksalla.");
        NumberField numberField = new NumberField("Number");
        numberField.setWidth("100%");

        DatePicker datePicker = new DatePicker("Html5Date");
        datePicker.setValue(new Date());

        DateField dateField = new DateField("Amaz VaadinDF");
        dateField.setValue(new Date());

        g.addComponents(textField, textArea, numberField, datePicker, dateField);

        Upload upload = new Upload();
        upload.setImmediate(true);
        upload.setCaption("Upload");
        upload.setButtonCaption("Take or Choose Photo");
        g.addComponent(upload);

        switchComponent = new Switch("Switch");
        g.addComponent(switchComponent);

        g.addComponent(new EmailField("Email"));

        VerticalComponentGroup g2 = new VerticalComponentGroup("Type");
        optionGroup = new OptionGroup();
        for (Type t : Type.values()) {
            optionGroup.addItem(t);
        }
        optionGroup.setValue(Type.ERROR_MESSAGE);
        g2.addComponent(optionGroup);

        NativeSelect select = new NativeSelect("Different type");
        select.setContainerDataSource(optionGroup);
        select.setPropertyDataSource(optionGroup);

        ComboBox comboBox = new ComboBox("Combodough");
        comboBox.setContainerDataSource(optionGroup);

        g2.addComponents(select, comboBox);

        Label label = new Label("This is some example text that is "
                + "written here for demonstration purposes only.");
        g.addComponent(label);

        CssLayout cssLayout = new CssLayout(g, g2);

        group = new HorizontalButtonGroup();
        button = new Button("Save");
        button.setStyleName(StyleNames.BUTTON_GREEN);
        group.addComponent(button);
        button = new Button("Cancel");
        button.setStyleName(StyleNames.BUTTON_RED);
        group.addComponent(button);
        cssLayout.addComponent(group);

        Button c = new Button("Normal Button");
        cssLayout.addComponent(c);

        navigationView.setContent(cssLayout);

        toolbar = new Toolbar();

        button = new Button();
        button.setIcon(FontAwesome.HOME);
        toolbar.addComponent(button);

        button = new Button(null, this);
        button.setIcon(FontAwesome.FLOPPY_O);
        toolbar.addComponent(button);
        button = new Button();
        button.setIcon(FontAwesome.SHARE);
        toolbar.addComponent(button);
        button = new Button();
        button.setIcon(FontAwesome.PRINT);
        toolbar.addComponent(button);
        button = new Button();
        button.setIcon(FontAwesome.SITEMAP);
        toolbar.addComponent(button);

        navigationView.setToolbar(toolbar);

    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (switchComponent.getValue()) {

            NavigationView content = new NavigationView("Popover");
            final Popover popover = new Popover(content);
            popover.setHeight("50%");
            popover.setWidth("400px");
            content.setRightComponent(new Button("X", new ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    popover.removeFromParent();
                }
            }));
            CssLayout c = new CssLayout(new Label("This is content area."));
            content.setContent(c);
            popover.showRelativeTo(event.getButton());
        } else {
            Notification.show(textField.getValue(), textArea.getValue(),
                    getType());
        }

    }

    private Type getType() {
        return (Type) optionGroup.getValue();
    }

}
