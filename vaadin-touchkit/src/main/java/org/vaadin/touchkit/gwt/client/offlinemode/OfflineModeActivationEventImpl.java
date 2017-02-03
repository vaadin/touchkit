package org.vaadin.touchkit.gwt.client.offlinemode;

import org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationEvent;
import org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason;

/**
 * The default implementation of the {@link ActivationEvent} interface.
 */
public final class OfflineModeActivationEventImpl implements ActivationEvent {

    private String msg;
    private ActivationReason reason;

    /**
     * Constructs a new OfflineModeActivationEventImpl with the provided message
     * and reason.
     * 
     * @param msg
     *            A human readable message telling why offline mode was
     *            activated.
     * @param reason
     *            An {@link ActivationReason} telling why offline mode was
     *            activated.
     */
    public OfflineModeActivationEventImpl(String msg, ActivationReason reason) {
        this.msg = msg;
        this.reason = reason;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivationReason getActivationReason() {
        return reason;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getActivationMessage() {
        return msg;
    }
}
