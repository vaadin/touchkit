package com.vaadin.addon.touchkit.rootextensions;

import java.io.Serializable;

/**
 * Callback for asynchronous local storage access from server side code.
 */
public interface LocalStorageCallback extends Serializable {
    
    public interface FailureEvent extends Serializable {
        public String getMessage();
    }

    /**
     * Called when access was successful
     * 
     * @param value the value
     */
    void onSuccess(String value);

    /**
     * Called when something went wrong with local storage access
     */
    void onFailure(FailureEvent error);

}
