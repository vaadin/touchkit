package com.vaadin.addon.touchkit.itest.oldtests;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class NavPanelTestWithNavButtons extends NavigationManager implements
		ComponentContainer {

	private SimpleNavView[] views;

	public NavPanelTestWithNavButtons() {

		views = new SimpleNavView[7];
		for (int i = 0; i < views.length; i++) {
			SimpleNavView v = new SimpleNavView();
			views[i] = v;
			if (i == 0) {
				v.getPrev().setEnabled(false);
				continue;
			}
			v.getPrev().setTargetView(views[i - 1]);
			views[i - 1].getNext().setTargetView(v);
			views[i - 1].getNext().setCaption(v.getCaption());

			if (i + 1 == views.length) {
				v.getNext().setEnabled(false);
			}
		}

		navigateTo(views[0]);

	}

	static class SimpleNavView extends CssLayout {
		static int counter = 0;
		private NavigationButton next = new NavigationButton("next");
		private NavigationButton prev = new NavigationButton("prev");

		public void setNext(NavigationButton next) {
			this.next = next;
		}

		public NavigationButton getNext() {
			return next;
		}

		public void setPrev(NavigationButton prev) {
			this.prev = prev;
		}

		public NavigationButton getPrev() {
			return prev;
		}

		public SimpleNavView() {
			setDebugId("SNV" + counter);
			setCaption("Nav view " + counter);
			addComponent(new Label("Nav view " + counter++));
			addComponent(prev);
			addComponent(next);
			prev.addListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					/*
					 * Hack to make test somewhat working.
					 * addAttribute(Paintable) don't support painting un
					 * attached components properly. It works but breaks subtree
					 * caching in some cases. This is handled in
					 * NavigatioButton. Thats why we need to force repaint of
					 * prev button in next visible view. This will be
					 * automatically handled by NavigationManager for
					 * NavigationViews.
					 */
					SimpleNavView targetView = (SimpleNavView) prev.getTargetView();
					targetView.prev.requestRepaint();
					targetView.next.requestRepaint();
				}
			});
		}
	}

}
