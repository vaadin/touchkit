package com.vaadin.addon.touchkit.ui;

import com.vaadin.addon.touchkit.gwt.client.vcom.SwipeViewRpc;
import com.vaadin.addon.touchkit.gwt.client.vcom.SwipeViewSharedState;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

/**
 * SwipeView is as simple layout which has scrollable content area. It is meant
 * to be used inside a NavigationManager where it controls its parent on
 * horizontal "swipe gestures" aka horizontal scrolls. Swiping navigates
 * forward/backward.
 * <p>
 * To make the usage fluent, developer is suggested to fill both next and
 * previous component.
 */
public class SwipeView extends CssLayout {

    /**
     * Creates a {@link SwipeView}.
     */
    public SwipeView() {
        setSizeFull();
        registerRpc(new SwipeViewRpc() {
            @Override
            public void setScrollTop(int scrollTop) {
                getState().scrollTop = scrollTop;
            }

            @Override
            public void navigateForward() {
                getNavigationManager().navigateTo(
                        getNavigationManager().getNextComponent());
            }

            @Override
            public void navigateBackward() {
                getNavigationManager().navigateBack();
            }
        });
    }

    @Override
    public SwipeViewSharedState getState() {
        return (SwipeViewSharedState) super.getState();
    }

    /**
     * Creates a {@link SwipeView} with the given caption and an empty
     * {@link CssLayout} as content.
     * 
     * @param caption
     */
    public SwipeView(String caption) {
        this();
        setCaption(caption);
    }

    public void setScrollPosition(int scrollPosition) {
        getState().scrollTop = scrollPosition;
        requestRepaint();
    }

    public int getScrollPosition() {
        return getState().scrollTop;
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
