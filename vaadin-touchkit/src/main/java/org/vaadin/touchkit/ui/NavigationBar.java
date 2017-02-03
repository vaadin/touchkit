package org.vaadin.touchkit.ui;

import java.util.Iterator;
import java.util.LinkedList;

import org.vaadin.touchkit.gwt.client.vcom.navigation.NavigationBarState;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;

/**
 * The NavigationBar component sits at the top of a {@link NavigationView} and
 * contains an XHTML caption in the middle as well as component slots (most
 * commonly for buttons) on the left and right. A back-button is automatically
 * shown if a <code>previousView</code> is available.
 * <p>
 * Commonly used in a {@link NavigationView}.
 * </p>
 * 
 * @see NavigationView
 */
public class NavigationBar extends AbstractComponentContainer {

    private static final String STYLE_NAME_BACK_BUTTON = "back";
    private NavigationButton backButton = new NavigationButton();
    private Component navigationBarComponent;
    private Component leftNavigationBarComponent;

    /**
     * Constructs a new NavigationBar
     */
    public NavigationBar() {
        backButton.setVisible(false);
        backButton.setStyleName(STYLE_NAME_BACK_BUTTON);
        setLeftComponent(backButton);
    }

    @Override
    public NavigationBarState getState() {
        return (NavigationBarState) super.getState();
    }

    /**
     * Sets the component in the left slot. This place most commonly features
     * the back button. Setting the left component replaces any existing
     * components, including the back button.
     * 
     * @param c
     *            the component to put in the left slot.
     */
    public void setLeftComponent(Component c) {
        if (leftNavigationBarComponent != null) {
            super.removeComponent(leftNavigationBarComponent);
        }
        if (c != null) {
            super.addComponent(c);
        }
        getState().setLeftComponent(c);
        leftNavigationBarComponent = c;
        markAsDirty();

    }

    /**
     * @return the component in the left slot or null if the none is set
     */
    public Component getLeftComponent() {
        return leftNavigationBarComponent;
    }

    /**
     * Sets the component in the right slot.
     * 
     * @param c
     *            the component to put in the right slot.
     */
    public void setRightComponent(Component c) {
        if (navigationBarComponent != null) {
            super.removeComponent(navigationBarComponent);
        }
        if (c != null) {
            super.addComponent(c);
        }
        getState().setRightComponent(c);
        navigationBarComponent = c;
        markAsDirty();
    }

    /**
     * @return the component in the right slot or null if none set
     */
    public Component getRightComponent() {
        return navigationBarComponent;
    }

    /**
     * Sets which view (component) will be navigated to when the back button is
     * pressed.
     * 
     * @param component
     *            the previous view
     */
    public void setPreviousView(Component component) {
        if (getBackButton().getParent() != null) {
            // only if the left component has not been changed
            getBackButton().setTargetView(component);
            getBackButton().setVisible(component != null);
        }
    }

    /**
     * Gets the previousView that will be navigated to when the back button is
     * pressed.
     * 
     * @return the previousView or null if none set
     * @see #setPreviousView(Component)
     */
    public Component getPreviousView() {
        return getBackButton().getTargetView();
    }

    /**
     * @return the back button
     */
    private NavigationButton getBackButton() {
        return backButton;
    }

    /**
     * @deprecated Not supported by NavigationBar.
     * @throws UnsupportedOperationException
     */
    @Deprecated
    @Override
    public void addComponent(Component c) {
        throw new UnsupportedOperationException(
                "Navigation bar does not support general container mutation methods. Use setRightComponent, setCaption and setPreviousVew methods to control the componen.");
    }

    /**
     * @deprecated Not supported by NavigationBar.
     * @throws UnsupportedOperationException
     */
    @Deprecated
    public void replaceComponent(Component oldComponent, Component newComponent) {
        throw new UnsupportedOperationException(
                "Navigation bar does not support general container mutation methods. Use setRightComponent, setCaption and setPreviousVew methods to control the componen.");
    }

    public Iterator<Component> getComponentIterator() {
        LinkedList<Component> components = new LinkedList<Component>();
        components.add(leftNavigationBarComponent);
        if (navigationBarComponent != null) {
            components.add(navigationBarComponent);
        }
        return components.iterator();
    }

    @Override
    public Iterator<Component> iterator() {
        return getComponentIterator();
    }

    @Override
    public int getComponentCount() {
        int count = 0;
        if (backButton != null) {
            count++;
        }
        if (navigationBarComponent != null) {
            count++;
        }
        return count;
    }
}
