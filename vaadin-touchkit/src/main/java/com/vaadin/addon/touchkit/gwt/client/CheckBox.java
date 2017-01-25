package com.vaadin.addon.touchkit.gwt.client;

import com.vaadin.v7.client.ui.VCheckBox;

/**
 * Overrides the default implementation of client side CheckBox. Ignores the setText
 * command as the caption is presented by the parent.
 */
public class CheckBox extends VCheckBox {
  
  @Override
  public void setText(String text) {
    //ignored
  }

}