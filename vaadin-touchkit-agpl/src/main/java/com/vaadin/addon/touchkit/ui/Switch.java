package com.vaadin.addon.touchkit.ui;

import com.vaadin.addon.touchkit.gwt.client.VSwitch;
import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;

/**
 * A {@link CheckBox} that is rendered as a switch button, which might provide a
 * better user experience on touch devices.
 * 
 * @see CheckBox
 */
@ClientWidget(value = VSwitch.class, loadStyle = LoadStyle.EAGER)
public class Switch extends CheckBox {

    /**
     * @see CheckBox#CheckBox()
     */
    public Switch() {
        super();
    }

    /**
     * @see CheckBox#CheckBox(String,boolean)
     */
    public Switch(String caption, boolean initialState) {
        super(caption, initialState);
    }

    /**
     * @see CheckBox#CheckBox(String,ClickListener)
     */
    public Switch(String caption, ClickListener listener) {
        super(caption, listener);
    }

    /**
     * @see CheckBox#CheckBox(String,Property)
     */
    public Switch(String caption, Property dataSource) {
        super(caption, dataSource);
    }

    /**
     * @see CheckBox#CheckBox(String)
     */
    public Switch(String caption) {
        super(caption);
    }

}
