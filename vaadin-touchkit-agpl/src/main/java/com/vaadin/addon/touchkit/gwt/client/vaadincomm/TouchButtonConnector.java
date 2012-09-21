package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.vaadin.addon.touchkit.gwt.client.TouchButton;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;
import com.vaadin.ui.Button;

@SuppressWarnings("serial")
@Connect(value = Button.class, loadStyle = LoadStyle.EAGER)
public class TouchButtonConnector extends ButtonConnector {

	@Override
	public TouchButton getWidget() {
		return (TouchButton)super.getWidget();
	}
	
}
