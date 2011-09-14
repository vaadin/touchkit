package com.vaadin.addon.touchkit;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class FallbackApplication extends Application {

	@Override
	public void init() {
		Window window = new Window("Fallback app");
		window.addComponent(new Label(
				"Your browser is not supported by this application. You'll instead be shown this fallback application. Use webkit based application to test TouchKit"));
		setMainWindow(window);

	}

}
