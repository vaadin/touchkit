package org.vaadin.touchkit.ui;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.CheckBox;

/**
 * A {@link CheckBox} that is rendered as a switch button, which might provide a
 * better user experience on touch devices.
 * 
 * @see CheckBox
 */
public class Switch extends CheckBox {
	
    private static final String STYLENAME = "v-touchkit-switch-widget";

    /**
     * @see CheckBox#CheckBox()
     */
    public Switch() {
        super();
        setPrimaryStyleName(STYLENAME);
    }

    /**
     * @see CheckBox#CheckBox(String,boolean)
     */
    public Switch(String caption, boolean initialState) {
        super(caption, initialState);
        setPrimaryStyleName(STYLENAME);
    }

    /**
     * @see CheckBox#CheckBox(String,Property)
     */
    public Switch(String caption, Property<Boolean> dataSource) {
        super(caption, dataSource);
        setPrimaryStyleName(STYLENAME);
    }

    /**
     * @see CheckBox#CheckBox(String)
     */
    public Switch(String caption) {
        super(caption);
        setPrimaryStyleName(STYLENAME);
    }
        
}
