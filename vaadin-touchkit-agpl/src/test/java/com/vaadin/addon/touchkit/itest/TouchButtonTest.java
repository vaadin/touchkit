package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.TouchButton;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;

@SuppressWarnings("serial")
public class TouchButtonTest extends AbstractTouchKitIntegrationTest {
	
	public TouchButtonTest() {
		setDescription("TouchButton tests");
		
		CssLayout layout = new CssLayout();
		
		TouchButton touch = new TouchButton("Touch", touchListener);
		layout.addComponent(touch);
		
		Button normal = new Button("Normal", normalListener);
		layout.addComponent(normal);
		
		addComponent(layout);
	}
	
	private Button.ClickListener touchListener = new Button.ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			Notification notif = new Notification("Touch!");
			notif.show(TouchButtonTest.this.getUI().getPage());
		}
	};
	
	private Button.ClickListener normalListener = new Button.ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			Notification notif = new Notification("Normal!");
			notif.show(TouchButtonTest.this.getUI().getPage());
		}
	};

}
