package org.vaadin.touchkit.ui;

import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;

/**
 * The VerticalComponentGroup is a layout to group controls vertically. Unlike
 * with default layouts, Components in a VerticalComponentGroup are visually
 * decorated from other parts of the UI.
 * <p>
 * Captions are rendered on the same row as the component. Relative widths are
 * relative to the {@link VerticalComponentGroup} width except if the component
 * has a caption, in which case a relative width is relative to the remaining
 * available space.
 * <p>
 * Most commonly {@link Field}s in {@link VerticalComponentGroup} should be full
 * width, so {@link VerticalComponentGroup} automatically sets width to 100%
 * when {@link Field}s are added to it, unless they have an explicit width
 * defined.
 */
@SuppressWarnings("serial")
public class VerticalComponentGroup extends AbstractComponentGroup {

    /**
     * Constructs a vertical component group.
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

    /**
     * In addition to normal component addition, as a side effect this method
     * ensures {@link Field}s have sane width set.
     * 
     * @see org.vaadin.touchkit.ui.AbstractComponentGroup#addComponent(com.vaadin.ui.Component,
     *      int)
     */
    @Override
    public void addComponent(Component component, int index) {
        verifySaneFieldWidth(component);
        super.addComponent(component, index);
    }

    private void verifySaneFieldWidth(Component component) {
        if ((component instanceof Field) && component.getWidth() < 0) {
            component.setWidth("100%");
        }
    }

}
