package com.vaadin.addons.touchkit.ui;

import com.vaadin.addons.touchkit.gwt.client.VTabsheet;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.TabSheet;

/**
 * A Tabsheet implementation that mimics native tabsheet in ios. E.g. the tabbar
 * is below the main content.
 */
@ClientWidget(VTabsheet.class)
public class Tabsheet extends TabSheet {

	public Tabsheet() {
		super();
		setStyleName("touchkit-tabsbelow");
		setSizeFull();
	}

}
