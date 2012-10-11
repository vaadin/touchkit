package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.addon.touchkit.gwt.client.CheckBox;
import com.vaadin.shared.ui.Connect;

/**
 * Overrides default implementation of Connector of CheckBox. Moves caption
 * presenting from widget to parent (layout). FIXME overriding connector
 * implementation don't work anymore
 */
@SuppressWarnings("serial")
// @Connect(com.vaadin.ui.CheckBox.class)
public class CheckBoxConnector extends
        com.vaadin.client.ui.checkbox.CheckBoxConnector {

    @Override
    public boolean delegateCaptionHandling() {
        return true;
    }

    @Override
    public CheckBox getWidget() {
        return (CheckBox) super.getWidget();
    }

}
