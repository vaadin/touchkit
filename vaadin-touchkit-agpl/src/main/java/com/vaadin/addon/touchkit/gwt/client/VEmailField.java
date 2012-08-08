package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.TextBoxBase;

public class VEmailField extends TextBoxBase {
	public VEmailField() {
	        super(Document.get().createTextInputElement());
		getElement().setPropertyString("type", "email");
		getElement().setAttribute("autocapitalize", "off");
		getElement().setAttribute("autocorrect", "off");
	}
	
}
