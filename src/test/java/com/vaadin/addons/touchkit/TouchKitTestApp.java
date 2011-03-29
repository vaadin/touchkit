package com.vaadin.addons.touchkit;

import java.util.Random;

import com.vaadin.Application;
import com.vaadin.addons.touchkit.ui.TouchKitWindow;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
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

	private static final String[] runoicons = new String[] { "arrow-down.png",
			"arrow-left.png", "arrow-right.png", "arrow-up.png",
			"attention.png", "calendar.png", "cancel.png", "document-add.png",
			"document-delete.png", "document-doc.png", "document-edit.png",
			"document-image.png", "document-pdf.png", "document-ppt.png",
			"document-txt.png", "document-web.png", "document-xsl.png",
			"document.png", "email-reply.png", "email-send.png", "email.png",
			"folder-add.png", "folder-delete.png", "folder.png", "globe.png",
			"help.png", "lock.png", "note.png", "ok.png", "reload.png",
			"settings.png", "trash-full.png", "trash.png", "user.png",
			"users.png"

	};

	static Random rnd = new Random();

	public static Resource getRndRunoIconResource() {
		return new ThemeResource("../runo/icons/64/"
				+ runoicons[rnd.nextInt(runoicons.length)]);
	}

	public Embedded getRndIcon32() {
		Embedded embedded = new Embedded(null, getRndRunoIconResource());
		embedded.setHeight(32, Embedded.UNITS_PIXELS);
		return embedded;
	}

}
