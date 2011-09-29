package com.vaadin.addon.touchkit.itest;

import java.util.Random;

import com.vaadin.Application;
import com.vaadin.addon.touchkit.service.Position;
import com.vaadin.addon.touchkit.service.PositionCallback;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.ResizeEvent;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class TouchKitTestApp extends Application {

    @Override
    public void init() {
        // All TouchKit apps should inherint from Base in order for the theme to
        // work correctly
        setTheme("base");
        final TouchKitWindow mainWindow = new TouchKitWindow();
        mainWindow.setPersistentSessionCookie(true);
        CssLayout content = new CssLayout();
        content.setMargin(true);
        mainWindow.setContent(content);
        mainWindow.addListener(new Window.ResizeListener() {
            public void windowResized(ResizeEvent e) {
                System.err.println("Window size now:"
                        + e.getWindow().getWidth() + " x "
                        + e.getWindow().getHeight());
            }
        });
        mainWindow.setImmediate(true);
        mainWindow.setCaption("Hello mobile user");
        final Label label = new Label("Hello mobile user");
        mainWindow.addComponent(label);
        
        NativeSelect nativeSelect = new NativeSelect("Select test");
        nativeSelect.setImmediate(true);
        nativeSelect.addItem(Selects.class.getSimpleName());
        nativeSelect.addItem(PersonView.class.getSimpleName());
        nativeSelect.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                getMainWindow().open(new ExternalResource(getURL().toString() + event.getProperty() + "/"));
            }
        });
        
        mainWindow.addComponent(nativeSelect);

        Button b = new Button("NavPanelTest");
        b.addListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                mainWindow.setContent(new NavPanelTest());

            }
        });

        // mainWindow.addComponent(b);

        b = new Button("NavPanelTest with navButtons");
        b.addStyleName("white");
        b.addListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                mainWindow.setContent(new NavPanelTestWithNavButtons());
            }
        });

        // mainWindow.addComponent(b);

        b = new Button("NavPanelTest with views");
        b.addStyleName("white");
        b.addListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                NavPanelTestWithViews.app = TouchKitTestApp.this;
                mainWindow.setContent(new NavPanelTestWithViews());
            }
        });

        mainWindow.addComponent(b);

        b = new Button("TouchKitTabsheet");
        b.addListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                mainWindow.setContent(new TabsheetTest());
            }
        });

        b = new Button("Junkyard");
        b.addListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                mainWindow.setContent(new JunkYard());
            }
        });

        mainWindow.addComponent(b);

        mainWindow.addComponent(new Button("Geolocation",
                new Button.ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        mainWindow
                                .detectCurrentPosition(new PositionCallback() {

                                    public void onSuccess(Position position) {

                                        double latitude = position
                                                .getLatitude();
                                        double longitude = position
                                                .getLongitude();
                                        double accuracy = position
                                                .getAccuracy();

                                        mainWindow.addComponent(new Label(
                                                "Position is: " + longitude
                                                        + " " + latitude
                                                        + "(accuracy:"
                                                        + accuracy + ")"));

                                    }

                                    public void onFailure(int errorCode) {
                                        // TODO Auto-generated method stub

                                    }

                                });
                    }
                }));

        mainWindow.addComponent(new Button("Show switch",
                new Button.ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        Switch switch1 = new Switch();
                        switch1.setCaption("Jep");
                        mainWindow.addComponent(switch1);

                    }
                }));

        mainWindow.addComponent(new Button("PlainNavigationView with buttons",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        NavPanelTestWithViews.app = TouchKitTestApp.this;
                        mainWindow.setContent(new NavViewWithButtons());
                    }
                }));

        Table table = new Table();
        table.addContainerProperty("Property", String.class, "value");
        table.addContainerProperty("Another", String.class, "value");
        table.addContainerProperty("Third", String.class, "value");
        for (int i = 0; i < 100; i++) {
            Object addItem = table.addItem();
            table.getItem(addItem).getItemProperty("Property")
                    .setValue("value " + i);
        }

        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setSelectable(true);

        ComboBox comboBox = new ComboBox();
        comboBox.setContainerDataSource(table);
        comboBox.setValue(comboBox.getItemIds().iterator().next());
        comboBox.setItemCaptionPropertyId("Property");

        // TODO reduce a bug: put these into different order and table is hidden
        // until combobox menu is opened
        mainWindow.addComponent(table);
        mainWindow.addComponent(comboBox);

        setMainWindow(mainWindow);
    }

    private static final String[] runoicons = new String[] { "arrow-down.png",
            "arrow-left.png", "arrow-right.png", "arrow-up.png",
            "attention.png", "calendar.png", "cancel.png", "document-add.png",
            "document-delete.png", "document-doc.png", "document-edit.png",
            "document-image.png", "document-pdf.png", "document-ppt.png",
            "document-txt.png", "document-web.png", "document-xsl.png",
            "document.png", "email-reply.png", "email-send.png", "email.png",
            "folder-add.png", "folder-delete.png", "folder.png", "globe.png",
            "help.png", "lock.png", "note.png", "ok.png", "reload.png",
            "settings.png", "trash-full.png", "trash.png", "user.png",
            "users.png"

    };

    static Random rnd = new Random();

    public static Resource getRndRunoIconResource() {
        return new ThemeResource("../runo/icons/64/"
                + runoicons[rnd.nextInt(runoicons.length)]);
    }

    public Embedded getRndIcon32() {
        Embedded embedded = new Embedded(null, getRndRunoIconResource());
        embedded.setHeight(32, Embedded.UNITS_PIXELS);
        return embedded;
    }
    
    /**
     * Returns test views (instances of TouchKitWindow) by their name.
     * 
     * @see com.vaadin.addon.touchkit.ui.TouchKitApplication#getWindow(java.lang.
     *      String)
     */
    @Override
    public Window getWindow(String name) {
        Window window = (Window) super.getWindow(name);
        if (window == null && name != null && !"".equals(name)
                && !name.contains(".ico")) {

            try {

                String className = getClass().getPackage().getName() + "."
                        + name;
                Class<?> forName = Class.forName(className);
                if (forName != null) {
                    TouchKitWindow newInstance = (TouchKitWindow) forName
                            .newInstance();
                    window = newInstance;
                    addWindow(window);
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return window;
    }
    

}
