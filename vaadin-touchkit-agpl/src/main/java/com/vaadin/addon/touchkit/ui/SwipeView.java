package com.vaadin.addon.touchkit.ui;

import java.util.Map;

import com.vaadin.addon.touchkit.gwt.client.VSwipeView;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
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
@ClientWidget(value = VSwipeView.class, loadStyle = LoadStyle.EAGER)
public class SwipeView extends CssLayout {

    private int scrollPosition;

    /**
     * Creates a {@link SwipeView}.
     */
    public SwipeView() {
        setSizeFull();
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

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addVariable(this, "sp", scrollPosition);
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
        requestRepaint();
    }

    public int getScrollPosition() {
        return scrollPosition;
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        Integer newScrollPosition = (Integer) variables.get("sp");
        if (newScrollPosition != null) {
            scrollPosition = newScrollPosition;
        }
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
