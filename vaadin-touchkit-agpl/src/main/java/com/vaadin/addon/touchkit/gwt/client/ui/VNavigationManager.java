package com.vaadin.addon.touchkit.gwt.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;

public class VNavigationManager extends ComplexPanel {

    public interface AnimationListener {
        public void animationDidEnd();

        public void animationWillStart();
    }

    class PlaceHolder extends Widget {
        private DivElement el = Document.get().createDivElement();

        public PlaceHolder() {
            setElement(Document.get().createDivElement());
            setStyleName(CLASSNAME + "-placeholder");
            getElement().appendChild(el);
        }

        public void moveToNextPosition() {
            setPosition(getElement().getStyle(), -currentWrapperPos + 1);
        }

        public void setHTML(String innerText) {
            el.setInnerHTML(innerText);
        }

    }

    private static final String CONTAINER_CLASSNAME = "v-touchkit-navpanel-container";
    private static final String WRAPPER_CLASSNAME = "v-touchkit-navpanel-wrapper";
    private static final String CLASSNAME = "v-touchkit-navpanel";
    private Widget currentView;
    private Widget prevView;
    private Widget nextView;
    private DivElement wrapper = Document.get().createDivElement();
    private List<AnimationListener> animationListeners = new ArrayList<AnimationListener>();

    static boolean rerendering = false;

    int currentWrapperPos = 0;

    private PlaceHolder _placeHolder;

    private boolean transitionPending;

    /**
     * Flag to indicate whether ios6 scrolling workaround is needed. See #9754
     */
    private boolean needsIos6ScrollingWorkaround;
    /**
     * Flag used for #9754 workaround. True when previous and next are in
     * correct place for animating/sliding.
     * 
     * @see #needsIos6ScrollingWorkaround
     */
    private boolean preparedForTranslation3d;

    private String width;

    private String pendingWidth;
    private Collection<Widget> detachAfterAnimation = new ArrayList<Widget>();

    public VNavigationManager() {
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
        wrapper.setClassName(WRAPPER_CLASSNAME);
        getElement().appendChild(wrapper);
        hookTransitionEndListener(Css3Propertynames.transitionEnd(),
                wrapper);
        getElement().setTabIndex(-1);
    }

    private void add(Widget child, int pos) {
        Element createContainerElement = createContainerElement();
        add(child, createContainerElement);
        setPosition(child, pos);
    }

    public void addAnimationListener(AnimationListener animationListener) {
        animationListeners.add(animationListener);
    }

    private void animateHorizontally(final int views) {
        animateHorizontally(views, true);
    }

    private void animateHorizontally(final int views, final boolean lockClient) {
        prepareForAnimation();
        if (lockClient) {
            transitionPending = true;
            fireAnimationWillStart();
        }
        currentWrapperPos += views;
        Style style = wrapper.getStyle();
        // ensure animation are "on" (from css), they might not be on due
        // setHorizontalOffset
        style.setProperty(Css3Propertynames.transition(), "");
        setLeftUsingTranslate3d(style, currentWrapperPos);
        if(!BrowserInfo.get().isWebkit()) {
        	// FIXME FF && IE10 don't fire transition end events properly for some reason
        	new Timer() {
				@Override
				public void run() {
					onTransitionEnd();
				}}.schedule(300);
        }
    }

    private Element createContainerElement() {
        DivElement el = Document.get().createDivElement();
        el.setClassName(CONTAINER_CLASSNAME);
        moveAside(Element.as(el));
        wrapper.appendChild(el);
        return el.cast();
    }

    private void fireAnimationDidEnd() {
        for (AnimationListener l : animationListeners) {
            l.animationDidEnd();
        }
    }

    private void fireAnimationWillStart() {
        for (AnimationListener l : animationListeners) {
            l.animationWillStart();
        }
    }

    public Widget getNextView() {
        return nextView;
    }

    private int getPixelWidth() {
        int offsetWidth = getOffsetWidth();
        return offsetWidth;
    }

    private PlaceHolder getPlaceHolder() {
        if (_placeHolder == null) {
            _placeHolder = new PlaceHolder();
            Element container = wrapper.cast();
            add(_placeHolder, container);
        }
        return _placeHolder;
    }

    public Widget getPreviousView() {
        return prevView;
    }

    private void hidePlaceHolder() {
        if (_placeHolder != null) {
            moveAside(_placeHolder.getElement());
        }
    }

    private native void hookTransitionEndListener(String eventName,
            DivElement el)
    /*-{
    	var me = this;
        el.addEventListener(eventName,function(event) {
        	if(event.target == el) {
        	    	$entry(
        	        	me.@com.vaadin.addon.touchkit.gwt.client.ui.VNavigationManager::onTransitionEnd()()
        	        );
        	}
        },false);
    }-*/;

    private void initIosScroollHack() {
        needsIos6ScrollingWorkaround = Navigator.getUserAgent().contains(
                " OS 6_")
                && Navigator.getUserAgent().contains(" afari");
        // Disable hack if "fullscreen", the hack disturbs e.g. SwipeView a
        // LOT as it slows down "warming up" the hardware accelerated layer
        if (needsIos6ScrollingWorkaround
                && getOffsetWidth() == RootPanel.get().getOffsetWidth()) {
            needsIos6ScrollingWorkaround = false;
        }
    }

    private void moveAside(com.google.gwt.dom.client.Element element) {
        element.getStyle().setOpacity(0);
        element.getStyle().setTop(100, Unit.PCT);
    }

    private void moveAside(Widget p) {
        com.google.gwt.dom.client.Element parentElement = (p).getElement()
                .getParentElement();
        moveAside(parentElement);
    }

    public void navigateBackward() {
        animateHorizontally(1);
        if (nextView != null) {
            nextView.removeFromParent();
        }
        nextView = currentView;
        currentView = prevView;
        prevView = null;
    }

    public void navigateForward() {
        animateHorizontally(-1);
        if (prevView != null) {
            prevView.removeFromParent();
        }
        prevView = currentView;
        currentView = nextView;
        nextView = null;
    }

    /**
     * Navigates to a placeholder component that mimics VNavigationView by
     * default. During the animation developers can commonly make a server visit
     * and fetch real content for new view. The given string is used in the
     * placeholder as a caption.
     * 
     * @param placeHolderCaption
     */
    public void navigateToPlaceholder(String placeHolderCaption) {
        preparePlaceHolder(placeHolderCaption);
        animateHorizontally(-1);

        detachAfterAnimation.add(prevView);
        prevView = currentView;
        currentView = null;
    }

    private void onTransitionEnd() {
        new Timer() {
            @Override
            public void run() {
                hidePlaceHolder();
            }
        }.schedule(160);

        transitionPending = false;
        fireAnimationDidEnd();
        if (pendingWidth != null) {
            setWidth(pendingWidth);
            pendingWidth = null;
        }
        for (Widget w : detachAfterAnimation) {
            if (w != null) {
                w.removeFromParent();
            }
        }
        detachAfterAnimation.clear();
        prepareIos6ForScrolling();
    }

    private void prepareForAnimation() {
        // ensure focus is removed from possibly focused fields
        getElement().focus();
        if (needsIos6ScrollingWorkaround && !preparedForTranslation3d) {
            prepareForAnimation(prevView);
            prepareForAnimation(nextView);
            preparedForTranslation3d = true;
        }
    }

    private RegExp regExp3dValues = RegExp.compile("\\(([^,]+)");

    private void prepareForAnimation(Widget p) {
        if (p != null) {
            Style style = p.getElement().getParentElement().getStyle();
            String property = style.getProperty(Css3Propertynames
                    .transform());
            MatchResult exec = regExp3dValues.exec(property);
            style.setProperty(Css3Propertynames.transform(),
                    "translate3d(" + exec.getGroup(1) + ",0,0)");
        }
    }

    private void prepareForScrolling(Widget p) {
        if (p != null) {
            // we'll swift the element to place where even ios 6 webkit don't
            // sink events for it
            Style style = p.getElement().getParentElement().getStyle();
            String property = style.getProperty(Css3Propertynames.transform());
            MatchResult exec = regExp3dValues.exec(property);
            style.setProperty(
                    Css3Propertynames.transform(),
                    "translate3d(" + exec.getGroup(1) + ",-"
                            + Window.getClientHeight() + "px,0)");
        }
    }

    private void prepareIos6ForScrolling() {
        if (needsIos6ScrollingWorkaround && preparedForTranslation3d) {
            prepareForScrolling(prevView);
            prepareForScrolling(nextView);
            preparedForTranslation3d = false;
        }
    }

    private void preparePlaceHolder(String placeholdercaption) {
        getPlaceHolder().setHTML(placeholdercaption);
        getPlaceHolder().moveToNextPosition();
        if (nextView != null) {
            moveAside(nextView);
        }
    }

    @Override
    public boolean remove(Widget w) {
        com.google.gwt.dom.client.Element wrapperElement = w.getElement()
                .getParentElement();
        boolean removed = super.remove(w);
        if (removed) {
            wrapper.removeChild(wrapperElement);
        }
        return removed;
    }

    public void removeAnimationListener(AnimationListener animationListener) {
        animationListeners.remove(animationListener);
    }

    int lastWidth;

    public void resetPositionsAndChildSizes() {
        if (getPixelWidth() == lastWidth) {
            return;
        }
        lastWidth = getPixelWidth();
        // update positions. Not set with percentages as ios safari bugs
        // occasionally with percentages in translate3d.

        /*
         * Disable animation for while.
         */
        wrapper.getStyle().setProperty(Css3Propertynames.transition(), "none");
        currentWrapperPos = 0;
        animateHorizontally(0, false);
        transitionPending = false;
        if (currentView != null) {
            setPosition(currentView, -currentWrapperPos);
            // client.handleComponentRelativeSize((Widget) currentView);
        }
        if (prevView != null) {
            setPosition(prevView, -currentWrapperPos - 1);
            // client.handleComponentRelativeSize((Widget) prevView);
        }
        if (nextView != null) {
            setPosition(nextView, -currentWrapperPos + 1);
            // client.handleComponentRelativeSize((Widget) nextView);
        }
        wrapper.getStyle().setProperty(Css3Propertynames.transition(), "");
        prepareIos6ForScrolling();
    }

    public void setCurrentWidget(Widget w) {
        if (currentView == null) {
            // No currentView => no animation,
            // = placeholder navigation done, or this is the first view
            if (w == nextView) {
                // turns out we're actually going to show the nextView
                // null to avoid problems, we're going to show it w/o animation
                setNextWidget(null);
            } else if (w == prevView) {
                // turns out we're actually going to show the prevView
                // null to avoid problems, we're going to show it w/o animation
                setPreviousWidget(null);
            }

            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    initIosScroollHack();

                }
            });
            add(w, -currentWrapperPos);
            currentView = w;
        } else if (nextView == w) {
            navigateForward();
        } else if (prevView == w) {
            navigateBackward();
        } else {
            if (currentView == w) {
                return;
            }
            // We'll navigate forward since we don't know better -
            // explicit direction should be added to the API
            setNextWidget(w);
            navigateForward();
        }
    }

    public void setHorizontalOffset(int deltaX, boolean animate) {
        final Style style = wrapper.getStyle();
        if (!animate) {
            style.setProperty(Css3Propertynames.transition(), "none");
        }
        prepareForAnimation();

        setPosition(style, currentWrapperPos + deltaX
                / (double) getPixelWidth());
        if (!animate) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                public void execute() {
                    style.setProperty(Css3Propertynames.transition(), "");
                }
            });
        }
    }

    /**
     * @param style
     * @param pos
     *            multiple of panel width
     */
    private void setLeftUsingTranslate3d(Style style, double pos) {
        style.setProperty(Css3Propertynames.transform(),
                "translate3d(" + Math.round((int) (pos * getPixelWidth()))
                        + "px,0,0)");
    }

    public void setNextWidget(Widget w) {
        if (nextView == w) {
            return;
        }
        if (nextView != null) {
            remove(nextView);
        }
        if (w != null) {
            if (w.getParent() != null) {
                throw new RuntimeException("P Component already has a parent "
                        + w.getElement().getId() + " parent"
                        + w.getParent().getElement().getId());
            }
            add(w, -currentWrapperPos + 1);
        }
        nextView = w;
    }

    private void setPosition(Style style, double pos) {
        if (style != null) {
            style.setTop(0, Unit.PCT);
            // style.setLeft(pos * getOffsetWidth(), Unit.PX);
            setLeftUsingTranslate3d(style, pos);
            style.setOpacity(1);
        }
    }

    /**
     * Sets the position of given widget in "widget strip".
     * 
     * @param widget
     *            the widget inside this component whose position is to be
     *            modified
     * @param pos
     *            0 means current, negative are left from current, positive are
     *            on the right
     */
    private void setPosition(Widget widget, int pos) {
        if (widget != null && widget.getParent() == this) {
            setPosition((widget).getElement().getParentElement().getStyle(),
                    pos);
        }
    }

    public void setPreviousWidget(Widget w) {
        if (prevView == w) {
            return;
        }
        if (prevView != null) {
            remove(prevView);
        }
        if (w != null) {
            if (w.getParent() != null) {
                throw new RuntimeException("P Component already has a parent "
                        + w.getElement().getId() + " parent"
                        + w.getParent().getElement().getId());
            }
            add(w, -currentWrapperPos - 1);
        }
        prevView = w;
    }

    private void slideFromLeft() {
        animateHorizontally(1);
    }
}
