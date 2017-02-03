package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class DynamicVerticalComponentGroupTest extends AbstractTouchKitIntegrationTest {
	
	final VerticalComponentGroup group = new VerticalComponentGroup();
	int counter = 0;
	
	public DynamicVerticalComponentGroupTest() {
		setDescription("This is dynamic VerticalComponentGroup test");
		
		CssLayout buttonLayout = new CssLayout();
		addComponent(buttonLayout);
		
		Button addToTop = new Button("AddToTop");
		addToTop.setId("add-top");
		buttonLayout.addComponent(addToTop);
		addToTop.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				group.addComponent(new Label("First generated " + (++counter)),
						0);
			}
		});
		
		Button addToSecond = new Button("AddToSecond");
		addToSecond.setId("add-second");
		buttonLayout.addComponent(addToSecond);
		addToSecond.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				group.addComponent(new Label("Second generated " + (++counter)),
						1);
			}
		});
		
		group.setWidth("100%");
		
		for (int i = 0; i < 3; ++i) {
			final CssLayout layout = new CssLayout();
			layout.setData(new Integer(++counter));
			layout.setId("layout-" + counter);
			layout.setWidth("100%");
			final Button hideCaption = new Button("Show caption");
			hideCaption.setId("hide-" + counter);
			hideCaption.setWidth("30%");
			layout.addComponent(hideCaption);
			hideCaption.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					if (layout.getCaption() == null || layout.getCaption().isEmpty()) {
						layout.setCaption("Caption #" + layout.getData().toString());
						hideCaption.setCaption("Hide caption");
					} else {
						layout.setCaption(null);
						hideCaption.setCaption("Show caption");
					}
					
				}
			});
			
			Button remove = new Button("Remove");
			remove.setId("remove-" + counter);
			remove.setWidth("30%");
			layout.addComponent(remove);
			remove.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					group.removeComponent(layout);	
				}
			});
			
			Button first = new Button("First");
			first.setId("first-" + counter);
			first.setWidth("30%");
			layout.addComponent(first);
			first.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					group.addComponent(layout, 0);	
				}
			});
			
			
			
			group.addComponent(layout);
		}
		
		addComponent(group);
	}

}
