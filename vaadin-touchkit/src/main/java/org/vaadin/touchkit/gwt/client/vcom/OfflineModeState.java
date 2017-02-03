package org.vaadin.touchkit.gwt.client.vcom;

import com.vaadin.shared.communication.SharedState;

public class OfflineModeState extends SharedState {
    public static final int DEFAULT_OFFLINE_MODE_DELAY = 10;

    public Integer persistentSessionTimeout = null;
    public int offlineModeTimeout = DEFAULT_OFFLINE_MODE_DELAY;
}
