package com.vaadin.addons.touchkit.ui;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

/**
 * A native looking toolbar for e.g. buttons. Button icons will be on top of the
 * caption text and each button shall be equally sized.
 * 
 * <p>
 * Impl. note. If we'd want buttons to layout exactly like in ios, we'd need to
 * add spacers between components and expand spacers. Currently space spread
 * equally and components aligned center.
 * 
 */
public class Toolbar extends HorizontalLayout {

	private static final String STYLENAME = "v-toolbar";

	public Toolbar() {
		setMargin(false);
		setStyleName(STYLENAME);
		setWidth(100,UNITS_PERCENTAGE);
		setHeight(40, UNITS_PIXELS);
	}

	@Override
	public void addComponent(Component c) {
		super.addComponent(c);
		setComponentAlignment(c, Alignment.MIDDLE_CENTER);
	}

}
