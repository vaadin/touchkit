package com.vaadin.addon.touchkit.ui;

import com.vaadin.addon.touchkit.gwt.client.VComponentGroup;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.CssLayout;

/**
 * Parent class that encapsulates similarities between
 * {@link HorizontalComponentGroup} and {@link VerticalComponentGroup}. Does not
 * support changing orientation on the fly.
 */
@ClientWidget(value = VComponentGroup.class, loadStyle = LoadStyle.EAGER)
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