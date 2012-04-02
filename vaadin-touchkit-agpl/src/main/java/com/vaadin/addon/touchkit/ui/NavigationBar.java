package com.vaadin.addon.touchkit.ui;

import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.addon.touchkit.gwt.client.VNavigationBar;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.Component;

/**
 * A navigation bar that contains an XHTML caption in the middle and component
 * areas (most commonly for buttons) on the left and right. A back-button is
 * automatically shown if a <code>previousView</code> is available.
 * <p>
 * Ususally used in a {@link NavigationView}.
 * </p>
 * 
 * @see NavigationView
 */
@ClientWidget(value = VNavigationBar.class, loadStyle = LoadStyle.EAGER)
public class NavigationBar extends AbstractComponentContainer {

    private static final String STYLE_NAME_BACK_BUTTON = "back";
    private NavigationButton backButton = new NavigationButton();
    private Component navigationBarComponent;
    private Component leftNavigationBarComponent;

    public NavigationBar() {
        backButton.setVisible(false);
        backButton.setStyleName(STYLE_NAME_BACK_BUTTON);
        setLeftComponent(backButton);
    }

    /**
     * Sets the component on the left side of the caption.
     * 
     * <p>
     * This place is most commonly reserved for the back button. In case the
     * {@link #setPreviousView(Component)} method is used, it replaces existing
     * components from this location.
     * 
     * @param c
     */
    public void setLeftComponent(Component c) {
        if (leftNavigationBarComponent != null) {
            super.removeComponent(leftNavigationBarComponent);
        }
        if (c != null) {
            super.addComponent(c);
        }
        leftNavigationBarComponent = c;
        requestRepaint();
    }

    /**
     * @return the component on the left side of the caption or null if the
     *         component is not set
     */
    public Component getLeftComponent() {
        return leftNavigationBarComponent;
    }

    /**
     * Sets the component on the right side of the caption.
     * 
     * @param c
     */
    public void setRightComponent(Component c) {
        if (navigationBarComponent != null) {
            super.removeComponent(navigationBarComponent);
        }
        if (c != null) {
            super.addComponent(c);
        }
        navigationBarComponent = c;
        requestRepaint();
    }

    /**
     * @return the component on right side of the caption or null if not set
     */
    public Component getRightComponent() {
        return navigationBarComponent;
    }

    /**
     * Sets which view (component) will be navigated to when the back-button is
     * pressed.
     * 
     * @param component
     */
    public void setPreviousView(Component component) {
        if (getBackButton().getParent() != null) {
            // only if the left component has not been changed
            getBackButton().setTargetView(component);
            getBackButton().setVisible(component != null);
        }
    }

    /**
     * Get the previousView that will be navigated to when the back-button is
     * pressed.
     * 
     * @return the previousView or null if n/a
     * @see #setPreviousView(Component)
     */
    public Component getPreviousView() {
        return getBackButton().getTargetView();
    }

    private NavigationButton getBackButton() {
        return backButton;
    }

    /**
     * Not supported by NavigationBar.
     * 
     * @deprecated
     */
    @Deprecated
    @Override
    public void addComponent(Component c) {
        throw new UnsupportedOperationException(
                "Navigation bar does not support general container mutation methods. Use setRightComponent, setCaption and setPreviousVew methods to control the componen.");
    }

    /**
     * Not supported by NavigationBar.
     * 
     * @deprecated
     */
    @Deprecated
    public void replaceComponent(Component oldComponent, Component newComponent) {
        throw new UnsupportedOperationException(
                "Navigation bar does not support general container mutation methods. Use setRightComponent, setCaption and setPreviousVew methods to control the componen.");
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (leftNavigationBarComponent != null) {
            target.startTag("back");
            leftNavigationBarComponent.paint(target);
            target.endTag("back");
        }

        if (navigationBarComponent != null) {
            target.startTag("component");
            navigationBarComponent.paint(target);
            target.endTag("component");
        }

    }

    public Iterator<Component> getComponentIterator() {
        LinkedList<Component> components = new LinkedList<Component>();
        components.add(backButton);
        if (navigationBarComponent != null) {
            components.add(navigationBarComponent);
        }
        return components.iterator();
    }

}
