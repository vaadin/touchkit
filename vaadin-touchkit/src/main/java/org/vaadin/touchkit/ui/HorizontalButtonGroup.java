package org.vaadin.touchkit.ui;

/**
 * HorizontalButtonGroup is a layout that groups buttons horizontally. Note that
 * only buttons are supported, but this is not enforced in any way. Using other
 * components results in undefined behavior.
 * <p>
 * Any relative sizes are relative to the size of the
 * {@link HorizontalButtonGroup}.
 * <p>
 */
public class HorizontalButtonGroup extends AbstractComponentGroup {

    /**
     * Constructs a new HorizontalButtonGroup
     */
    public HorizontalButtonGroup() {
        this(null);
    }

    /**
     * Constructs a new HorizontalButtonGroup with the given caption.
     * 
     * @param caption
     *            The caption.
     */
    public HorizontalButtonGroup(String caption) {
        super(caption);
        setWidth(null);
    }

}
