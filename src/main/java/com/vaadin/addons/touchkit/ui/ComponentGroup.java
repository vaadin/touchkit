package com.vaadin.addons.touchkit.ui;

import com.vaadin.addons.touchkit.gwt.client.VOptionLayout;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.CssLayout;

/**
 * A layout to group controls. Uses margins, white background and rounded
 * corners to visualize the grouping.
 * <p>
 * Relative sizes work as in CssLayout, except if the component has a caption.
 * Components with caption are rendered on the same line with their caption and
 * setting this kind of component 100% width stretches it to consume all the
 * space from captions end to the content areas end.
 */
@ClientWidget(VOptionLayout.class)
public class ComponentGroup extends CssLayout {

    public ComponentGroup() {
        setStyleName("v-tk-componentgroup");
        setWidth("100%");
    }

    public ComponentGroup(String caption) {
        this();
        setCaption(caption);
    }
}
