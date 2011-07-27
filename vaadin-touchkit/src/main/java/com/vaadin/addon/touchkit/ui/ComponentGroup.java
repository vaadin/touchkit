package com.vaadin.addon.touchkit.ui;

import com.vaadin.addon.touchkit.gwt.client.VComponentGroup;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.CssLayout;

/**
 * A layout to group controls. It supports two orientations,
 * {@link Orientation#HORIZONTAL} and {@link Orientation#VERTICAL}.
 * <p>
 * {@link Orientation#VERTICAL} produces a vertical grouping that by default has
 * a white background, margins, rounded corners, and is divided into rows using
 * thing lines.<br/>
 * Relative sizes work as in CssLayout, except if the component has a caption.
 * Components with caption are rendered on the same line with their caption and
 * setting this kind of component 100% width stretches it to consume all the
 * space from captions end to the content areas end.
 * <p>
 * The default theme of a ComponentGroup in {@link Orientation#HORIZONTAL} mode
 * renders components into a "button group" with bit stronger decoration than in
 * the vertical mode.
 * <p>
 * Due to the styling, {@link Orientation#VERTICAL} is by default more flexible
 * than {@link Orientation#HORIZONTAL}, and it can accommodate many components.
 */
@ClientWidget(value = VComponentGroup.class, loadStyle = LoadStyle.EAGER)
public class ComponentGroup extends CssLayout {

    public enum Orientation {
        HORIZONTAL, VERTICAL
    }

    /**
     * Sets whether the components are vertically or horizontally grouped.
     * <p>
     * Setting {@link Orientation#HORIZONTAL} will automatically disable
     * margins, while {@link Orientation#VERTICAL} will apply a 100% width. You
     * can reset these later, if needed.
     * </p>
     * 
     * @param orientation
     *            {@link Orientation#VERTICAL} or {@link Orientation#HORIZONTAL}
     */
    public void setOrientation(Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            addStyleName("v-tk-componentgroup-h");
            removeStyleName("v-tk-componentgroup");
            setMargin(false);
        } else {
            removeStyleName("v-tk-componentgroup-h");
            addStyleName("v-tk-componentgroup");
            setWidth("100%");
        }
    }

    /**
     * Constructs a vertical {@link ComponentGroup} with 100% width.
     */
    public ComponentGroup() {
        this(Orientation.VERTICAL);
    }

    /**
     * Constructs a vertical {@link ComponentGroup} with 100% width and the
     * given caption.
     * 
     * @param caption
     */
    public ComponentGroup(String caption) {
        this();
        setCaption(caption);
    }

    /**
     * Constructs a {@link ComponentGroup} with the given orientation and
     * caption.
     * 
     * @see #setOrientation(Orientation)
     * @param caption
     * @param orientation
     */
    public ComponentGroup(String caption, Orientation orientation) {
        this(orientation);
        setCaption(caption);
    }

    /**
     * Constructs a {@link ComponentGroup} with the given orientation.
     * 
     * @see #setOrientation(Orientation)
     * @param orientation
     */
    public ComponentGroup(Orientation orientation) {
        setOrientation(orientation);
    }
}
