package org.vaadin.touchkit.gwt.client.vcom;

import org.vaadin.touchkit.gwt.client.CheckBox;

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
