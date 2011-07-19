package com.vaadin.addons.touchkit;

import com.vaadin.addons.touchkit.ui.NavigationManager;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class NavPanelTest extends NavigationManager implements
		ComponentContainer, ClickListener {

	private CssLayout[] views;

	public NavPanelTest() {

		views = new CssLayout[7];
		for (int i = 0; i < views.length; i++) {
			views[i] = getView();
			views[i].addComponent(new Label("View " + i));
			views[i].setDebugId("V" + i);
		}

		navigateTo(views[0]);
		setNextComponent(views[1]);

	}

	private CssLayout getView() {
		CssLayout cssLayout = new CssLayout();
		Button back = new Button("Back");
		back.addListener(this);
		cssLayout.addComponent(back);
		Button forward = new Button("Forward");
		forward.addListener(this);
		cssLayout.addComponent(forward);
		cssLayout.setSizeFull();
		return cssLayout;
	}

	public void buttonClick(ClickEvent event) {
		if (event.getButton().getCaption().equals("Back")) {
			navigateBack();
		} else {
			for (int i = 0; i < views.length; i++) {
				if (views[i] == event.getButton().getParent()) {
					if (i + 1 >= views.length) {
						event.getButton().getWindow()
								.showNotification("No more views");
					} else {
						navigateTo(views[i + 1]);
					}
					return;
				}
			}
		}

	}

}
