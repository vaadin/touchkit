package com.vaadin.addon.touchkit.gwt.client;

import com.vaadin.client.ui.checkbox.VCheckBox;

/**
 * Overrides default implementation of client side CheckBox. Ignores setText
 * command as caption is presented by parent.
 */
public class CheckBox extends VCheckBox {
  
  @Override
  public void setText(String text) {
    //ignored
  }

}