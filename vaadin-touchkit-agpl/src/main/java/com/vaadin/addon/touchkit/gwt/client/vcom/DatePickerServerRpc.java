package com.vaadin.addon.touchkit.gwt.client.vcom;

import java.util.Date;

import com.vaadin.shared.communication.ServerRpc;

/**
 * Server RPC interface of DatePicker
 */
public interface DatePickerServerRpc extends ServerRpc {
  /**
   * Value of DatePicker has changed on client side
   */
  public void valueChanged(Date date);
}
