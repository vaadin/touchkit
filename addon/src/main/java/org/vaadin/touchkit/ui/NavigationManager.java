package org.vaadin.touchkit.ui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.vaadin.touchkit.gwt.client.vcom.navigation.NavigationManagerSharedState;
import org.vaadin.touchkit.ui.NavigationManager.NavigationEvent.Direction;

import com.vaadin.event.ConnectorEventListener;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;

/**
 * The NavigationManager is a non-visible component container that allows for
 * smooth navigation between components, or views. It support all components,
 * but back buttons are updated automatically only for {@link NavigationView}s.
 * <p>
 * When a component is navigated to, it replaces the currently visible
 * component, which in turn is pushed on to the stack of previous views. One can
 * navigate backwards by calling {@link #navigateBack()}, in which case the
 * currently visible view is forgotten (still cached in case the user decides to
 * navigate to it again, see {@link #getNextComponent()}) and the previous view
 * is restored from the stack and made visible.
 * <p>
 * When used with {@link NavigationView}s, {@link NavigationBar}s and
 * {@link NavigationButton}s, navigation is smooth and quite automatic.
 * <p>
 * Bootstrap the navigation by giving the {@link NavigationManager} an initial
 * view, either by using the constructor {@link #NavigationManager(Component)}
 * or by calling {@link #navigateTo(Component)}.
 */
public class NavigationManager extends AbstractComponentContainer {

    /*
     * Implementation notes
     * 
     * Actually has three 'active' components: previous, current and next. The
     * previous component is actually pushed onto the viewStack only when
     * navigateTo() pushes everything down. I.e setPreviousComponent() actually
     * replaces the previous component before it's pushed onto the stack. In
     * javadoc, this is simplified to ignore implementation details, instead
     * pretending the previous component is topmost on the 'history'.
     */

    private Stack<Component> viewStack = new Stack<Component>();
    private boolean maintainBreadcrumb = true;

    /**
     * Constructs a NavigationManager that is 100% wide and high.
     */
    public NavigationManager() {
        setSizeFull();
    }

    /**
     * Constructs a NavigationManager that is 100% wide and high, and initially
     * navigates to (shows) the given component.
     */
    public NavigationManager(Component c) {
        this();
        navigateTo(c);
    }

    @Override
    public NavigationManagerSharedState getState() {
        return (NavigationManagerSharedState) super.getState();
    }

    /**
     * Gets the view stack. Each time the user navigates forward, the previous
     * view is pushed on top of the view stack and when navigating backwards,
     * views are popped off of the view stack.
     * <p>
     * Developers can override components in this stack if they want to manually
     * modify the breadcrumb or e.g. release previous views for garbage
     * collection.
     * 
     * @see #isMaintainBreadcrumb()
     * 
     * @return the navigation view stack.
     */
    public Stack<Component> getViewStack() {
        return viewStack;
    }

    /**
     * Navigates to the given component, effectively making it the new visible
     * component. If the given component is actually the previous component in
     * the history, {@link #navigateBack()} is performed, otherwise the replaced
     * view (previously visible) is pushed onto the view stack.
     * 
     * @param c
     *            the view to navigate to
     */
    public void navigateTo(Component c) {
        if (c == null) {
            throw new UnsupportedOperationException(
                    "Some component must always be visible");
        } else if (c == getCurrentComponent()) {
            /*
             * Already navigated to this component.
             */
            return;
        } else if (getPreviousComponent() == c) {
            /*
             * Same as navigateBack
             */
            navigateBack();
            return;
        }

        if (getNextComponent() != c) {
            if (getNextComponent() != null) {
                removeComponent(getNextComponent());
                getState().setNextComponent(null);
            }
            addComponent(c);
        } else {
            getState().setNextComponent(null);
        }

        if (c instanceof NavigationView) {
            NavigationView navigationView = (NavigationView) c;
            if (navigationView.getPreviousComponent() == null) {
                navigationView.setPreviousComponent(getCurrentComponent());
            }
        }

        if (getPreviousComponent() != null) {
            removeComponent(getPreviousComponent());
            if (isMaintainBreadcrumb()) {
                getViewStack().push(getPreviousComponent());
            }
        }
        getState().setPreviousComponent(getCurrentComponent());
        getState().setCurrentComponent(c);
        notifyViewOfBecomingVisible();
        markAsDirty();
        fireEvent(new NavigationEvent(this, Direction.FORWARD));
    }

    private void notifyViewOfBecomingVisible() {
        if (getCurrentComponent() instanceof NavigationView) {
            NavigationView v = (NavigationView) getCurrentComponent();
            v.onBecomingVisible();
            /*
             * TODO consider forcing setting the previous component here.
             */
        }

    }

    /**
     * Navigates backwards in history by popping the previous component off of
     * the view stack and making it visible. The currently visible view is
     * replaced and cached for a short while (see {@link #getNextComponent()} in
     * case the user wishes to return to the same view.
     */
    public void navigateBack() {
        if (getPreviousComponent() == null) {
            return;
        }
        if (getNextComponent() != null) {
            removeComponent(getNextComponent());
        }
        // nextComponent is kept for the animation and in case the user
        // navigates 'back to the future':
        getState().setNextComponent(getCurrentComponent());
        getState().setCurrentComponent(getPreviousComponent());
        if (isMaintainBreadcrumb()) {
            getState().setPreviousComponent(
                    getViewStack().isEmpty() ? null : getViewStack().pop());
        } else {
            getState().setPreviousComponent(null);
        }
        if (getPreviousComponent() != null) {
            addComponent(getPreviousComponent());
        }
        notifyViewOfBecomingVisible();
        markAsDirty();
        fireEvent(new NavigationEvent(this, Direction.BACK));
    }

    /**
     * Sets the currently visible component in the NavigationManager.
     * <p>
     * If the current component is already set it is overridden. If the previous
     * component or the next component is of type NavigationView, their next and
     * previous components will be automatically re-assigned.
     * 
     * @param newCurrentComponent
     *            the component to set as the currently visible component.
     */
    public void setCurrentComponent(Component newCurrentComponent) {
        if (getCurrentComponent() != newCurrentComponent) {
            if (getCurrentComponent() != null) {
                removeComponent(getCurrentComponent());
            }
            getState().setCurrentComponent(newCurrentComponent);
            addComponent(newCurrentComponent);
            if (getPreviousComponent() != null
                    && getCurrentComponent() instanceof NavigationView) {
                NavigationView view = (NavigationView) getCurrentComponent();
                view.setPreviousComponent(getPreviousComponent());
            }
            if (getNextComponent() != null
                    && getNextComponent() instanceof NavigationView) {
                NavigationView view = (NavigationView) getNextComponent();
                view.setPreviousComponent(getCurrentComponent());

            }
            markAsDirty();
        }
    }

    /**
     * @return the component that is currently visible
     */
    public Component getCurrentComponent() {
        return (Component) getState().getCurrentComponent();
    }

    /**
     * Replaces the topmost component in the history, forgetting the replaced
     * component - i.e modifies the history.
     * 
     * @param newPreviousComponent
     *            the new previous component
     */
    public void setPreviousComponent(Component newPreviousComponent) {
        if (getPreviousComponent() != newPreviousComponent) {
            if (getPreviousComponent() != null) {
                removeComponent(getPreviousComponent());
            }
            getState().setPreviousComponent(newPreviousComponent);
            if (getCurrentComponent() instanceof NavigationView) {
                NavigationView view = (NavigationView) getCurrentComponent();
                view.setPreviousComponent(newPreviousComponent);
            }
            if (getPreviousComponent() != null) {
                addComponent(newPreviousComponent);
            }
            markAsDirty();
        }
    }

    /**
     * @return the previous component, e.g. the top of the view stack, or null
     *         if n/a
     */
    public Component getPreviousComponent() {
        return (Component) getState().getPreviousComponent();
    }

    /**
     * If the developer knows the next component where user is going to
     * navigate, it can be set with this method. This might allow the component
     * to be pre-rendered before the actual navigation (and animation) occurs.
     * Having a null as nextComponent shows a placeholder content until the next
     * view is rendered.
     * <p>
     * When navigating backwards, this is used to cache the views in case the
     * user decides to return to the same view.
     */
    public void setNextComponent(Component nextComponent) {
        if (this.getNextComponent() == nextComponent) {
            return;
        }
        if (this.getNextComponent() != null) {
            removeComponent(this.getNextComponent());
        }
        getState().setNextComponent(nextComponent);
        if (nextComponent != null) {
            addComponent(nextComponent);
        }
        markAsDirty();
    }

    /**
     * @see #setNextComponent(Component)
     * @return the next component, or null if none set
     */
    public Component getNextComponent() {
        return (Component) getState().getNextComponent();
    }

    /**
     * This operation is not supported
     * 
     * @throws UnsupportedOperationException
     */
    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Component> getComponentIterator() {
        ArrayList<Component> components = getComponents();
        return components.iterator();
    }

    private ArrayList<Component> getComponents() {
        ArrayList<Component> components = new ArrayList<Component>(3);
        if (getPreviousComponent() != null) {
            components.add(getPreviousComponent());
        }
        if (getCurrentComponent() != null) {
            components.add(getCurrentComponent());
        }
        if (getNextComponent() != null) {
            components.add(getNextComponent());
        }
        return components;
    }

    /**
     * A NavigationEvent is triggered when the user navigates forward or
     * backward in the NavigationManager.
     */
    public static class NavigationEvent extends com.vaadin.ui.Component.Event {

        public enum Direction {
            BACK, FORWARD
        }

        private Direction direction;

        /**
         * Constructs a NavigationEvent with the given source and direction of
         * navigation.
         * 
         * @param source
         *            the source
         * @param direction
         *            the direction of navigation
         */
        public NavigationEvent(Component source, Direction direction) {
            super(source);
            this.direction = direction;
        }

        /**
         * @return the direction of navigation
         */
        public Direction getDirection() {
            return direction;
        }

    }

    /**
     * A NavigationListener is notified whenever a navigation event occurs.
     * NavigationListeners can be used for setting the next view in cases where
     * it is known. See {@link #setNextComponent(com.vaadin.ui.Component)}
     */
    public interface NavigationListener extends ConnectorEventListener {
        Method METHOD = ReflectTools.findMethod(NavigationListener.class,
                "navigate", NavigationEvent.class);

        /**
         * Called when a navigation event is triggered
         * 
         * @param event
         *            the navigation event.
         */
        public void navigate(NavigationEvent event);
    }

    /**
     * Adds a navigation listener that is notified whenever a navigation event
     * occurs.
     * 
     * @param listener
     *            the listener to add.
     */
    public void addNavigationListener(NavigationListener listener) {
        addListener(NavigationEvent.class, listener, NavigationListener.METHOD);
    }

    /**
     * Removes a navigation listener.
     * 
     * @param listener
     *            the listener to remove.
     */
    public void removeListener(NavigationListener listener) {
        removeListener(NavigationEvent.class, listener,
                NavigationListener.METHOD);
    }

    /**
     * @return true if NavigationManager should maintain a breadcrumb of visited
     *         views. The default is true.
     * @see #setMaintainBreadcrumb(boolean)
     */
    public boolean isMaintainBreadcrumb() {
        return maintainBreadcrumb;
    }

    /**
     * Configures whether the NavigationManager maintains a breadcrumb of
     * visited views automatically. This is handy when using NavigationViews to
     * dive into a deep hierarchy.
     * 
     * @param maintainBreadcrumb
     *            true if a breadcrumb should be maintained
     */
    public void setMaintainBreadcrumb(boolean maintainBreadcrumb) {
        this.maintainBreadcrumb = maintainBreadcrumb;
    }

    @Override
    public int getComponentCount() {
        return getComponents().size();
    }

    @Override
    public Iterator<Component> iterator() {
        return getComponentIterator();
    }

}
