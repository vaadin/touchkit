package com.vaadin.addon.touchkit.rootextensions;

import com.vaadin.addon.touchkit.gwt.client.vcom.Position;

public interface PositionCallback {

    void onSuccess(Position position);

    void onFailure(int errorCode);

}
