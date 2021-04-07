package com.vaadin.addon.touchkit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("touchkit")
@Title("TouchKit test app")
public class TouchkitTestUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        String requestPathInfo = request.getPathInfo();
        if (requestPathInfo.length() > 3) {
            try {

                String className;
                if (requestPathInfo.startsWith("/com.")) {
                    className = requestPathInfo.substring(1);
                } else {
                    className = getClass().getPackage().getName() + ".itest."
                            + requestPathInfo.substring(1);
                }
                if(className.contains("/")) {
                    className = className.substring(0, className.indexOf("/"));
                }
                Class<?> forName = Class.forName(className);
                if (forName != null) {
                    AbstractComponentContainer newInstance = (AbstractComponentContainer) forName
                            .newInstance();
                    newInstance.setDescription(null);
                    setContent(newInstance);
                    System.out.println("Initialized " + className);
                }
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                // e.printStackTrace();
            } catch (IllegalAccessException e) {
                // e.printStackTrace();
            }
        }

        File itestroot = new File("../vaadin-touchkit-agpl/src/test/java/"
                + getClass().getPackage().getName().replace(".", "/")
                + "/itest");

        Collection<Class<? extends AbstractComponent>> tests = new ArrayList<Class<? extends AbstractComponent>>();
        addTests(getClass().getPackage().getName() + ".itest", itestroot, tests);

        Table table = new Table();
        final BeanItemContainer<Class<? extends AbstractComponent>> beanItemContainer = new BeanItemContainer<Class<? extends AbstractComponent>>(
                tests);
        table.setContainerDataSource(beanItemContainer);
        table.setVisibleColumns(new Object[] { "simpleName" });
        table.addGeneratedColumn("description", new ColumnGenerator() {
            public Object generateCell(Table source, Object itemId,
                    Object columnId) {
                Class<?> c = (Class<?>) itemId;
                try {
                    AbstractComponent t = (AbstractComponent) c
                            .newInstance();
                    String description = t.getDescription();
                    if(description == null) {description = "-";};
                    Label label2 = new Label(description);
                    // label2.setWidth("300px");
                    // label2.setHeight("50px");
                    return label2;
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                return null;
            }
        });
        table.addItemClickListener(new ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                Class<?> itemId = (Class<?>) event.getItemId();
                String canonicalName = itemId.getCanonicalName();
                String debug = debugmode.getValue() ? "?debug" : "";
                Page.getCurrent().open(canonicalName + debug, null);
            }
        });
        table.setSizeFull();
        table.setColumnCollapsingAllowed(true);
        debugmode.setValue(false);
        HorizontalLayout options = new HorizontalLayout();
        options.addComponent(debugmode);
        TextField textField = new TextField();
        textField.setInputPrompt("Filter");
        textField.addTextChangeListener(new TextChangeListener() {
            
            @Override
            public void textChange(TextChangeEvent event) {
                
                beanItemContainer.removeAllContainerFilters();
                beanItemContainer.addContainerFilter("simpleName", event.getText(), true, false);
            }
        });
        options.addComponent(textField);
        addComponent(options);
        content.addComponent(table);
        content.setExpandRatio(table, 1);
    }

    CheckBox debugmode = new CheckBox("Open in debug");

    VerticalLayout content = new VerticalLayout();

    private void addComponent(Component component) {
        content.setSizeFull();
        if (content.getParent() == null) {
            setContent(content);
        }
        content.addComponent(component);
    }

    private void addTests(String base, File itestroot,
            Collection<Class<? extends AbstractComponent>> tests) {
        File[] listFiles = itestroot.listFiles();
        for (File file : listFiles) {
            if (file.isDirectory()) {
                addTests(base + "." + file.getName(), file, tests);
            } else if (file.getName().endsWith(".java")) {
                String name = file.getName().substring(0,
                        file.getName().indexOf("."));
                try {
                    Class<?> forName = Class.forName(base + "." + name);

                    if (AbstractComponent.class
                            .isAssignableFrom(forName)) {
                        tests.add((Class<? extends AbstractComponent>) forName);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
