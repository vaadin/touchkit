package com.vaadin.addon.touchkit.ui;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

/**
 * A native looking toolbar for showing buttons.
 * <p>
 * Typically {@link Button}s with icons or a {@link HorizontalComponentGroup}
 * containing Buttons are added to the Toolbar. All components will be rendered
 * as equally sized and centered vertically in the toolbar.
 * <p>
 * Toolbar is typically used as a part of a {@link NavigationView}.
 * 
 */
public class Toolbar extends CssLayout {

    /*-
     *  Implementation notes
     *  
     * If we'd want buttons to layout exactly like in ios, we'd need to add 
     * spacers between components and expand spacers. Currently space spread
     * equally and components aligned center.
     */

    private static final String STYLENAME = "v-touchkit-toolbar";

    /**
     * By default the Toolbar has no margin, is 100% wide and 44px high.
     */
    public Toolbar() {
        setMargin(false);
        setStyleName(STYLENAME);
        setWidth(100, UNITS_PERCENTAGE);
    }

    @Override
    public void addComponent(Component c) {
        super.addComponent(c);
        float width = 100 / getComponentCount();
        for (Component component : components) {
            component.setWidth(width, UNITS_PERCENTAGE);
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
    }

    @Override
    protected String getCss(Component c) {
        String css = "float:left;";
        return css;
    }

}
