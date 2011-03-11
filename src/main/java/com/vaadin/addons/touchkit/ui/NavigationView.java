package com.vaadin.addons.touchkit.ui;

import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.addons.touchkit.gwt.client.VNavigationView;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

/**
 * A layout consisting of a NavigationBar and content area. The content area is
 * scrollable (no need to use Panel in it). NavigatioView is most commonly used
 * in {@link NavigationPanel} which provides smooth forwared/back animations.
 * <p>
 * In addition to the main content area (set with {@link #setContent(Component)}
 * method), {@link NavigationView} can contain a secondary component that is by
 * default positioned at the bottom of the layout. The secondary content is set
 * with setToolbarComponent most often contains a Toolbar.
 * 
 */
@ClientWidget(VNavigationView.class)
public class NavigationView extends AbstractComponentContainer {

	private NavigationBar navigationBar = new NavigationBar();
	private Component mainComponent;
	private Component toolbar;

	public NavigationView(Component content) {
		setSizeFull();
		mainComponent = content;
	}

	public NavigationView() {
		this(new CssLayout());
		super.addComponent(getNavigationBar());
		super.addComponent(getContent());
	}

	public NavigationView(String captionHtml) {
		this();
		setCaption(captionHtml);
	}

	public void setContent(Component c) {
		if (mainComponent == c) {
			return;
		}
		if (mainComponent != null) {
			super.removeComponent(mainComponent);
		}
		if (c == null) {
			c = new CssLayout();
		}
		super.addComponent(c);
		mainComponent = c;
	}

	public Component getContent() {
		return mainComponent;
	}

	/**
	 * @deprecated use setContent or setToolbar instead
	 */
	@Override
	public void addComponent(Component c) {
		setContent(c);
	}

	@Override
	public void removeComponent(Component c) {
		if (c == toolbar) {
			super.removeComponent(c);
			mainComponent = null;
		} else {
			throw new IllegalArgumentException(
					" Only main toolbar can be removed from NavigationView");
		}
	}

	@Override
	public void removeAllComponents() {
		removeComponent(mainComponent);
	}

	protected NavigationBar getNavigationBar() {
		return navigationBar;
	}

	/**
	 * Sets the component on the right side of the navigation bar.
	 * 
	 * @param c
	 */
	public void setNavigationBarComponent(Component c) {
		getNavigationBar().setNavigationBarComponent(c);
	}

	public Component getPreviousComponent() {
		return getNavigationBar().getPreviousView();
	}

	public void setPreviousComponent(Component component) {
		getNavigationBar().setPreviousView(component);
	}

	@Override
	public void setCaption(String caption) {
		getNavigationBar().setCaption(caption);
	}

	public String getCaption() {
		return getNavigationBar().getCaption();
	}

	protected void onBecomingVisible() {
		/*
		 * Due to limitations with Paintble references in UIDL, reset previous
		 * component to make sure back button renders its target view.
		 */
		Component previousComponent = getPreviousComponent();
		if (previousComponent != null) {
			setPreviousComponent(previousComponent);
		}
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {
		if (mainComponent == oldComponent) {
			setContent(newComponent);
		} else if (toolbar == oldComponent) {
			setToolbar(newComponent);
		}
	}

	@Override
	public Iterator<Component> getComponentIterator() {
		LinkedList<Component> linkedList = new LinkedList<Component>();
		linkedList.add(navigationBar);
		linkedList.add(mainComponent);
		if (toolbar != null) {
			linkedList.add(toolbar);
		}
		return linkedList.iterator();
	}

	public void setToolbar(Component toolbar) {
		if (this.toolbar != null && this.toolbar != toolbar) {
			super.removeComponent(this.toolbar);
		}
		this.toolbar = toolbar;
		if(toolbar != null) {
			super.addComponent(toolbar);
		}
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		for (Iterator<Component> componentIterator = getComponentIterator(); componentIterator
				.hasNext();) {
			Component next = componentIterator.next();
			next.paint(target);
		}
	}

}
