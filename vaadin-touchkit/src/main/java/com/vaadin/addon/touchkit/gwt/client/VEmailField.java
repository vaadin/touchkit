package com.vaadin.addon.touchkit.gwt.client;

import com.vaadin.terminal.gwt.client.ui.VTextField;

public class VEmailField extends VTextField {
	public VEmailField() {
		getElement().setPropertyString("type", "email");
		getElement().setAttribute("autocapitalize", "off");
		getElement().setAttribute("autocorrect", "off");
	}
	
}
