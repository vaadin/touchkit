package com.vaadin.addon.touchkit.gwt.client;

import com.vaadin.addon.touchkit.gwt.client.OfflineMode.ActivationEvent;
import com.vaadin.addon.touchkit.gwt.client.OfflineMode.ActivationReason;

public final class ActivationEventImpl implements ActivationEvent {

    private String msg;
    private ActivationReason reason;

    public ActivationEventImpl(String msg, ActivationReason reason) {
        this.msg = msg;
        this.reason = reason;
    }

    @Override
    public ActivationReason getActivationReason() {
        return reason;
    }

    @Override
    public String getActivationMessage() {
        return msg;
    }
}