package com.vaadin.addon.touchkit.ui;

import com.vaadin.addon.touchkit.gwt.client.vaadincomm.TouchButtonState;
import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class TouchButton extends Button {
	
	public TouchButton(String caption) {
		super (caption);
	}
	
	public TouchButton(String caption, Button.ClickListener listener) {
		super (caption, listener);
	}
	
	@Override
	protected TouchButtonState getState() {
		return (TouchButtonState)super.getState();
	}

}
