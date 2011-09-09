package com.vaadin.addon.touchkit.ui;

import com.vaadin.ui.CssLayout;

/**
 * Parent class that encapsulates similarities between
 * {@link HorizontalComponentGroup} and {@link VerticalComponentGroup}. Does not
 * support changing orientation on the fly.
 */
public abstract class AbstractComponentGroup extends CssLayout {

    /**
     * Constructs a {@link AbstractComponentGroup} with the given caption.
     * 
     * @param caption
     *            The caption for the component group
     */
    protected AbstractComponentGroup(String caption) {
        setCaption(caption);
    }

}