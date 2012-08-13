package com.vaadin.addon.touchkit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.vaadin.RootRequiresMoreInformationException;
import com.vaadin.addon.touchkit.ui.TouchKitApplication;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Page;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Root;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class TouchKitTestApp extends TouchKitApplication {

    // public void init() {
    // // All TouchKit apps should inherint from Base in order for the theme to
    // // work correctly
    // final TouchKitWindow mainWindow = new TouchKitWindow();
    // mainWindow.setPersistentSessionCookie(true);
    // CssLayout content = new CssLayout();
    // content.setMargin(true);
    // mainWindow.setContent(content);
    // mainWindow.addListener(new Window.ResizeListener() {
    // public void windowResized(ResizeEvent e) {
    // System.err.println("Window size now:"
    // + e.getWindow().getWidth() + " x "
    // + e.getWindow().getHeight());
    // }
    // });
    // mainWindow.setImmediate(true);
    // mainWindow.setCaption("Hello mobile user");
    // final Label label = new Label("Hello mobile user");
    // mainWindow.addComponent(label);
    //
    // NativeSelect nativeSelect = new NativeSelect("Select test");
    // nativeSelect.setImmediate(true);
    // nativeSelect.addItem(Selects.class.getSimpleName());
    // nativeSelect.addItem(PersonView.class.getSimpleName());
    // nativeSelect.addListener(new Property.ValueChangeListener() {
    // public void valueChange(ValueChangeEvent event) {
    // getMainWindow().open(
    // new ExternalResource(getURL().toString()
    // + event.getProperty() + "/"));
    // }
    // });
    //
    // mainWindow.addComponent(nativeSelect);
    //
    // Button b = new Button("NavPanelTest");
    // b.addListener(new ClickListener() {
    //
    // public void buttonClick(ClickEvent event) {
    // mainWindow.setContent(new NavPanelTest());
    //
    // }
    // });
    //
    // // mainWindow.addComponent(b);
    //
    // b = new Button("NavPanelTest with navButtons");
    // b.addStyleName("white");
    // b.addListener(new ClickListener() {
    // public void buttonClick(ClickEvent event) {
    // mainWindow.setContent(new NavPanelTestWithNavButtons());
    // }
    // });
    //
    // // mainWindow.addComponent(b);
    //
    // b = new Button("NavPanelTest with views");
    // b.addStyleName("white");
    // b.addListener(new ClickListener() {
    // public void buttonClick(ClickEvent event) {
    // NavPanelTestWithViews.app = TouchKitTestApp.this;
    // mainWindow.setContent(new NavPanelTestWithViews());
    // }
    // });
    //
    // mainWindow.addComponent(b);
    //
    // b = new Button("TouchKitTabsheet");
    // b.addListener(new ClickListener() {
    // public void buttonClick(ClickEvent event) {
    // mainWindow.setContent(new TabsheetTest());
    // }
    // });
    //
    // b = new Button("SwipeView test");
    // b.addListener(new ClickListener() {
    // public void buttonClick(ClickEvent event) {
    // mainWindow.setContent(new SwipeViewTestMgr());
    // }
    // });
    // mainWindow.addComponent(b);
    //
    // b = new Button("Junkyard");
    // b.addListener(new ClickListener() {
    // public void buttonClick(ClickEvent event) {
    // mainWindow.setContent(new JunkYard());
    // }
    // });
    //
    // mainWindow.addComponent(b);
    //
    // mainWindow.addComponent(new Button("Geolocation",
    // new Button.ClickListener() {
    //
    // public void buttonClick(ClickEvent event) {
    // mainWindow
    // .detectCurrentPosition(new PositionCallback() {
    //
    // public void onSuccess(Position position) {
    //
    // double latitude = position
    // .getLatitude();
    // double longitude = position
    // .getLongitude();
    // double accuracy = position
    // .getAccuracy();
    //
    // mainWindow.addComponent(new Label(
    // "Position is: " + longitude
    // + " " + latitude
    // + "(accuracy:"
    // + accuracy + ")"));
    //
    // }
    //
    // public void onFailure(int errorCode) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // });
    // }
    // }));
    //
    // mainWindow.addComponent(new Button("Show switch",
    // new Button.ClickListener() {
    //
    // public void buttonClick(ClickEvent event) {
    // Switch switch1 = new Switch();
    // switch1.setCaption("Jep");
    // mainWindow.addComponent(switch1);
    //
    // }
    // }));
    //
    // mainWindow.addComponent(new Button("PlainNavigationView with buttons",
    // new Button.ClickListener() {
    // public void buttonClick(ClickEvent event) {
    // NavPanelTestWithViews.app = TouchKitTestApp.this;
    // mainWindow.setContent(new NavViewWithButtons());
    // }
    // }));
    //
    // Table table = new Table();
    // table.addContainerProperty("Property", String.class, "value");
    // table.addContainerProperty("Another", String.class, "value");
    // table.addContainerProperty("Third", String.class, "value");
    // for (int i = 0; i < 100; i++) {
    // Object addItem = table.addItem();
    // table.getItem(addItem).getItemProperty("Property")
    // .setValue("value " + i);
    // }
    //
    // table.setColumnCollapsingAllowed(true);
    // table.setColumnReorderingAllowed(true);
    // table.setSelectable(true);
    //
    // ComboBox comboBox = new ComboBox();
    // comboBox.setContainerDataSource(table);
    // comboBox.setValue(comboBox.getItemIds().iterator().next());
    // comboBox.setItemCaptionPropertyId("Property");
    //
    // // TODO reduce a bug: put these into different order and table is hidden
    // // until combobox menu is opened
    // mainWindow.addComponent(table);
    // mainWindow.addComponent(comboBox);
    //
    // setMainWindow(mainWindow);
    // }

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

    private void addTests(String base, File itestroot,
            Collection<Class<? extends AbstractTouchKitIntegrationTest>> tests) {
        File[] listFiles = itestroot.listFiles();
        for (File file : listFiles) {
            if (file.isDirectory()) {
                addTests(base + "." + file.getName(), file, tests);
            } else if (file.getName().endsWith(".java")) {
                String name = file.getName().substring(0,
                        file.getName().indexOf("."));
                try {
                    Class<?> forName = Class.forName(base + "." + name);

                    if (AbstractTouchKitIntegrationTest.class
                            .isAssignableFrom(forName)) {
                        tests.add((Class<? extends AbstractTouchKitIntegrationTest>) forName);
                    }
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }

    }
    
    @Override
    protected Root getRoot(WrappedRequest request)
            throws RootRequiresMoreInformationException {
        // TODO Auto-generated method stub
        return super.getRoot(request);
    }

    @Override
    public Root getTouchRoot(WrappedRequest request) {
        return new Root(){

            @Override
            protected void init(WrappedRequest request) {
                // FIXME
                // setTheme("base");

                String requestPathInfo = request.getRequestPathInfo();
                if (requestPathInfo.length() > 3) {
                    try {

                        String className;
                        if (requestPathInfo.startsWith("/com.")) {
                            className = requestPathInfo.substring(1);
                        } else {
                            className = getClass().getPackage().getName() + ".itest."
                                    + requestPathInfo.substring(1);
                        }
                        Class<?> forName = Class.forName(className);
                        if (forName != null) {
                            CssLayout newInstance = (CssLayout) forName.newInstance();
                            setContent(newInstance);
                            System.out.println("Initialized " + className);
                        }
                        return;
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        // e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        // e.printStackTrace();
                    }
                }

                Label label = new Label(
                        "TouchKit test server, get test by adding its name to url, eg: Selects or subpackage.FooBar");
                addComponent(label);
                addComponent(new Label("TODO add list of available tests here"));

                File itestroot = new File("src/test/java/"
                        + getClass().getPackage().getName().replace(".", "/")
                        + "/itest");

                Collection<Class<? extends AbstractTouchKitIntegrationTest>> tests = new ArrayList<Class<? extends AbstractTouchKitIntegrationTest>>();
                addTests(getClass().getPackage().getName() + ".itest", itestroot, tests);

                Table table = new Table();
                BeanItemContainer<Class<? extends AbstractTouchKitIntegrationTest>> beanItemContainer = new BeanItemContainer<Class<? extends AbstractTouchKitIntegrationTest>>(
                        tests);
                table.setContainerDataSource(beanItemContainer);
                table.setVisibleColumns(new Object[] { "canonicalName" });
                table.addGeneratedColumn("description", new ColumnGenerator() {
                    public Object generateCell(Table source, Object itemId,
                            Object columnId) {
                        Class<?> c = (Class<?>) itemId;
                        try {
                            AbstractTouchKitIntegrationTest t = (AbstractTouchKitIntegrationTest) c
                                    .newInstance();
                            Label label2 = new Label(t.getDescription());
                            // label2.setWidth("300px");
                            // label2.setHeight("50px");
                            return label2;
                        } catch (InstantiationException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
                table.addListener(new ItemClickListener() {

                    public void itemClick(ItemClickEvent event) {
                        Class<?> itemId = (Class<?>) event.getItemId();
                        String canonicalName = itemId.getCanonicalName();
                        Page.getCurrent().open(new ExternalResource(canonicalName));
                    }
                });
                addComponent(table);
            }};
    }
}
