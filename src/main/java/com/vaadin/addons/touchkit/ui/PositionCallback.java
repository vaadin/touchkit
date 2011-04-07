package com.vaadin.addons.touchkit.ui;

public interface PositionCallback {

	void onSuccess(Position position);

	void onFailure(int errorCode);
	
}
