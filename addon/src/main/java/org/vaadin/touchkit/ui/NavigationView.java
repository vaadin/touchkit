package org.vaadin.touchkit.ui;

import java.util.Iterator;
import java.util.LinkedList;

import org.vaadin.touchkit.gwt.client.vcom.navigation.NavigationViewServerRpc;
import org.vaadin.touchkit.gwt.client.vcom.navigation.NavigationViewState;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;

/**
 * The NavigationView is a component container which integrates well with the
 * {@link NavigationManager}. It consists of a {@link NavigationBar}, a content
 * area, and optionally a {@link Toolbar}.
 * <p>
 * The content area is scrollable (i.e. no need to use a {@link Panel} in it).
 * The {@link NavigationView} is most commonly used with a
 * {@link NavigationManager} which provides smooth forward/back animations.
 * <p>
 * In addition to the main content area (set with {@link #setContent(Component)}
 * ), a {@link NavigationView} can contain a secondary component which, by
 * default, is positioned at the bottom of the layout. The secondary content is
 * set with {@link #setToolbar(Component)}, and is usually a {@link Toolbar}.
 * 
 */
public class NavigationView extends AbstractComponentContainer {

    private NavigationBar navigationBar = new NavigationBar();
    private Component mainComponent;
    private Component toolbar;

    private NavigationViewServerRpc rpc = new NavigationViewServerRpc() {
        @Override
        public void updateScrollPosition(int position) {
            setScrollPosition(position);
        }
    };

    /**
     * Constructs a NavigationView with the given content.
     * 
     * @param content
     */
    public NavigationView(Component content) {
        this("", content);
    }

    /**
     * Constructs a NavigationView with an empty {@link CssLayout} as its
     * content.
     */
    public NavigationView() {
        this(new CssLayout());
    }

    /**
     * Constructs a NavigationView with the given caption and an empty
     * {@link CssLayout} as its content.
     * 
     * @param caption
     *            the caption
     */
    public NavigationView(String caption) {
        this();
        setCaption(caption);
    }

    /**
     * Constructs a NavigationView with the given caption and content.
     * 
     * @param caption
     *            the caption
     * @param content
     *            the content
     */
    public NavigationView(String caption, Component content) {
        registerRpc(rpc);
        mainComponent = content;
        super.addComponent(getContent());
        super.addComponent(getNavigationBar());
        setCaption(caption);
    }

    /**
     * Sets the main content of the NavigationView. If null, an empty
     * {@link CssLayout} will be used.
     * 
     * @param c
     *            the component to set as the content
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
        markAsDirty();
    }

    /**
     * @return the content of the navigation view.
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
     * {@link CssLayout} is set as the content.
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
        markAsDirty();
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
     * @return the {@link NavigationBar}
     */
    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    /**
     * Sets the component in the navigation bar's right slot.
     * 
     * @param c
     *            the component to set in the right slot.
     */
    public void setRightComponent(Component c) {
        getNavigationBar().setRightComponent(c);
    }

    /**
     * @return the component in the right slot of the navigation bar or null if
     *         not set
     */
    public Component getRightComponent() {
        return getNavigationBar().getRightComponent();
    }

    /**
     * Sets the component in the navigation bar's left slot. Most commonly this
     * component slot is automatically populated by the NavigationView (a back
     * button).
     * 
     * @param c
     *            the component to set in the left slot.
     */
    public void setLeftComponent(Component c) {
        getNavigationBar().setLeftComponent(c);
    }

    /**
     * @return the component in the left slot of the navigation bar or null if
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
    @Override
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

    @Override
    public Iterator<Component> getComponentIterator() {
        LinkedList<Component> linkedList = new LinkedList<Component>();
        if (navigationBar != null) {
            linkedList.add(navigationBar);
        }
        if (mainComponent != null) {
            linkedList.add(mainComponent);
        }
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
     *            The component to put in the toolbar slot.
     */
    public void setToolbar(Component toolbar) {
        if (this.toolbar != null && this.toolbar != toolbar) {
            super.removeComponent(this.toolbar);
        }
        this.toolbar = toolbar;
        if (toolbar != null) {
            super.addComponent(toolbar);
        }
        markAsDirty();
    }

    /**
     * @return The component in the toolbar slot or null if no toolbar has been
     *         set
     */
    public Component getToolbar() {
        return toolbar;
    }

    @Override
    public NavigationViewState getState() {
        return (NavigationViewState) super.getState();
    }

    /**
     * Scrolls the view to the specified position.
     * 
     * @param scrollPosition
     *            the position to scroll to (y coordinate).
     */
    public void setScrollPosition(int scrollPosition) {
        getState().scrollPosition = scrollPosition;
    }

    /**
     * @return The scroll position of the view.
     */
    public int getScrollPosition() {
        return getState().scrollPosition;
    }

    /**
     * Gets the @link {@link NavigationManager} in which this view is contained.
     * 
     * @return the parent {@link NavigationManager} or null if not inside one.
     */
    public NavigationManager getNavigationManager() {
        Component p = getParent();
        if (p instanceof NavigationManager) {
            return (NavigationManager) p;
        }
        return null;
    }

    @Override
    public int getComponentCount() {
        return toolbar != null ? 3 : 2;
    }

    @Override
    public Iterator<Component> iterator() {
        return getComponentIterator();
    }

}
