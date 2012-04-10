package com.vaadin.addon.touchkit.ui;

import java.util.Map;

import com.vaadin.addon.touchkit.gwt.client.VNavigationButton;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Button;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.Component;

/**
 * A {@link Button} implementation optimized to be used inside a
 * {@link NavigationManager}.
 * <p>
 * Clicking button will automatically navigate to the target view if one is
 * defined in this button (via constructor or {@link #setTargetView(Component)}
 * ). On the client side, the {@link NavigationManager} will start animation
 * immediately when clicked, while the server request/response has finished.
 * <p>
 * Note that navigation will only work when the button is used inside a
 * {@link NavigationManager}, otherwise it will work as a regular {@link Button}.
 */
@ClientWidget(value = VNavigationButton.class, loadStyle = LoadStyle.EAGER)
public class NavigationButton extends Button {

    private Component targetView;

    private String targetViewCaption;

    /**
     * Creates a new navigation button.
     * 
     * @param caption
     *            the Button caption
     */
    public NavigationButton(String caption) {
        super(caption);
    }

    /**
     * Constructs a button with the specified target view, and sets the caption
     * to equal that of the target view.
     * 
     * @param targetView
     *            the view to navigate to when pressed
     */
    public NavigationButton(Component targetView) {
        super(targetView.getCaption());
        this.targetView = targetView;
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
        super(caption);
        this.targetView = targetView;
    }

    /**
     * @see Button#Button()
     */
    public NavigationButton() {
    }

    // @Override
    // protected void fireClick() {
    // /*
    // * TODO remove debug timeout
    // */
    // // try {
    // // Thread.sleep(300);
    // // } catch (InterruptedException e) {
    // // // TODO Auto-generated catch block
    // // e.printStackTrace();
    // // }
    // if (targetView != null) {
    // getNavigationPanel().navigateTo(targetView);
    // }
    // super.fireClick();
    // }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        // FIXME since #6605 click implementation in Button sucks. Fix it then
        // uncomment the previous method or something.
        // super.changeVariables(source, variables);
        if (targetView != null) {
            getNavigationManager().navigateTo(targetView);
        }
        fireClick();
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

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (targetView != null && targetView.getApplication() != null) {
            target.addAttribute("nv", targetView);
        }
        String nvc = getTargetViewCaption();
        if (nvc != null && !nvc.equals(getCaption())) {
            target.addAttribute("nvc", nvc);
        }
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
        this.targetView = targetView;
        requestRepaint();
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
        String caption2 = super.getCaption();
        if (caption2 == null) {
            /*
             * Use caption from target view unless explicitly set for this
             * button
             */
            if (targetView != null && targetView.getCaption() != null) {
                return targetView.getCaption();
            }
        }
        return caption2;
    }

    /**
     * Gets the target view that will be navigated to when the button is
     * pressed.
     * 
     * @return the current target view
     * @see #setTargetView(Component)
     */
    public Component getTargetView() {
        return targetView;
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
        if (targetViewCaption != null) {
            return targetViewCaption;
        }
        if (targetView != null && targetView.getCaption() != null) {
            return targetView.getCaption();
        }
        return getCaption();
    }

    /**
     * Sets the caption that is expected to be on the view this button navigates
     * to.
     * 
     * @param targetViewCaption
     *            the explicit caption of the target view.
     */
    public void setTargetViewCaption(String targetViewCaption) {
        this.targetViewCaption = targetViewCaption;
    }

}
