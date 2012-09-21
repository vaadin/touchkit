package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.addon.touchkit.gwt.client.TouchButton;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;

@SuppressWarnings("serial")
@Connect(value = com.vaadin.addon.touchkit.ui.TouchButton.class, loadStyle = LoadStyle.EAGER)
public class TouchButtonConnector extends ButtonConnector {

	public TouchButton getWidget() {
		return (TouchButton)super.getWidget();
	}
	
}
