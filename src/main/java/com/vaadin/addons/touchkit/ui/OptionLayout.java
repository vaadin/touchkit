package com.vaadin.addons.touchkit.ui;

import com.vaadin.ui.CssLayout;

/**
 * A layout to group controls. Uses margins, white background and rounded
 * corners to visualize grouping.
 * <p>
 * TODO may be better to create a customized client side counterpart. Or should
 * this actually be a styled FormLayout? See eg. ios settings and something with
 * text field.
 */
public class OptionLayout extends CssLayout {

	public OptionLayout() {
		setStyleName("v-optionlayout");
		setWidth("100%");
	}

	public OptionLayout(String caption) {
		this();
		setCaption(caption);
	}
}
