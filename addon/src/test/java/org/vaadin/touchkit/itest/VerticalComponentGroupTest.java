package org.vaadin.touchkit.itest;

import java.util.Iterator;
import java.util.Locale;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.HorizontalButtonGroup;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NumberField;
import org.vaadin.touchkit.ui.Switch;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;

public class VerticalComponentGroupTest extends AbstractTouchKitIntegrationTest {

    public VerticalComponentGroupTest() {
        setDescription("This is VerticalComponentGroup test");
        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup(
                "Vertical component group");

        verticalComponentGroup.addComponent(new Button("Button"));
        TextField tf = new TextField(
                "A TextField with long caption text and 100% width textfield component");
        verticalComponentGroup.addComponent(tf);

        Link link = new Link("link caption", new ExternalResource(
                "http://www.gizmag.com/"));
        link.setDescription("link description text");

        verticalComponentGroup.addComponent(link);
        verticalComponentGroup.addComponent(new Switch("Switch"));
        NumberField numberField = new NumberField("numberfield");
        verticalComponentGroup.addComponent(numberField);

        NavigationButton one = new NavigationButton("Navigation button");
        one.setDescription("nav button description");
        NavigationButton too = new NavigationButton(
                "Navigation button with icon");
        too.setIcon(new ThemeResource("../runo/icons/32/ok.png"));
        too.setDescription("nav button description");
        verticalComponentGroup.addComponent(one);
        verticalComponentGroup.addComponent(too);
        verticalComponentGroup.addComponent(new NavigationButton("Simple"));
        verticalComponentGroup.addComponent(new Label(
                "FIXME: Label, between buttons bugs"));
        verticalComponentGroup.addComponent(new Button("Button too"));

        verticalComponentGroup.addComponent(getOptiongroup());

        addComponent(verticalComponentGroup);

        verticalComponentGroup = new VerticalComponentGroup();
        verticalComponentGroup.setCaption("Horizontal in vertical");

        HorizontalButtonGroup horizontalGroup = getHorizontalGroup();
        verticalComponentGroup.addComponent(horizontalGroup);

        horizontalGroup = getHorizontalGroup();
        horizontalGroup.addComponent(new Button("Third"));
        horizontalGroup.setWidth("300px");
        Iterator<Component> componentIterator = horizontalGroup
                .getComponentIterator();
        while (componentIterator.hasNext()) {
            Component next = componentIterator.next();
            next.setWidth("" + 100.0 / (double)horizontalGroup.getComponentCount() + "%");
        }
        verticalComponentGroup.addComponent(horizontalGroup);

        horizontalGroup = getHorizontalGroup();
        Iterator<Component> it = horizontalGroup.getComponentIterator();
        it.next().setCaption("Only one here");
        horizontalGroup.removeComponent(it.next());
        verticalComponentGroup.addComponent(horizontalGroup);

        addComponent(verticalComponentGroup);
    }

    private OptionGroup getOptiongroup() {
        OptionGroup languageSelect = new OptionGroup();
        Locale[] availableLocales = new Locale[] { Locale.CANADA,
                Locale.ENGLISH, Locale.GERMAN };
        for (Locale locale : availableLocales) {
            languageSelect.addItem(locale);
            languageSelect.setItemCaption(locale,
                    locale.getDisplayLanguage(locale));
        }
        languageSelect.setValue(Locale.ENGLISH);
        return languageSelect;
    }

    private HorizontalButtonGroup getHorizontalGroup() {
        HorizontalButtonGroup horizontalComponentGroup = new HorizontalButtonGroup();
        horizontalComponentGroup.addComponent(new Button("First"));
        horizontalComponentGroup.addComponent(new Button("Another"));

        return horizontalComponentGroup;
    }

}
