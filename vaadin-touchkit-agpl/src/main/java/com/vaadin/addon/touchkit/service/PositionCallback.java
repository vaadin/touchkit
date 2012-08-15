package com.vaadin.addon.touchkit.service;

import com.vaadin.addon.touchkit.gwt.client.vaadincomm.Position;

public interface PositionCallback {

    void onSuccess(Position position);

    void onFailure(int errorCode);

}
