package com.vaadin.addon.touchkit.ui;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.vaadin.addon.touchkit.gwt.client.vcom.navigation.NavigationButtonRpc;
import com.vaadin.addon.touchkit.gwt.client.vcom.navigation.NavigationButtonSharedState;
import com.vaadin.shared.Connector;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents.ComponentAttachEvent;
import com.vaadin.ui.HasComponents.ComponentAttachListener;
import com.vaadin.util.ReflectTools;

/**
 * The NavigationButton is a Button implementation optimized to be used inside a
 * {@link NavigationManager} or generally in touch devices.
 * <p>
 * Clicking button will automatically navigate to the target view, if defined in
 * this button (via constructor or {@link #setTargetView(Component)} ). On the
 * client side, the {@link NavigationManager} will start animation immediately
 * when clicked, while the client is waiting for a response from the server.
 * <p>
 * If the button does not have a target view, {@link NavigationButton} will
 * still cause a immediate client-side animation, and in this case you MUST make
 * sure to navigate to a view in the {@link NavigationButtonClickListener},
 * otherwise the user will be stuck on an empty view.
 * <p>
 * Note that navigation will only work when the button is used inside a
 * {@link NavigationManager}, otherwise it will work as a regular {@link Button}.
 */
public class NavigationButton extends AbstractComponent {

    private ComponentAttachListener componentAttachListener;

    /**
     * Constructs a new NavigationButton with the given caption.
     * <p>
     * NOTE that if you do not specify a target view later with
     * {@link #setTargetView(Component)}, OR navigate to a view when the button
     * is clicked (in a {@link NavigationButtonClickListener}, the user will be
     * stuck on a empty view.
     * 
     * @param caption
     *            the caption
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
     *            the caption
     * @param targetView
     *            the view to navigate to when pressed
     */
    public NavigationButton(String caption, Component targetView) {
        this(caption);
        setTargetView(targetView);
    }

    /**
     * Creates a navigation button without any caption nor target view.
     * <p>
     * NOTE that if you do not specify a target view later with
     * {@link #setTargetView(Component)}, OR navigate to a view when the button
     * is clicked (in a {@link NavigationButtonClickListener}, the user will be
     * stuck on a empty view.
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
    public NavigationButtonSharedState getState() {
        return (NavigationButtonSharedState) super.getState();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        // Steal caption from target view if not explicitly defined
        String caption = getState().caption;
        AbstractComponent targetView = (AbstractComponent) getState()
                .getTargetView();
        String targetViewCaption = getState().getTargetViewCaption();
        if (caption == null) {
            caption = targetViewCaption;
            if (caption == null && targetView != null) {
                caption = targetView.getCaption();
            }
        }
        getState().caption = caption;

        if (getState().getTargetViewCaption() == null) {
            if (targetView == null) {
                getState().setTargetViewCaption(caption);
            }
        }

    }

    /**
     * Gets the {@link NavigationManager} in which this button is contained.
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
     * The client side widget communicates directly with a
     * {@link NavigationManager} to make the actual navigation work.
     * </p>
     * 
     * @param targetView
     *            The view to navigate to when pressed.
     */
    public void setTargetView(Component targetView) {
        getState().setTargetView(targetView);
        markAsDirty();

    }

    @Override
    public void attach() {
        super.attach();
        NavigationManager navigationManager = getNavigationManager();
        if (navigationManager != null) {
            navigationManager
                    .addComponentAttachListener(getComponentAttachListener());
        }
    }

    @Override
    public void detach() {
        if (componentAttachListener != null) {
            getNavigationManager().removeComponentAttachListener(
                    componentAttachListener);
        }
        super.detach();
    }

    private ComponentAttachListener getComponentAttachListener() {
        if (componentAttachListener == null) {
            componentAttachListener = new ComponentAttachListener() {
                @Override
                public void componentAttachedToContainer(
                        ComponentAttachEvent event) {
                    Component attachedComponent = event.getAttachedComponent();
                    if (getTargetView() == attachedComponent) {
                        markAsDirty();
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
     * 
     * @return the caption
     */
    @Override
    public String getCaption() {
        return super.getCaption();
    }

    /**
     * Gets the target view that will be navigated to when the button is
     * pressed.
     * 
     * @return the target view
     * @see #setTargetView(Component)
     */
    public Component getTargetView() {
        return (Component) getState().getTargetView();
    }

    /**
     * Returns the caption that is expected to be on the view this button
     * navigates to. Used for the placeholder, which is visible before the
     * client has a chance to render the real target view.
     * <p>
     * If the caption is not set explicitly with
     * {@link #setTargetViewCaption(String)}, the caption of target view is
     * used. In case neither explicit target view caption or the target view is
     * not defined the button caption is used.
     * 
     * @return the caption that will be used for the placeholder of the target
     *         view.
     */
    public String getTargetViewCaption() {
        return getState().getTargetViewCaption();
    }

    /**
     * Sets the caption that is expected to be on the view this button navigates
     * to. This is used for the placeholder, which is visible before the client
     * has a chance to render the real target view.
     * 
     * @param targetViewCaption
     *            the explicit caption of the target view.
     */
    public void setTargetViewCaption(String targetViewCaption) {
        getState().setTargetViewCaption(targetViewCaption);
        markAsDirty();
    }

    /**
     * Click event. This event is triggered, when the navigation button is
     * clicked.
     */
    public class NavigationButtonClickEvent extends Component.Event {

        /**
         * Constructs a new NavigationButtonClickEvent
         * 
         * @param source
         *            the Source of the event.
         */
        public NavigationButtonClickEvent(Component source) {
            super(source);
        }

    }

    /**
     * Interface for listening for a {@link NavigationButtonClickEvent} fired by
     * a {@link Component}.
     */
    public interface NavigationButtonClickListener extends Serializable {

        public static final Method BUTTON_CLICK_METHOD = ReflectTools
                .findMethod(NavigationButtonClickListener.class, "buttonClick",
                        NavigationButtonClickEvent.class);

        /**
         * Called when a {@link NavigationButton} has been clicked. A reference
         * to the button is given by
         * {@link NavigationButtonClickEvent#getSource()}.
         * 
         * @param event
         *            An event containing information about the click.
         */
        public void buttonClick(NavigationButtonClickEvent event);

    }

    /**
     * Adds a navigation button click listener.
     * 
     * @param listener
     *            the Listener to add.
     */
    public void addClickListener(NavigationButtonClickListener listener) {
        addListener(NavigationButtonClickEvent.class, listener,
                NavigationButtonClickListener.BUTTON_CLICK_METHOD);
    }

    /**
     * Removes a navigation button click listener.
     * 
     * @param listener
     *            the Listener to remove.
     */
    public void removeClickListener(NavigationButtonClickListener listener) {
        removeListener(NavigationButtonClickEvent.class, listener,
                NavigationButtonClickListener.BUTTON_CLICK_METHOD);
    }

    /**
     * Simulates a button click, notifying all server-side listeners.
     * 
     * No action is taken if the button is disabled.
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
     * In subclasses, override {@link #fireEvent(java.util.EventObject)} instead
     * of this method.
     */
    protected void fireClick() {
        fireEvent(new NavigationButtonClickEvent(this));
    }

    /**
     * Description in NavigationButton is show on the right side of the button.
     * Normally with bit smaller and gray text.
     * 
     * @see com.vaadin.addon.touchkit.gwt.client.theme.StyleNames#NAVIGATION_BUTTON_DESC_PILL
     * @see com.vaadin.ui.AbstractComponent#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

}
