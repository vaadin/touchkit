package com.vaadin.addons.touchkit.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.vaadin.addons.touchkit.gwt.client.VNavigationPanel;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;

/**
 * Support all components, but back buttons are updated automatically only for
 * {@link NavigationView}s.
 * <p>
 * TODO deprecate + throw on component container mutation methods.
 * <p>
 * TODO make automatic breakcrump configurable, developers might want to release
 * previous views from memory and handle back navigation by themselves. E.g.
 * keep only one view backwards in memory and reload older ones if necessary.
 * 
 */
@ClientWidget(VNavigationPanel.class)
public class NavigationPanel extends AbstractComponentContainer {

	private Stack<Component> breadCrump = new Stack<Component>();

	private Component currentComponent;
	private Component previousComponent;
	private Component nextComponent;

	public NavigationPanel() {
		setSizeFull();
	}

	public void navigateTo(Component c) {
		if (c == null) {
			throw new UnsupportedOperationException(
					"Some view must always be visible");
		} else if (c == currentComponent) {
			/*
			 * Already nagigated to this component.
			 */
			return;
		} else if (previousComponent == c) {
			/*
			 * Same as navigateBack
			 */
			navigateBack();
			return;
		}

		if (nextComponent != c) {
			if (nextComponent != null) {
				removeComponent(nextComponent);
			}
			addComponent(c);
		} else {
			nextComponent = null;
		}
		if (previousComponent != null) {
			removeComponent(previousComponent);
			breadCrump.push(previousComponent);
		}
		previousComponent = currentComponent;
		currentComponent = c;
		notifyViewOfBecomingVisible();
		requestRepaint();
	}

	private void notifyViewOfBecomingVisible() {
		if(currentComponent instanceof NavigationView) {
			NavigationView v = (NavigationView) currentComponent;
			v.onBecomingVisible();
		}
		
	}
	
	public void navigateBack() {
		if (previousComponent == null) {
			return;
		}
		if (nextComponent != null) {
			removeComponent(nextComponent);
		}
		nextComponent = currentComponent;
		currentComponent = previousComponent;
		previousComponent = breadCrump.isEmpty() ? null : breadCrump.pop();
		if (previousComponent != null) {
			addComponent(previousComponent);
		}
		notifyViewOfBecomingVisible();
		requestRepaint();
	}

	public void setCurrentComponent(Component currentComponent) {
		this.currentComponent = currentComponent;
	}

	public Component getCurrentComponent() {
		return currentComponent;
	}

	public void setPreviousComponent(Component previousComponent) {
		this.previousComponent = previousComponent;
	}

	public Component getPreviousComponent() {
		return previousComponent;
	}

	/**
	 * If the developer know the next component where user is going to navigate,
	 * it can be set with this method. Having a null as nextComponent shows a
	 * placeholder content until the next view is rendered.
	 */
	public void setNextComponent(Component nextComponent) {
		this.nextComponent = nextComponent;
		requestRepaint();
	}

	public Component getNextComponent() {
		return nextComponent;
	}

	public void replaceComponent(Component oldComponent, Component newComponent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		if (currentComponent != null)
			target.addAttribute("c", currentComponent);
		if (nextComponent != null)
			target.addAttribute("n", nextComponent);
		if (previousComponent != null)
			target.addAttribute("p", previousComponent);
		Iterator<Component> componentIterator = getComponentIterator();
		while (componentIterator.hasNext()) {
			Component next = componentIterator.next();
			next.paint(target);
		}
	}

	public Iterator<Component> getComponentIterator() {
		ArrayList<Component> components = new ArrayList<Component>(3);
		if (previousComponent != null) {
			components.add(previousComponent);
		}
		if (currentComponent != null) {
			components.add(currentComponent);
		}
		if (nextComponent != null) {
			components.add(nextComponent);
		}
		return components.iterator();
	}

}
