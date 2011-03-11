package com.vaadin.addons.touchkit.ui;

import com.vaadin.addons.touchkit.gwt.client.VNumberField;
import com.vaadin.data.Property;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.TextField;

@ClientWidget(VNumberField.class)
public class NumberField extends TextField {

	public NumberField() {
		super();
	}

	public NumberField(Property dataSource) {
		super(dataSource);
	}

	public NumberField(String caption, Property dataSource) {
		super(caption, dataSource);
	}

	public NumberField(String caption, String value) {
		super(caption, value);
	}

	public NumberField(String caption) {
		super(caption);
	}

}
