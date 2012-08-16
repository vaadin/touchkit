package com.vaadin.addon.touchkit.ui;

/**
 * A layout to group controls horizontally. The default theme renders components
 * into a "button group".
 * <p>
 * Relative sizes are relative to the size of the
 * {@link HorizontalComponentGroup}.
 * <p>
 */
public class HorizontalComponentGroup extends AbstractComponentGroup {

    /**
     * Creates a horizontal component group
     */
    public HorizontalComponentGroup() {
        this(null);
    }

    public HorizontalComponentGroup(String caption) {
        super(caption);
        setWidth(null);
        setMargin(false);
    }

}
