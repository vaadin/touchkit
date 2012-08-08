package com.vaadin.addon.touchkit.gwt.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.TextBoxBase;

public class VNumberField extends TextBoxBase {
	public VNumberField() {
	        super(Document.get().createTextInputElement());
		getElement().setPropertyString("type", "number");
	}
	
}
