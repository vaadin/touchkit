package com.vaadin.addon.touchkit.ui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.vaadin.addon.touchkit.gwt.client.navigation.NavigationManagerSharedState;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent.Direction;
import com.vaadin.event.ConnectorEventListener;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;

/**
 * A non-visible component container that allows for smooth navigation between
 * components, or views. It support all components, but back buttons are updated
 * automatically only for {@link NavigationView}s.
 * <p>
 * When a component is navigated to, it replaces the currently visible
 * component, which in turn is pushed on to the stack of previous views. One can
 * navigate backwards by calling {@link #navigateBack()}, in which case the
 * currently visible view is forgotten and the previous view is restored from
 * the stack and made visible.
 * <p>
 * When used with {@link NavigationView}s, {@link NavigationBar}s and
 * {@link NavigationButton}s, navigation is smooth and quite automatic.
 * <p>
 * Bootstrap the navigation by giving the {@link NavigationManager} an initial
 * view, either by using the constructor {@link #NavigationManager(Component)}
 * or by calling {@link #navigateTo(Component)}.
 */
public class NavigationManager extends AbstractComponentContainer {

    /*-
    TODO deprecate + throw on component container mutation methods.
    
     */

    /*
     * Implementation notes
     * 
     * Actually has three 'active' components: previous, current and next. The
     * previous component is actually pushed onto the viewStack only when
     * natigateTo() pushes everything down. I.e setPreviousComponent() actually
     * replaces the previous component before it's pushed onto the stack. In
     * javadoc, this is simplified to ignore implementation details, instead
     * pretending the previous component is topmost on the 'history'.
     */

    private Stack<Component> viewStack = new Stack<Component>();
    private boolean maintainBreadcrumb = true;

    /**
     * Constructs a {@link NavigationManager} that is 100% wide and high.
     */
    public NavigationManager() {
        setSizeFull();
    }

    /**
     * Constructs a {@link NavigationManager} that is 100% wide and high, and
     * initially navigates to (shows) the given component.
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
     * viewStack holds components that are kind of before the previous
     * components. By default NavigatioManager automatically maintains this
     * stack so that it can provide automatic "back navigation". Developers can
     * override components in this stack if they want to manually modify the
     * "breadcrump" or e.g. release previous views for garbage collection.
     * 
     * @see #isMaintainBreadcrumb()
     * 
     * @return components that the navigation manager has saved to be on the
     *         "left side" of previous component.
     */
    public Stack<Component> getViewStack() {
        return viewStack;
    }

    /**
     * Navigates to the given component, effectively making it the new visible
     * component. If the given component is actually the previous component in
     * the history, {@link #navigateBack()} is performed, otherwise the replaced
     * view (previously visible) is pushed onto the history.
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
        requestRepaint();
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
     * Makes the previous component in the history visible, replacing (and
     * essentially forgetting) the component that was previously visible.
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
        requestRepaint();
        fireEvent(new NavigationEvent(this, Direction.BACK));
    }

    /**
     * Sets the currently displayed component in the NavigationManager.
     * <p>
     * If current component is already set it is overridden. If the new
     * component or the next component is of type NavigationView, their previous
     * components will be automatically re-assigned.
     * 
     * @param newcurrentComponent
     */
    public void setCurrentComponent(Component newcurrentComponent) {
        if (getCurrentComponent() != newcurrentComponent) {
            if (getCurrentComponent() != null) {
                removeComponent(getCurrentComponent());
            }
            getState().setCurrentComponent(newcurrentComponent);
            addComponent(newcurrentComponent);
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
            requestRepaint();
        }
    }

    /**
     * Returns the currently visible component.
     * 
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
            requestRepaint();
        }
    }

    /**
     * Gets the previous component from the history.
     * 
     * @return the previous component, or null if n/a
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
        requestRepaint();
    }

    /**
     * Gets the next component, if one is set.
     * 
     * @see #setNextComponent(Component)
     * @return the next component, or null id n/a
     */
    public Component getNextComponent() {
        return (Component) getState().getNextComponent();
    }

    /**
     * This operation is not supported
     */
    public void replaceComponent(Component oldComponent, Component newComponent) {
        throw new UnsupportedOperationException();
    }

    // FIXME
    // @Override
    // public void paintContent(PaintTarget target) throws PaintException {
    // super.paintContent(target);
    // if (currentComponent != null) {
    // target.addAttribute("c", currentComponent);
    // }
    // if (nextComponent != null) {
    // target.addAttribute("n", nextComponent);
    // }
    // if (previousComponent != null) {
    // target.addAttribute("p", previousComponent);
    // }
    // Iterator<Component> componentIterator = getComponentIterator();
    // while (componentIterator.hasNext()) {
    // Component next = componentIterator.next();
    // next.paint(target);
    // }
    // }
    //
    // @Override
    // public void changeVariables(Object source, Map<String, Object> variables)
    // {
    // super.changeVariables(source, variables);
    // Integer navigated = (Integer) variables.get("navigated");
    // if (navigated != null) {
    // if (navigated > 0) {
    // navigateTo(nextComponent);
    // } else {
    // navigateBack();
    // }
    // }
    // }

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

    public static class NavigationEvent extends com.vaadin.ui.Component.Event {

        public enum Direction {
            BACK, FORWARD
        }

        private Direction direction;

        public NavigationEvent(Component source, Direction direction) {
            super(source);
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }

    }

    public interface NavigationListener extends ConnectorEventListener {
        Method METHOD = ReflectTools.findMethod(NavigationListener.class,
                "navigate", NavigationEvent.class);

        public void navigate(NavigationEvent event);
    }

    public void addNavigationListener(NavigationListener listener) {
        addListener(NavigationEvent.class, listener, NavigationListener.METHOD);
    }

    public void removeListener(NavigationListener listener) {
        removeListener(NavigationEvent.class, listener,
                NavigationListener.METHOD);
    }

    /**
     * @return true if NavigationManager should maintain "breadcrumb" of
     *         previous views. The default is true.
     * @see #setMaintainBreadcrumb(boolean)
     */
    public boolean isMaintainBreadcrumb() {
        return maintainBreadcrumb;
    }

    /**
     * Configures whether the NavigationManager maintains "breadcrumb"
     * automatically. This is handy when using NavigationViews to dive into a
     * deep hierarchy.
     * 
     * @param maintainBreadcrumb
     *            true if "breadcrumb" should be maintained
     */
    public void setMaintainBreadcrumb(boolean maintainBreadcrumb) {
        this.maintainBreadcrumb = maintainBreadcrumb;
    }

    public int getComponentCount() {
        return getComponents().size();
    }

    @Override
    public Iterator<Component> iterator() {
        return getComponentIterator();
    }

}
