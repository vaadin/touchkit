package org.vaadin.touchkit.itest;

import java.util.Date;
import java.util.Locale;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.gwt.client.theme.StyleNames;
import org.vaadin.touchkit.ui.HorizontalButtonGroup;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.Popover;
import org.vaadin.touchkit.ui.SwipeView;
import org.vaadin.touchkit.ui.Switch;
import org.vaadin.touchkit.ui.TabBarView;
import org.vaadin.touchkit.ui.VerticalComponentGroup;
import org.vaadin.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import org.vaadin.touchkit.ui.NavigationButton.NavigationButtonClickListener;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Slider;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

public class ThemeTest extends AbstractTouchKitIntegrationTest {

    NavigationManager mainManager;

    class TextView extends SwipeView {
        public TextView() {
            Label label = new Label(
                    "This is some plain text to show how that looks.<br/>Please just go back to the previous view.<p>No, really. Please?</p><b>Swipe to get back</b>");
            label.setContentMode(ContentMode.HTML);
            setContent(label);
        }
    }

    public ThemeTest() {
        setDescription("Shows various components in order to 'test' the theme");

        mainManager = new MainContent();
        addComponent(new TabView());
    }

    class TabView extends TabBarView {
        public TabView() {

            Tab t = addTab(mainManager);
            t.setIcon(FontAwesome.FILE_TEXT);

            t = addTab(new Components());
            t.setIcon(FontAwesome.CALENDAR);

            t = addTab(new Notifications());
            t.setIcon(FontAwesome.EXCLAMATION_TRIANGLE);

            t = addTab(new TableThing());
            t.setIcon(FontAwesome.LIST);
        }
    }

    class FirstTab extends NavigationView {
        public FirstTab() {
            setCaption("View Title");

            CssLayout layout = new CssLayout();
            setContent(layout);

            {
                VerticalComponentGroup group = new VerticalComponentGroup(
                        "Group caption:");
                layout.addComponent(group);

                NavigationButton b = new NavigationButton(new TextView());
                b.setCaption("Navigation Button");
                b.setDescription("123");
                group.addComponent(b);

                TextField tf = new TextField("Caption");
                tf.setValue("TextField Value");
                group.addComponent(tf);

                tf = new TextField("Longer caption");
                tf.setValue("Erroneous value");
                tf.setComponentError(new UserError("This is not right"));
                group.addComponent(tf);

                NativeSelect sel = new NativeSelect("Required");
                sel.setRequired(true);
                sel.setValue("Native select...");
                group.addComponent(sel);
            }

            {
                VerticalComponentGroup group = new VerticalComponentGroup(
                        "Link buttons:");
                layout.addComponent(group);

                Button b = new Button("Link button...");
                b.setStyleName(StyleNames.BUTTON_LINK);
                group.addComponent(b);
                group.setIcon(FontAwesome.CAMERA);

                b = new Button("Remove");
                group.addComponent(b);
                b.setStyleName(StyleNames.BUTTON_LINK);
                b.setIcon(FontAwesome.TRASH_O);
            }

            {
                Button b = new Button("Normal Button");
                layout.addComponent(b);

                b = new Button("Primary Button");
                b.setStyleName(StyleNames.BUTTON_BLUE);
                layout.addComponent(b);

                b = new Button("Danger Button");
                b.setStyleName(StyleNames.BUTTON_RED);
                layout.addComponent(b);
            }

            {
                HorizontalButtonGroup bg = new HorizontalButtonGroup();
                layout.addComponent(bg);
                bg.addComponent(new Button("Button"));
                bg.addComponent(new Button("Another"));
                bg.addComponent(new Button("Last Button"));
            }
        }

    }

    class Components extends NavigationView {
        public Components() {
            setCaption(getClass().getSimpleName());

            CssLayout layout = new CssLayout();
            setContent(layout);

            VerticalComponentGroup group = new VerticalComponentGroup(
                    "Switches:");
            layout.addComponent(group);

            Switch sw = new Switch("Offline mode");
            group.addComponent(sw);
            sw = new Switch("Use my location", true);
            group.addComponent(sw);

            group = new VerticalComponentGroup("Components:");
            layout.addComponent(group);

            Slider slider = new Slider("Slider", 0, 100);
            slider.setValue(98d);
            group.addComponent(slider);

            DateField df = new DateField("Date fallback");
            df.setValue(new Date(113, 6, 8, 13, 45));
            df.setResolution(Resolution.MINUTE);
            df.setLocale(Locale.US);
            group.addComponent(df);

            CheckBox cb = new CheckBox("Not checked");
            group.addComponent(cb);
            cb = new CheckBox("Checked", true);
            group.addComponent(cb);

            OptionGroup og = new OptionGroup();
            og.addItem("Not selected");
            og.addItem("Selected");
            og.select("Selected");
            group.addComponent(og);

            ComboBox combo = new ComboBox("Combo box");
            combo.addItem("Option value");
            for (int i = 1; i <= 20; i++) {
                combo.addItem("Option value " + i);
            }
            combo.select("Option value");
            group.addComponent(combo);

        }

    }

    class Notifications extends NavigationView {
        public Notifications() {
            setCaption(getClass().getSimpleName());

            CssLayout layout = new CssLayout();
            setContent(layout);

            HorizontalButtonGroup notifications = new HorizontalButtonGroup(
                    "Notifications:");
            layout.addComponent(notifications);

            notifications.addComponent(new Button("Regular",
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            Notification n = new Notification(
                                    "Notification Title",
                                    "Notification description message text, which spans multiple lines.");
                            n.show(Page.getCurrent());
                        }
                    }));

            notifications.addComponent(new Button("Warning",
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            Notification n = new Notification(
                                    "Warning",
                                    "Notification description message text, which spans multiple lines.",
                                    Notification.Type.WARNING_MESSAGE);
                            n.show(Page.getCurrent());
                        }
                    }));

            notifications.addComponent(new Button("Error",
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            Notification n = new Notification(
                                    "Error",
                                    "Notification description message text, which spans multiple lines.",
                                    Notification.Type.ERROR_MESSAGE);
                            n.show(Page.getCurrent());
                        }
                    }));

            Button pop = new Button("Popover", new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    Popover p = new Popover(new MainContent());
                    p.setHeight("50%");
                    p.showRelativeTo(event.getComponent());
                }
            });
            layout.addComponent(pop);
        }
    }

    class TableThing extends NavigationView {
        public TableThing() {
            setCaption(getClass().getSimpleName());

            CssLayout layout = new CssLayout();
            setContent(layout);

            layout.addComponent(new Label("A fairly long table follows"));

            Table tbl = new Table();
            tbl.setSelectable(true);
            tbl.addContainerProperty("Name", String.class, "John Doe");
            tbl.addContainerProperty("Area", String.class, "A12");
            tbl.addContainerProperty("Date", String.class, "5/23/13");
            tbl.addContainerProperty("Start", String.class, "7:00 AM");
            tbl.addContainerProperty("End", String.class, "3:00 PM");
            for (int i = 0; i < 50; i++) {
                tbl.addItem();
            }
            layout.addComponent(tbl);
        }
    }

    class MainContent extends NavigationManager {
        public MainContent() {
            final FirstTab tab = new FirstTab();
            setCurrentComponent(tab);

            NavigationButton previous = new NavigationButton("Previous...", tab);
            previous.addClickListener(new NavigationButtonClickListener() {

                @Override
                public void buttonClick(NavigationButtonClickEvent event) {
                    Notification.show("Sorry, this is just for show.");
                }
            });
            previous.setStyleName(StyleNames.NAVIGATION_BUTTON_BACK);
            tab.setLeftComponent(previous);

            Button button = new Button(FontAwesome.BARS);
            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    Notification
                            .show("Yeah, this is another notification. You clicked the button.");
                }
            });
            tab.setRightComponent(button);
        }
    }

}
