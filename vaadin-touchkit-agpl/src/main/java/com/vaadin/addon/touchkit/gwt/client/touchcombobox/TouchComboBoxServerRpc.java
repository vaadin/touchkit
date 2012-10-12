package com.vaadin.addon.touchkit.gwt.client.touchcombobox;

import com.vaadin.shared.communication.ServerRpc;

/**
 * Server RPC interface of DatePicker
 */
public interface TouchComboBoxServerRpc extends ServerRpc {

    public void textValueChanged(String filter);

    public void selectionEvent(String key);

    public void next(String key);

    public void previous(String key);

    public void clearPageNumber();

    public void pageLengthChange(int itemAmount, String key);
}
