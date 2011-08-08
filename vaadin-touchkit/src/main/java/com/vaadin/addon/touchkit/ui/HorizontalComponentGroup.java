package com.vaadin.addon.touchkit.ui;

/**
 * A layout to group controls horizontally.
 * <p>
 * Relative sizes are relative to the size of the
 * {@link HorizontalComponentGroup}.
 * <p>
 * The default theme renders components into a "button group" with a bit
 * stronger decoration than VerticalComponentGroup.
 * <p>
 * Due to the styling, {@link VerticalComponentGroup} is by default more
 * flexible than {@link HorizontalComponentGroup} and it can accommodate many
 * components.
 */
public class HorizontalComponentGroup extends AbstractComponentGroup {

    private static final String STYLE_NAME = "v-touchkit-horizontalcomponentgroup";

    /**
     * Creates a horizontal component group
     * 
     */
    public HorizontalComponentGroup() {
        this(null);
    }

    public HorizontalComponentGroup(String caption) {
        super(caption);
        setMargin(false);
        setStyleName(STYLE_NAME);

    }

}
