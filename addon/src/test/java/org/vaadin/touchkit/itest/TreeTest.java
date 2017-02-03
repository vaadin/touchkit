package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;

import com.vaadin.v7.ui.Tree;

/**
 * Test to be used to verify that tree theming works.
 */
@SuppressWarnings("serial")
public class TreeTest extends AbstractTouchKitIntegrationTest {

    private final static String ITEM_1 = "grandparent 1";
    private final static String ITEM_1_1 = "parent 1/1";
    private final static String ITEM_1_1_1 = "child 1/1/1";
    private final static String ITEM_1_1_2 = "child 1/1/2";
    private final static String ITEM_2 = "parent 2";
    private final static String ITEM_2_1 = "child 2/1";

    public TreeTest() {
        Tree tree = new Tree();

        tree.addItem(ITEM_1);
        tree.setChildrenAllowed(ITEM_1, true);
        tree.addItem(ITEM_1_1);
        tree.setParent(ITEM_1_1, ITEM_1);
        tree.setChildrenAllowed(ITEM_1_1, true);
        tree.addItem(ITEM_1_1_1);
        tree.setChildrenAllowed(ITEM_1_1_1, false);
        tree.setParent(ITEM_1_1_1, ITEM_1_1);
        tree.addItem(ITEM_1_1_2);
        tree.setChildrenAllowed(ITEM_1_1_2, false);
        tree.setParent(ITEM_1_1_2, ITEM_1_1);
        tree.addItem(ITEM_2);
        tree.setChildrenAllowed(ITEM_2, true);
        tree.addItem(ITEM_2_1);
        tree.setParent(ITEM_2_1, ITEM_2);
        tree.setChildrenAllowed(ITEM_2_1, false);

        tree.expandItemsRecursively(ITEM_1);
        tree.expandItemsRecursively(ITEM_2);
        addComponent(tree);

    }

}
