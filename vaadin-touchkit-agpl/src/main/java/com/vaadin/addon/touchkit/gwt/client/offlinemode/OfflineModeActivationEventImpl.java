package com.vaadin.addon.touchkit.gwt.client.offlinemode;

import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationEvent;
import com.vaadin.addon.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason;

public final class OfflineModeActivationEventImpl implements ActivationEvent {

    private String msg;
    private ActivationReason reason;

    public OfflineModeActivationEventImpl(String msg, ActivationReason reason) {
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