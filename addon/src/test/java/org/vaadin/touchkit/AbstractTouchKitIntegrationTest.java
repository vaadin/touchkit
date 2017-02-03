package org.vaadin.touchkit;

import org.junit.Ignore;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

@Ignore
public class AbstractTouchKitIntegrationTest extends CssLayout {
    
    public AbstractTouchKitIntegrationTest() {
        setSizeFull();
    }
    
    public static void makeSmallTabletSize(Component c) {
        c.setWidth(450, UNITS_PIXELS);
        c.setHeight(640, UNITS_PIXELS);
    }
}
