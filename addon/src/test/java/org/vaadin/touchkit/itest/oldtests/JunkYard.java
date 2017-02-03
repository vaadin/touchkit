package org.vaadin.touchkit.itest.oldtests;

import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.HierarchicalContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.InlineDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Tree;

public class JunkYard extends CssLayout {

    public JunkYard() {
//        setMargin(true);
        setWidth("100%");
        
        TextField textField = new TextField("Textfield");
        textField.setInputPrompt("prompt");
        addComponent(textField);

        /**
         * ComboBox
         */
        ComboBox cb = new ComboBox();
        cb.setWidth("100%");
        cb.setInputPrompt("Prompt");
        for (int i = 1; i <= 100; i++) {
            cb.addItem("Item " + i);
        }
        cb.setItemIcon("Item " + 1, new ThemeResource(
                "../runo/icons/64/document.png"));
        addComponent(cb);

        /**
         * DateField
         */
        DateField df = new DateField();
        df.setWidth("100%");
        addComponent(df);
        df = new InlineDateField();
        addComponent(df);

        /**
         * Tree
         */
        Tree tree = new Tree(null, createContainer());
        for (Object rootItems : tree.rootItemIds()) {
            tree.expandItemsRecursively(rootItems);
        }
        tree.setChildrenAllowed("Item 73", false);

        addComponent(tree);

        /**
         * Table
         * */
        Table table = new Table();
        table.addContainerProperty("Property", String.class, "value");
        table.addContainerProperty("Another", String.class, "value");
        table.addContainerProperty("Third", String.class, "value");
        for (int i = 0; i < 100; i++) {
            table.addItem();
        }

        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setSelectable(true);

        addComponent(table);

    }

    private HierarchicalContainer createContainer() {
        HierarchicalContainer cont = new HierarchicalContainer();
        cont.addContainerProperty("name", String.class, "");

        for (int i = 0; i < 20; i++) {
            Item item = cont.addItem("Item " + i);
            item.getItemProperty("name").setValue("Item " + i);
            cont.setChildrenAllowed("Item " + i, false);

            if (i == 1 || i == 4) {
                cont.setChildrenAllowed("Item " + i, true);
            }

            // Add three items to item 1
            if (i > 1 && i < 4) {
                cont.setParent("Item " + i, "Item 1");
            }

            // Add 5 items to item 4
            if (i > 4 && i < 10) {
                cont.setChildrenAllowed("Item " + i, true);

                if (i == 7) {
                    item = cont.addItem("Item 71");
                    item.getItemProperty("name").setValue("Item 71");
                    cont.setParent("Item 71", "Item " + i);
                    cont.setChildrenAllowed("Item 71", false);

                    item = cont.addItem("Item 72");
                    item.getItemProperty("name").setValue("Item 72");
                    cont.setParent("Item 72", "Item " + i);
                    cont.setChildrenAllowed("Item 72", true);

                    item = cont.addItem("Item 73");
                    item.getItemProperty("name").setValue("Item 73");
                    cont.setParent("Item 73", "Item 72");
                    cont.setChildrenAllowed("Item 73", true);

                    item = cont.addItem("Item 74");
                    item.getItemProperty("name").setValue("Item 74");
                    cont.setParent("Item 74", "Item " + i);
                    cont.setChildrenAllowed("Item 74", true);
                }

                cont.setParent("Item " + i, "Item " + (i - 1));

            }
        }

        return cont;
    }
}