package com.vaadin.addon.touchkit.service;


public interface PositionCallback {

	void onSuccess(Position position);

	void onFailure(int errorCode);
	
}
