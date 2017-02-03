package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;

import com.vaadin.v7.ui.TreeTable;

@SuppressWarnings("serial")
public class TreeTableTest extends AbstractTouchKitIntegrationTest {

    private final static String ITEM_1 = "grandparent 1";
    private final static String ITEM_1_1 = "parent 1/1";
    private final static String ITEM_1_1_1 = "child 1/1/1";
    private final static String ITEM_1_1_2 = "child 1/1/2";
    private final static String ITEM_2 = "parent 2";
    private final static String ITEM_2_1 = "child 2/1";

    public TreeTableTest() {
        TreeTable tree = new TreeTable();
        tree.setWidth("100%");
        tree.addContainerProperty("Name", String.class, "");
        tree.setColumnExpandRatio("Name", 1.0f);

        tree.addItem(new Object[] { ITEM_1 }, ITEM_1);
        tree.setChildrenAllowed(ITEM_1, true);
        tree.addItem(new Object[] { ITEM_1_1 }, ITEM_1_1);
        tree.setParent(ITEM_1_1, ITEM_1);
        tree.setChildrenAllowed(ITEM_1_1, true);
        tree.addItem(new Object[] { ITEM_1_1_1 }, ITEM_1_1_1);
        tree.setChildrenAllowed(ITEM_1_1_1, false);
        tree.setParent(ITEM_1_1_1, ITEM_1_1);
        tree.addItem(new Object[] { ITEM_1_1_2 }, ITEM_1_1_2);
        tree.setChildrenAllowed(ITEM_1_1_2, false);
        tree.setParent(ITEM_1_1_2, ITEM_1_1);
        tree.addItem(new Object[] { ITEM_2 }, ITEM_2);
        tree.setChildrenAllowed(ITEM_2, true);
        tree.addItem(new Object[] { ITEM_2_1 }, ITEM_2_1);
        tree.setParent(ITEM_2_1, ITEM_2);
        tree.setChildrenAllowed(ITEM_2_1, false);

        addComponent(tree);
    }

}
