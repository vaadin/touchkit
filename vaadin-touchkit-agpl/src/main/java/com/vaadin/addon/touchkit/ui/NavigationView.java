package com.vaadin.addon.touchkit.ui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.vaadin.addon.touchkit.gwt.client.VNavigationView;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

/**
 * A component container which integrates well with the
 * {@link NavigationManager}, and consists of a {@link NavigationBar}, a content
 * area, and optionally a {@link Toolbar}.
 * <p>
 * The content area is scrollable (i.e. no need to use a Panel in it).
 * {@link NavigationView} is most commonly used with {@link NavigationManager}
 * which provides smooth forward/back animations.
 * <p>
 * In addition to the main content area (set with {@link #setContent(Component)}
 * ), {@link NavigationView} can contain a secondary component that is by
 * default positioned at the bottom of the layout. The secondary content is set
 * with {@link #setToolbar(Component)}, and is usually a {@link Toolbar}.
 * 
 */
@ClientWidget(value = VNavigationView.class, loadStyle = LoadStyle.EAGER)
public class NavigationView extends AbstractComponentContainer {

    private NavigationBar navigationBar = new NavigationBar();
    private Component mainComponent;
    private Component toolbar;
    private int scrollPosition;

    /**
     * Creates a {@link NavigationView} with the given content.
     * 
     * @param content
     */
    public NavigationView(Component content) {
        navigationBar.setWidth("100%");
        setSizeFull();
        mainComponent = content;
        super.addComponent(getContent());
        super.addComponent(getNavigationBar());
    }

    /**
     * Creates a {@link NavigationView} with an empty {@link CssLayout} as
     * content.
     */
    public NavigationView() {
        this(new CssLayout());
    }

    /**
     * Creates a {@link NavigationView} with the given caption and an empty
     * {@link CssLayout} as content.
     * 
     * @param caption
     */
    public NavigationView(String caption) {
        this();
        setCaption(caption);
    }

    /**
     * Creates a {@link NavigationView} with the given caption and content.
     * 
     * @param caption
     * @param content
     */
    public NavigationView(String caption, Component content) {
        this(content);
        setCaption(caption);
    }

    /**
     * Sets the main content. If nulled, an empty {@link CssLayout} will be set
     * as content.
     * 
     * @param c
     */
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

    /**
     * Gets the component currently set as content.
     * 
     * @return
     */
    public Component getContent() {
        return mainComponent;
    }

    /**
     * @deprecated use setContent or setToolbar instead
     */
    @Deprecated
    @Override
    public void addComponent(Component c) {
        setContent(c);
    }

    /**
     * The toolbar or content can be removed - other attempts will result in an
     * {@link IllegalArgumentException}. If the content is removed, an empty
     * {@link CssLayout} is set as content.
     */
    @Override
    public void removeComponent(Component c) {
        if (c == toolbar) {
            super.removeComponent(c);
            toolbar = null;
        } else if (c == mainComponent) {
            setContent(null);
        } else {
            throw new IllegalArgumentException(
                    " Only the toolbar or main content can be removed");
        }
    }

    /**
     * Removes the toolbar, and the current content (setting the content to an
     * empty {@link CssLayout}).
     */
    @Override
    public void removeAllComponents() {
        removeComponent(mainComponent);
        removeComponent(toolbar);
    }

    /**
     * Gets the {@link NavigationBar}
     * 
     * @return
     */
    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    /**
     * Sets the component on the right side of the navigation bar.
     * 
     * @param c
     */
    public void setRightComponent(Component c) {
        getNavigationBar().setRightComponent(c);
    }

    /**
     * @return the component on the right side of the navigation bar or null if
     *         not set
     */
    public Component getRightComponent() {
        return getNavigationBar().getRightComponent();
    }

    /**
     * Sets the component on the left side of the navigation bar. Most commonly
     * this component slot is automatically populated by the NavigationView.
     * 
     * @param c
     */
    public void setLeftComponent(Component c) {
        getNavigationBar().setLeftComponent(c);
    }

    /**
     * @return the component on the left side of the navigation bar or null if
     *         not set
     */
    public Component getLeftComponent() {
        return getNavigationBar().getLeftComponent();
    }

    /**
     * @see NavigationBar#getPreviousView()
     */
    public Component getPreviousComponent() {
        return getNavigationBar().getPreviousView();
    }

    /**
     * @see NavigationBar#setPreviousView(Component)
     */
    public void setPreviousComponent(Component component) {
        getNavigationBar().setPreviousView(component);
    }

    @Override
    public void setCaption(String caption) {
        getNavigationBar().setCaption(caption);
    }

    @Override
    public String getCaption() {
        return getNavigationBar().getCaption();
    }

    /**
     * Called by {@link NavigationManager} when the view is about to become
     * visible.
     */
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

    /**
     * The main content and the toolbar can be replaced - other attempts will
     * result in an {@link IllegalArgumentException}
     */
    public void replaceComponent(Component oldComponent, Component newComponent) {
        if (mainComponent == oldComponent) {
            setContent(newComponent);
        } else if (toolbar == oldComponent) {
            setToolbar(newComponent);
        } else {
            throw new IllegalArgumentException(
                    " Only the toolbar or main content can be replaced");
        }
    }

    public Iterator<Component> getComponentIterator() {
        LinkedList<Component> linkedList = new LinkedList<Component>();
        linkedList.add(navigationBar);
        linkedList.add(mainComponent);
        if (toolbar != null) {
            linkedList.add(toolbar);
        }
        return linkedList.iterator();
    }

    /**
     * Sets the toolbar component, usually a {@link Toolbar}. If a previous
     * toolbar is set, it is removed from the layout and forgotten.
     * 
     * @param toolbar
     */
    public void setToolbar(Component toolbar) {
        if (this.toolbar != null && this.toolbar != toolbar) {
            super.removeComponent(this.toolbar);
        }
        this.toolbar = toolbar;
        if (toolbar != null) {
            super.addComponent(toolbar);
        }
    }

    /**
     * Returns the toolbar for the view.
     * 
     * @return The toolbar or null if no toolbar has been set
     */
    public Component getToolbar() {
        return toolbar;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addVariable(this, "sp", scrollPosition);
        for (Iterator<Component> componentIterator = getComponentIterator(); componentIterator
                .hasNext();) {
            Component next = componentIterator.next();
            next.paint(target);
        }
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
        requestRepaint();
    }

    public int getScrollPosition() {
        return scrollPosition;
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        Integer newScrollPosition = (Integer) variables.get("sp");
        if (newScrollPosition != null) {
            scrollPosition = newScrollPosition;
        }
    }

    /**
     * Gets the @link {@link NavigationManager} in which this view is contained.
     * 
     * @return the {@link NavigationManager} or null if not inside one
     */
    public NavigationManager getNavigationManager() {
        Component p = getParent();
        if (p instanceof NavigationManager) {
            return (NavigationManager) p;
        }
        return null;
    }

}
