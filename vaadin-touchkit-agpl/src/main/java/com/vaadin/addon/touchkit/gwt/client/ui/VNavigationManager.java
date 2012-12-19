package com.vaadin.addon.touchkit.gwt.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.VConsole;

public class VNavigationManager extends ComplexPanel {

    public interface AnimationListener {
        public void animationWillStart();

        public void animationDidEnd();
    }

    private static final String CONTAINER_CLASSNAME = "v-touchkit-navpanel-container";
    private static final String WRAPPER_CLASSNAME = "v-touchkit-navpanel-wrapper";
    private static final String CLASSNAME = "v-touchkit-navpanel";
    private Widget currentView;
    private Widget prevView;
    private Widget nextView;
    private DivElement wrapper = Document.get().createDivElement();
    private List<AnimationListener> animationListeners = new ArrayList<AnimationListener>();

    public VNavigationManager() {
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
        wrapper.setClassName(WRAPPER_CLASSNAME);
        getElement().appendChild(wrapper);
        hookTransitionEndListener(wrapper);
    }

    private native void hookTransitionEndListener(DivElement el)
    /*-{
    	var me = this;
        el.addEventListener("webkitTransitionEnd",function(event) {
        	if(event.target == el) {
        	    	$entry(
        	        	me.@com.vaadin.addon.touchkit.gwt.client.navigation.VNavigationManager::onTransitionEnd()()
        	        );
        	}
        },false);
    }-*/;

    private void onTransitionEnd() {
        VConsole.log("Trs end");

        new Timer() {
            @Override
            public void run() {
                VConsole.log("Place holder hide");
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
    }

    static boolean rerendering = false;

    // TODO: remove this once Android 2.x is no longer relevant and newer
    // versions actually fix the bug where nativeselects don't work on the
    // second+ page in a VNavigationManager.
    public void forceRerender() {
        if (rerendering) {
            return;
        }
        rerendering = true;
        final String oldWidth = width;
        setWidth("100px");
        Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
            public boolean execute() {
                if ("".equals(oldWidth) || oldWidth == null) {
                    setWidth("100%");
                } else {
                    setWidth(oldWidth);
                }
                rerendering = false;
                return false;
            }
        }, 50);
    }

    private void hidePlaceHolder() {
        if (_placeHolder != null) {
            moveAside(_placeHolder.getElement());
        }
    }

    private void slideFromLeft() {
        animateHorizontally(1);
    }

    int currentWrapperPos = 0;
    private PlaceHolder _placeHolder;
    private boolean transitionPending;
    private int lastSizeUsedForWrapper;
    private int lastPixelWidthForPaintable;

    private void animateHorizontally(final int views) {
        animateHorizontally(views, true);
    }

    private void animateHorizontally(final int views, final boolean lockClient) {
        if (lockClient) {
            VConsole.log("Locking client until transition has finished");
            transitionPending = true;
            fireAnimationWillStart();
        }
        currentWrapperPos += views;
        lastSizeUsedForWrapper = getPixelWidth();
        Style style = wrapper.getStyle();
        // ensure animation are "on" (from css), they might not be on due
        // setHorizontalOffset
        style.setProperty("webkitTransition", "");
        setLeftUsingTranslate3d(style, currentWrapperPos);
    }

    /**
     * @param style
     * @param pos
     *            multiple of panel width
     */
    private void setLeftUsingTranslate3d(Style style, double pos) {
        style.setProperty("webkitTransform",
                "translate3d(" + Math.round((int) (pos * getPixelWidth()))
                        + "px,0,0)");
    }

    private int getPixelWidth() {
        int offsetWidth = getOffsetWidth();
        return offsetWidth;
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
            lastPixelWidthForPaintable = getPixelWidth();
        }
    }

    public void setCurrentWidget(Widget w) {
        if (nextView == w) {
            navigateForward();
        } else if (prevView == w) {
            navigateBackward();
        } else {
            // replace current with given
            if (currentView != null) {
                if (currentView == w) {
                    return;
                }
                remove(currentView);
            }
            add(w, -currentWrapperPos);
            currentView = w;
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

    @Override
    public boolean remove(Widget w) {
        VConsole.error("Removing" + w.getElement().getId());
        com.google.gwt.dom.client.Element wrapperElement = w.getElement()
                .getParentElement();
        boolean removed = super.remove(w);
        if (removed) {
            wrapper.removeChild(wrapperElement);
        }
        return removed;
    }

    private void setPosition(Style style, double pos) {
        if (style != null) {
            style.setTop(0, Unit.PCT);
            // style.setLeft(pos * getOffsetWidth(), Unit.PX);
            setLeftUsingTranslate3d(style, pos);
            style.setOpacity(1);
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

    // private UIDL getChildUidl(Paintable p, UIDL uidl) {
    // for (int i = 0; i < uidl.getChildCount(); i++) {
    // UIDL childUIDL = uidl.getChildUIDL(i);
    // Paintable paintable2 = client.getPaintable(childUIDL);
    // if (paintable2 == p) {
    // return childUIDL;
    // }
    // }
    // return null;
    // }

    private Element createContainerElement() {
        DivElement el = Document.get().createDivElement();
        el.setClassName(CONTAINER_CLASSNAME);
        moveAside(Element.as(el));
        wrapper.appendChild(el);
        return el.cast();
    }

    private void add(Widget child, int pos) {
        Element createContainerElement = createContainerElement();
        add(child, createContainerElement);
        setPosition(child, pos);
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

    private void preparePlaceHolder(String placeholdercaption) {
        getPlaceHolder().setHTML(placeholdercaption);
        getPlaceHolder().moveToNextPosition();
        if (nextView != null) {
            moveAside(nextView);
        }
    }

    public void resetPositionsAndChildSizes() {
        VConsole.error("handleChildSizesAndPositions");
        if (lastPixelWidthForPaintable == getPixelWidth()
                && lastSizeUsedForWrapper == getPixelWidth()) {
            VConsole.log("No adjustements needed ");
            return;
        }
        // update positions. Not set with percentages as ios safari bugs
        // occasionally with percentages in translate3d.

        /*
         * Disable animation for while.
         */
        wrapper.getStyle().setProperty("webkitTransition", "none");
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
        wrapper.getStyle().setProperty("webkitTransition", "");
    }

    private String width;
    private String pendingWidth;

    @Override
    public void setWidth(String width) {
        VConsole.log("VNavp" + width);
        if (transitionPending) {
            VConsole.log("transitionPending, postponing width setting");
            pendingWidth = width;
            return;
        }
        if (this.width == null || !this.width.equals(width)) {
            this.width = width;
            super.setWidth(width);
        }
    }

    private PlaceHolder getPlaceHolder() {
        if (_placeHolder == null) {
            _placeHolder = new PlaceHolder();
            Element container = wrapper.cast();
            add(_placeHolder, container);
        }
        return _placeHolder;
    }

    class PlaceHolder extends Widget {
        private DivElement el = Document.get().createDivElement();

        public PlaceHolder() {
            setElement(Document.get().createDivElement());
            setStyleName(CLASSNAME + "-placeholder");
            getElement().appendChild(el);
        }

        public void setHTML(String innerText) {
            el.setInnerHTML(innerText);
        }

        public void moveToNextPosition() {
            setPosition(getElement().getStyle(), -currentWrapperPos + 1);
        }

    }

    public void setHorizontalOffset(int deltaX, boolean animate) {
        final Style style = wrapper.getStyle();
        if (!animate) {
            style.setProperty("webkitTransition", "none");
        }
        setPosition(style, currentWrapperPos + deltaX
                / (double) getPixelWidth());
        if (!animate) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                public void execute() {
                    style.setProperty("webkitTransition", "");
                }
            });
        }
    }

    public Widget getPreviousView() {
        return prevView;
    }

    public Widget getNextView() {
        return nextView;
    }

    private Collection<Widget> detachAfterAnimation = new ArrayList<Widget>();

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

    public void addAnimationListener(AnimationListener animationListener) {
        animationListeners.add(animationListener);
    }

    public void removeAnimationListener(AnimationListener animationListener) {
        animationListeners.remove(animationListener);
    }

    private void fireAnimationWillStart() {
        for (AnimationListener l : animationListeners) {
            l.animationWillStart();
        }
    }

    private void fireAnimationDidEnd() {
        for (AnimationListener l : animationListeners) {
            l.animationDidEnd();
        }
    }
}
