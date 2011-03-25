package com.vaadin.addons.touchkit.ui;

import com.vaadin.ui.TabSheet;

/**
 * A Tabsheet implementation that mimics native tabsheet in ios. E.g. the tabbar
 * is below the main content.
 * 
 * TODO implement with style name + css.
 */
public class Tabsheet extends TabSheet {

	public Tabsheet() {
		super();
		setStyleName("touchkit-tabsbelow");
		setSizeFull();
	}

}
