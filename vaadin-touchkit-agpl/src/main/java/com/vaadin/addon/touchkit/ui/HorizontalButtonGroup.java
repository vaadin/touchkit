package com.vaadin.addon.touchkit.ui;

/**
 * A layout to group controls horizontally. The default theme renders components
 * into a "button group".
 * <p>
 * Relative sizes are relative to the size of the
 * {@link HorizontalButtonGroup}.
 * <p>
 */
public class HorizontalButtonGroup extends AbstractComponentGroup {

    /**
     * Creates a horizontal component group
     */
    public HorizontalButtonGroup() {
        this(null);
    }

    public HorizontalButtonGroup(String caption) {
        super(caption);
        setWidth(null);
    }

}
