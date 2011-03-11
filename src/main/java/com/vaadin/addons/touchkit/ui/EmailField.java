package com.vaadin.addons.touchkit.ui;

import com.vaadin.addons.touchkit.gwt.client.VEmailField;
import com.vaadin.data.Property;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.TextField;

@ClientWidget(VEmailField.class)
public class EmailField extends TextField {

	public EmailField() {
		super();
	}

	public EmailField(Property dataSource) {
		super(dataSource);
	}

	public EmailField(String caption, Property dataSource) {
		super(caption, dataSource);
	}

	public EmailField(String caption, String value) {
		super(caption, value);
	}

	public EmailField(String caption) {
		super(caption);
	}

}
