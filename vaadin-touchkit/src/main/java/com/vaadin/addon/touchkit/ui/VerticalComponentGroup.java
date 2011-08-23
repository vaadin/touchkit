package com.vaadin.addon.touchkit.ui;

import com.vaadin.addon.touchkit.gwt.client.VVerticalComponentGroup;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;

/**
 * A layout to group controls vertically. Items in a
 * {@link VerticalComponentGroup} have by default white background, margins and
 * rounded corners.
 * <p>
 * Captions are rendered on the same row as the component. Relative widths are
 * relative to the {@link VerticalComponentGroup} width except if the component
 * has a caption, in which case a relative width is relative to the remaining
 * available space.
 * <p>
 * Due to the styling, {@link VerticalComponentGroup} is by default more
 * flexible than {@link HorizontalComponentGroup} and it can accommodate many
 * components.
 */
@ClientWidget(value = VVerticalComponentGroup.class, loadStyle = LoadStyle.EAGER)
public class VerticalComponentGroup extends AbstractComponentGroup {

    private static final String STYLE_NAME = "v-touchkit-verticalcomponentgroup";

    /**
     * Creates a vertical component group.
     * <p>
     * The default width is 100%.
     */
    public VerticalComponentGroup() {
        this(null);
    }

    /**
     * Creates a vertical component group that is 100% wide.
     */
    public VerticalComponentGroup(String caption) {
        super(caption);
        setWidth("100%");
        setMargin(true);
        setStyleName(STYLE_NAME);
    }

}
