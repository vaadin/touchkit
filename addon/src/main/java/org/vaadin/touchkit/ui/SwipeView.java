package org.vaadin.touchkit.ui;

import org.vaadin.touchkit.gwt.client.vcom.SwipeViewRpc;
import org.vaadin.touchkit.gwt.client.vcom.SwipeViewSharedState;

import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.Component;

/**
 * The SwipeView is a simple layout which has a scrollable content area. It is
 * meant to be used inside a {@link NavigationManager} where it controls its
 * parent on horizontal swipe gestures aka horizontal scrolls. Swiping navigates
 * forward/backward in the NavigationManager.
 * <p>
 * To make usage as fluent as possible, it is suggested to set both next and
 * previous component in the NavigationManager. This can be done using a
 * {@link org.vaadin.touchkit.ui.NavigationManager.NavigationListener}.
 */
public class SwipeView extends AbstractSingleComponentContainer {

    /**
     * Constructs a SwipeView.
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
     * Constructs a SwipeView with the given caption.
     * 
     * @param caption
     *            the caption
     */
    public SwipeView(String caption) {
        this();
        setCaption(caption);
    }

    /**
     * Constructs a SwipeView with the given {@link Component} as its content.
     * 
     * @param content the component to be wrapped in SwipeView
     */
    public SwipeView(Component content) {
        setContent(content);
    }

    /**
     * Sets the vertical scroll position
     * 
     * @param scrollPosition
     *            the vertical scroll position (y-coordinate)
     */
    public void setScrollPosition(int scrollPosition) {
        getState().scrollTop = scrollPosition;
        markAsDirty();
    }

    /**
     * @return the vertical scroll position.
     */
    public int getScrollPosition() {
        return getState().scrollTop;
    }

    /**
     * @return the parent {@link NavigationManager} or null if not inside one
     */
    public NavigationManager getNavigationManager() {
        Component p = getParent();
        if (p instanceof NavigationManager) {
            return (NavigationManager) p;
        }
        return null;
    }

}
