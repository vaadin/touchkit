package org.vaadin.touchkit.gwt.client.theme;

import org.vaadin.touchkit.ui.VerticalComponentGroup;

/**
 * This class contains special style variations available in the TouchKit theme.
 */
public interface StyleNames {
    /** Makes a normal Button red. */
    public static final String BUTTON_RED = "red";

    /** Makes a normal Button green. */
    public static final String BUTTON_GREEN = "green";

    /** Makes a normal Button blue. */
    public static final String BUTTON_BLUE = "blue";

    /**
     * Makes a normal Button look like a navigation button. Works in
     * {@link VerticalComponentGroup}.
     */
    public static final String BUTTON_NAVIGATION = "nav";

    /**
     * Makes the description in a NavigationButton look kind of like a pill. See
     * MobileMail for an example.
     */
    public static final String NAVIGATION_BUTTON_DESC_PILL = "pill";

    /**
     * Makes a navigation button gray and pointing forward.
     */
    public static final String NAVIGATION_BUTTON_FORWARD = "forward";

    /**
     * Makes a navigation button gray and pointing backward.
     */
    public static final String NAVIGATION_BUTTON_BACK = "back";

    /**
     * Sets an up arrow icon on a {@link com.vaadin.ui.Button} in a
     * {@link org.vaadin.touchkit.ui.NavigationBar}
     */
    public static final String NAVBAR_BUTTON_ARROW_UP = "icon-arrow-up";

    /**
     * Sets a down arrow icon on a {@link com.vaadin.ui.Button} in a
     * {@link org.vaadin.touchkit.ui.NavigationBar}
     */
    public static final String NAVBAR_BUTTON_ARROW_DOWN = "icon-arrow-down";

    /**
     * Sets a left arrow icon on a {@link com.vaadin.ui.Button} in a
     * {@link org.vaadin.touchkit.ui.NavigationBar}
     */
    public static final String NAVBAR_BUTTON_ARROW_LEFT = "icon-arrow-left";

    /**
     * Sets a right arrow icon on a {@link com.vaadin.ui.Button} in a
     * {@link org.vaadin.touchkit.ui.NavigationBar}
     */
    public static final String NAVBAR_BUTTON_ARROW_RIGHT = "icon-arrow-right";

    // Included from the Base theme

    /**
     * Creates a button that looks like a regular hypertext link but still acts
     * like a normal button.
     */
    public static final String BUTTON_LINK = "link";

    /**
     * Adds the connector lines between a parent node and its child nodes to
     * indicate the tree hierarchy better.
     */
    public static final String TREE_CONNECTORS = "connectors";

    /**
     * Clips the component so it will be constrained to its given size and not
     * overflow.
     */
    public static final String CLIP = "v-clip";
}
