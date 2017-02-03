package org.vaadin.touchkit.ui;

import com.vaadin.ui.Button;

/**
 * The Toolbar is a native looking toolbar for showing buttons.
 * <p>
 * Typically {@link Button}s with icons or a {@link HorizontalButtonGroup}
 * containing Buttons are added to the Toolbar. All components will be rendered
 * as equally sized and centered vertically in the toolbar.
 * <p>
 * A Toolbar is typically used as a part of a {@link NavigationView}.
 * 
 */
public class Toolbar extends HorizontalButtonGroup {

    private static final String STYLENAME = "v-touchkit-toolbar";

    /**
     * Constructs a new Toolbar which has no margin and is 100% wide by 44px
     * high.
     */
    public Toolbar() {
    	setPrimaryStyleName(STYLENAME);
    }

}
