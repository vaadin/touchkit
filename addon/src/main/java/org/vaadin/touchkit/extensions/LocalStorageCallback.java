package org.vaadin.touchkit.extensions;

import java.io.Serializable;

/**
 * Callback for asynchronous local storage access from server side code.
 */
public interface LocalStorageCallback extends Serializable {

    public interface FailureEvent extends Serializable {
        /**
         * @return The error message for the failure.
         */
        public String getMessage();
    }

    /**
     * Called when access was successful
     * 
     * @param value
     *            the value fetched from local storage
     */
    void onSuccess(String value);

    /**
     * Called when something went wrong with the local storage access
     * 
     * @param error
     *            A {@link FailureEvent} containing an error message.
     */
    void onFailure(FailureEvent error);

}
