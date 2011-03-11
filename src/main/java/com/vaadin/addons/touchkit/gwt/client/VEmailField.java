package com.vaadin.addons.touchkit.gwt.client;

import com.vaadin.terminal.gwt.client.ui.VTextField;

public class VEmailField extends VTextField {
	public VEmailField() {
		getElement().setPropertyString("type", "email");
		getElement().setPropertyString("autocapitalize", "off");
		getElement().setPropertyString("autocorrect", "off");
	}
	
}
