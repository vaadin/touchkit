package com.vaadin.addon.touchkit.ui;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.vaadin.addon.touchkit.gwt.client.navigation.NavigationButtonRpc;
import com.vaadin.addon.touchkit.gwt.client.navigation.NavigationButtonSharedState;
import com.vaadin.shared.Connector;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer.ComponentAttachEvent;
import com.vaadin.ui.ComponentContainer.ComponentAttachListener;

/**
 * A Button implementation optimized to be used inside a
 * {@link NavigationManager} or generally in touch devices.
 * <p>
 * Clicking button will automatically navigate to the target view if one is
 * defined in this button (via constructor or {@link #setTargetView(Component)}
 * ). On the client side, the {@link NavigationManager} will start animation
 * immediately when clicked, while the server request/response has finished.
 * <p>
 * Note that navigation will only work when the button is used inside a
 * {@link NavigationManager}, otherwise it will work as a regular {@link Button}.
 */
public class NavigationButton extends AbstractComponent {
    
    private ComponentAttachListener componentAttachListener;

    /**
     * Creates a new navigation button.
     * 
     * @param caption
     *            the Button caption
     */
    public NavigationButton(String caption) {
        this();
        setCaption(caption);
    }

    /**
     * Constructs a button with the specified target view, and sets the caption
     * to equal that of the target view.
     * 
     * @param targetView
     *            the view to navigate to when pressed
     */
    public NavigationButton(Component targetView) {
        this(targetView.getCaption());
        setTargetView(targetView);
    }

    /**
     * Constructs a button with the specified target view, and sets the caption
     * explicitly.
     * 
     * @param caption
     *            the Button caption
     * @param targetView
     *            the view to navigate to when pressed
     */
    public NavigationButton(String caption, Component targetView) {
        this(caption);
        setTargetView(targetView);
    }

    /**
     * Creates a navigation button without caption nor target view.
     */
    public NavigationButton() {
        registerRpc(new NavigationButtonRpc() {
            @Override
            public void click() {
                NavigationButton.this.click();
            }
        });
    }
    
    @Override
    public void updateState() {
        super.updateState();
    }

    @Override
    public NavigationButtonSharedState getState() {
        return (NavigationButtonSharedState) super.getState();
    }
    
    /**
     * Gets the @link {@link NavigationManager} in which this button is
     * contained.
     * 
     * @return the {@link NavigationManager} or null if not inside one
     */
    public NavigationManager getNavigationManager() {
        Component p = getParent();
        while (p != null && !(p instanceof NavigationManager)) {
            p = p.getParent();
        }
        return (NavigationManager) p;
    }

    /**
     * Sets the view that will be navigated to when the button is pressed.
     * <p>
     * The button communicates directly with a {@link NavigationManager} to make
     * the actual navigation work.
     * </p>
     * 
     * @param targetView
     */
    public void setTargetView(Component targetView) {
        getState().setTargetView(targetView);
        requestRepaint();
    }
    
    @Override
    public void attach() {
        super.attach();
        NavigationManager navigationManager = getNavigationManager();
        if(navigationManager != null) {
            navigationManager.addListener(getComponentAttachListener());
        }
    }
    
    @Override
    public void detach() {
        if(componentAttachListener != null) {
            getNavigationManager().removeListener(componentAttachListener);
        }
        super.detach();
    }
    
    private ComponentAttachListener getComponentAttachListener() {
        if(componentAttachListener == null) {
            componentAttachListener = new ComponentAttachListener() {
                @Override
                public void componentAttachedToContainer(ComponentAttachEvent event) {
                    Component attachedComponent = event.getAttachedComponent();
                    if(getTargetView() == attachedComponent) {
                        requestRepaint();
                    }
                }
            };
        }
        return componentAttachListener;
    }

    /**
     * Gets the caption for this button.
     * <p>
     * If the caption is explicitly set, it will be used - otherwise the caption
     * is fetched from the target view, if one is set.
     * </p>
     * 
     * @see com.vaadin.ui.AbstractComponent#getCaption()
     */
    @Override
    public String getCaption() {
        return super.getCaption();
    }

    /**
     * Gets the target view that will be navigated to when the button is
     * pressed.
     * 
     * @return the current target view
     * @see #setTargetView(Component)
     */
    public Component getTargetView() {
        return (Component) getState().getTargetView();
    }

    /**
     * Returns the caption that is expected to be on the view this button
     * navigates to.
     * <p>
     * If the caption is not set explicitly with
     * {@link #setTargetViewCaption(String)}, the caption of target view is
     * used. In case neither explicit target view caption or the target view is
     * not defined the button caption is used.
     * 
     * @return the caption that will be used for placeholder of the target view.
     */
    public String getTargetViewCaption() {
        return getState().getTargetViewCaption();
    }

    /**
     * Sets the caption that is expected to be on the view this button navigates
     * to.
     * 
     * @param targetViewCaption
     *            the explicit caption of the target view.
     */
    public void setTargetViewCaption(String targetViewCaption) {
        getState().setTargetViewCaption(targetViewCaption);
        requestRepaint();
    }
    
    /**
     * Click event. This event is thrown, when the button is clicked.
     */
    public class NavigationButtonClickEvent extends Component.Event {

        /**
         * New instance of text change event.
         * 
         * @param source
         *            the Source of the event.
         */
        public NavigationButtonClickEvent(Component source) {
            super(source);
        }

    }

    /**
     * Interface for listening for a {@link NavigationButtonClickEvent} fired by a
     * {@link Component}.
     */
    public interface NavigationButtonClickListener extends Serializable {

        public static final Method BUTTON_CLICK_METHOD = ReflectTools
                .findMethod(NavigationButtonClickListener.class, "buttonClick",
                        NavigationButtonClickEvent.class);

        /**
         * Called when a {@link Button} has been clicked. A reference to the
         * button is given by {@link NavigationButtonClickEvent#getButton()}.
         * 
         * @param event
         *            An event containing information about the click.
         */
        public void buttonClick(NavigationButtonClickEvent event);

    }

    /**
     * Adds the button click listener.
     * 
     * @param listener
     *            the Listener to be added.
     */
    public void addClickListener(NavigationButtonClickListener listener) {
        addListener(NavigationButtonClickEvent.class, listener,
                NavigationButtonClickListener.BUTTON_CLICK_METHOD);
    }

    /**
     * Removes the button click listener.
     * 
     * @param listener
     *            the Listener to be removed.
     */
    public void removeClickListener(NavigationButtonClickListener listener) {
        removeListener(NavigationButtonClickEvent.class, listener,
                NavigationButtonClickListener.BUTTON_CLICK_METHOD);
    }

    /**
     * Simulates a button click, notifying all server-side listeners.
     * 
     * No action is taken is the button is disabled.
     */
    public void click() {
        if (isEnabled() && !isReadOnly()) {
            Connector targetView = getState().getTargetView();
            
            if (targetView != null) {
                getNavigationManager().navigateTo((Component) targetView);
            }

            fireClick();
        }
    }

    /**
     * Fires a click event to all listeners without any event details.
     * 
     * In subclasses, override {@link #fireClick(MouseEventDetails)} instead of
     * this method.
     */
    protected void fireClick() {
        fireEvent(new NavigationButtonClickEvent(this));
    }

}
