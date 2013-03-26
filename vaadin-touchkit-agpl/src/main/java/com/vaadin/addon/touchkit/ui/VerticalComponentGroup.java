package com.vaadin.addon.touchkit.ui;

/**
 * The VerticalComponentGroup is a layout to group controls vertically. Items in
 * a VerticalComponentGroup have a white background, margins and rounded corners
 * by default.
 * <p>
 * Captions are rendered on the same row as the component. Relative widths are
 * relative to the {@link VerticalComponentGroup} width except if the component
 * has a caption, in which case a relative width is relative to the remaining
 * available space.
 * <p>
 * Due to the styling, VerticalComponentGroup is more flexible than
 * {@link HorizontalButtonGroup} and it can accommodate many components.
 */
@SuppressWarnings("serial")
public class VerticalComponentGroup extends AbstractComponentGroup {

    /**
     * Constructs a vertical component group with 100% width.
     */
    public VerticalComponentGroup() {
        super(null);
    }

    /**
     * Creates a vertical component group with the provided caption.
     * 
     * @param caption
     *            the caption.
     */
    public VerticalComponentGroup(String caption) {
        super(caption);
    }

}
