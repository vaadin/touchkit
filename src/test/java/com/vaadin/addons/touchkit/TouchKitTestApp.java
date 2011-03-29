package com.vaadin.addons.touchkit;

import com.vaadin.Application;
import com.vaadin.addons.touchkit.ui.TouchKitWindow;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.ResizeEvent;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class TouchKitTestApp extends Application {

	@Override
	public void init() {
		final TouchKitWindow mainWindow = new TouchKitWindow();
		mainWindow.addListener(new Window.ResizeListener() {
			public void windowResized(ResizeEvent e) {
				System.err.println("Window size now:"
						+ e.getWindow().getWidth() + " x "
						+ e.getWindow().getHeight());
			}
		});
		mainWindow.setImmediate(true);
		mainWindow.setCaption("Hello mobile user");
		final Label label = new Label("Hello mobile user");
		mainWindow.addComponent(label);

		Button b = new Button("NavPanelTest");
		b.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				mainWindow.setContent(new NavPanelTest());

			}
		});

		// mainWindow.addComponent(b);

		b = new Button("NavPanelTest witn navButtons");
		b.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				mainWindow.setContent(new NavPanelTestWithNavButtons());
			}
		});

		// mainWindow.addComponent(b);

		b = new Button("NavPanelTest witn views");
		b.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				mainWindow.setContent(new NavPanelTestWithViews());
			}
		});

		mainWindow.addComponent(b);

		b = new Button("Tabsheet");
		b.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				mainWindow.setContent(new TabsheetTest());
			}
		});

		mainWindow.addComponent(b);

		setMainWindow(mainWindow);
	}

}
