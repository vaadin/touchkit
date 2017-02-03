package org.vaadin.touchkit.gwt.client.vcom;

import com.vaadin.shared.communication.ServerRpc;

/**
 * Server RPC interface of DatePicker
 */
public interface DatePickerServerRpc extends ServerRpc {
  /**
   * Value of DatePicker has changed on client side (yyyy-MM-dd'T'HH:mm:ss)
   */
  public void valueChanged(String date);
}
