package com.vaadin.addons.touchkit.service;


public interface PositionCallback {

	void onSuccess(Position position);

	void onFailure(int errorCode);
	
}
