package com.vaadin.addon.touchkit.gwt.client.vcom.touchcombobox;

import com.vaadin.shared.communication.ServerRpc;

/**
 * Server RPC interface of DatePicker
 */
public interface TouchComboBoxServerRpc extends ServerRpc {

    /**
     * Send new filter string to server.
     * 
     * @param filter
     */
    public void filterTextValueChanged(String filter);

    /**
     * Select item with given key as new value
     * 
     * @param key
     */
    public void selectionEvent(String key);

    /**
     * Showing next page of items
     * 
     * @param key
     */
    public void nextPage(String key);

    /**
     * Showing previous page of items
     * 
     * @param key
     */
    public void previousPage(String key);

    /**
     * Inform server side that selection box has been closed and next time it's
     * opened items should be shown from first position
     */
    public void clearPageNumber();

    /**
     * Send amount of items that are visible
     * 
     * @param itemAmount
     * @param key
     */
    public void pageLengthChange(int itemAmount, String key);
}
