package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class DynamicVerticalComponentGroupTest extends AbstractTouchKitIntegrationTest {
	
	public DynamicVerticalComponentGroupTest() {
		setDescription("This is dynamic VerticalComponentGroup test");
		
		Label hello = new Label("Hello World");
		addComponent(hello);
		
		final VerticalComponentGroup group = new VerticalComponentGroup();
		group.setWidth("100%");
		
		for (int i = 0; i < 3; ++i) {
			final CssLayout layout = new CssLayout();
			layout.setWidth("100%");
			final Button hideCaption = new Button("Show caption");
			hideCaption.setWidth("100%");
			layout.addComponent(hideCaption);
			hideCaption.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					if (layout.getCaption() == null) {
						layout.setCaption("Caption");
						hideCaption.setCaption("Hide caption");
					} else {
						layout.setCaption(null);
						hideCaption.setCaption("Show caption");
					}
					
				}
			});
			group.addComponent(layout);
		}
		
		addComponent(group);
	}

}
