package com.vaadin.addon.touchkit.gwt.client;

import java.util.ArrayList;
import java.util.Set;

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
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ui.VLazyExecutor;

public class VNavigationManager extends ComplexPanel implements Container {

    private static final String CONTAINER_CLASSNAME = "v-touchkit-navpanel-container";
    private static final String WRAPPER_CLASSNAME = "v-touchkit-navpanel-wrapper";
    private static final String CLASSNAME = "v-touchkit-navpanel";
    private ApplicationConnection client;
    private Paintable currentView;
    private Paintable prevView;
    private Paintable nextView;
    private DivElement wrapper = Document.get().createDivElement();
    private VTouchKitApplicationConnection ac;

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
    	        	me.@com.vaadin.addon.touchkit.gwt.client.VNavigationManager::onTransitionEnd()()
    	        );
        	}
        },false);
    }-*/;

    private void onTransitionEnd() {
        VConsole.log("Trs end");
        transitionPending = false;
        if (pendingWidth != null) {
            setWidth(pendingWidth);
            pendingWidth = null;
        }
        ac.resumeRendering(this);
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

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        ac = (VTouchKitApplicationConnection) client;
        rendering = true;
        this.client = client;
        if (client.updateComponent(this, uidl, true)) {
            rendering = false;
            return;
        }

        doUpdate(uidl);

        verifyPositions.trigger();

    }

    private void doUpdate(UIDL uidl) {
        final ArrayList<Widget> orphanedPaintables = new ArrayList<Widget>();
        for (Widget w : getChildren()) {
            if (w instanceof Paintable) {
                orphanedPaintables.add(w);
            }
        }
        /*
         * First render visible component. We'll lazy render others but the next
         * visible.
         */
        int childCount = uidl.getChildCount();
        for (int i = 0; i < childCount; i++) {
            UIDL childUIDL = uidl.getChildUIDL(i);
            Paintable paintable = client.getPaintable(childUIDL);
            orphanedPaintables.remove(paintable);
        }
        final Paintable newNext = uidl.getPaintableAttribute("n", client);
        Paintable newCurrent = uidl.getPaintableAttribute("c", client);
        final Paintable newPrev = uidl.getPaintableAttribute("p", client);
        updatePaintable(newCurrent, getChildUidl(newCurrent, uidl));
        if (newCurrent == currentView) {
            /*
             * already at correct position due to NavigationButtonClick -> no
             * transition.
             */
        } else if (prevView == newCurrent) {
            /*
             * Back navigation, slide right then ensure positions.
             */
            slideFromLeft();
        } else if (currentView == null) {
            /*
             * Placeholder or initial rendering.
             */
            setPosition(newCurrent, -currentWrapperPos);
        } else {
            /*
             * Forward navigation, slide left. First ensure newNext is on the
             * right side.
             */
            slideFromRight(newCurrent);
        }

        final UIDL newNextUidl = getChildUidl(newNext, uidl);
        final UIDL newPrevUidl = getChildUidl(newPrev, uidl);

        currentView = newCurrent;
        nextView = newNext;
        prevView = newPrev;

        /*
         * Detach orphaned components. Must be eagerly done so that orphaned
         * components don't send variables anymore.
         */

        for (Widget widget : orphanedPaintables) {
            com.google.gwt.dom.client.Element wrapperElement = widget
                    .getElement().getParentElement();
            widget.removeFromParent();
            client.unregisterPaintable((Paintable) widget);
            wrapper.removeChild(wrapperElement);
        }

        if (newNext != null) {
            updatePaintable(newNext, newNextUidl);
            setPosition(newNext, -currentWrapperPos + 1);
        }
        if (newPrev != null) {
            updatePaintable(newPrev, newPrevUidl);
            setPosition(newPrev, -currentWrapperPos - 1);
        }

        /**
         * Stylesheet fades in new navigation views in 150ms. After they have
         * become visible, remove placeholder below them.
         */
        new Timer() {
            @Override
            public void run() {
                hidePlaceHolder();
            }
        }.schedule(160);

        rendering = false;

    }

    private void hidePlaceHolder() {
        if (_placeHolder != null) {
            moveAside(_placeHolder.getElement());
        }
    }

    private void slideFromLeft() {
        animateHorizontally(1);
    }

    private void slideFromRight(final Paintable newView) {
        if (nextView != null) {
            moveAside(nextView);
        }
        /*
         * Ensure the position of next component is on the right side of
         * previeous
         */
        setPosition(newView, -currentWrapperPos + 1);
        animateHorizontally(-1);
    }

    int currentWrapperPos = 0;
    private PlaceHolder _placeHolder;
    private boolean transitionPending;
    private boolean rendering;
    private int pixelWidth;
    private int lastSizeUsedForWrapper;
    private int lastPixelWidthForPaintable;

    private void animateHorizontally(final int views) {
        animateHorizontally(views, true);
    }

    private void animateHorizontally(final int views, final boolean lockClient) {
        if (lockClient) {
            VConsole.log("Locking client until transition has finished");
            transitionPending = true;
            ac.suspendRendering(this);
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
        if (pixelWidth < 0) {
            return getOffsetWidth();
        } else {
            return pixelWidth;
        }
    }

    private void setPosition(Paintable newView, int pos) {
        if (newView != null) {
            setPosition(((Widget) newView).getElement().getParentElement()
                    .getStyle(), pos);
            lastPixelWidthForPaintable = getPixelWidth();
        }
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

    private void moveAside(Paintable p) {
        com.google.gwt.dom.client.Element parentElement = ((Widget) p)
                .getElement().getParentElement();
        moveAside(parentElement);
    }

    private UIDL getChildUidl(Paintable p, UIDL uidl) {
        for (int i = 0; i < uidl.getChildCount(); i++) {
            UIDL childUIDL = uidl.getChildUIDL(i);
            Paintable paintable2 = client.getPaintable(childUIDL);
            if (paintable2 == p) {
                return childUIDL;
            }
        }
        return null;
    }

    private void updatePaintable(Paintable paintable, UIDL childUIDL) {
        int widgetIndex = getWidgetIndex((Widget) paintable);
        if (widgetIndex == -1) {
            // new widget, attach
            add((Widget) paintable, createContainerElement());
        }
        paintable.updateFromUIDL(childUIDL, client);

    }

    private Element createContainerElement() {
        DivElement el = Document.get().createDivElement();
        el.setClassName(CONTAINER_CLASSNAME);
        moveAside(Element.as(el));
        wrapper.appendChild(el);
        return el.cast();
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        // TODO Auto-generated method stub

    }

    public boolean hasChildComponent(Widget component) {
        return getChildren().contains(component);
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        // NOP don't support captions
    }

    public boolean requestLayout(Set<Paintable> children) {
        return true;
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        return new RenderSpace(getElement().getClientWidth(), getElement()
                .getClientHeight());
    }

    public void onNaviButtonClick(VNavigationButton vNavigationButton) {
        String nextViewId = vNavigationButton.getNextViewId();
        if (nextViewId != null) {
            Paintable paintable = client.getPaintable(nextViewId);
            if (paintable != null) {
                if (paintable == nextView) {
                    navigateForward(false);
                    return;
                } else if (paintable == prevView) {
                    /*
                     * Back button.
                     */
                    navigateBackward(false);
                    return;
                }
            }
        }
        preparePlaceHolder(vNavigationButton);
        animateHorizontally(-1);
    }

    public void navigateBackward(boolean visitServer) {
        animateHorizontally(1);
        Paintable paintable = prevView;
        nextView = currentView;
        currentView = paintable;
        prevView = null;
        if (visitServer) {
            client.updateVariable(client.getPid(this), "navigated", -1, true);
        }
    }

    public void navigateForward(boolean visitServer) {
        animateHorizontally(-1);
        Paintable paintable = nextView;
        prevView = currentView;
        currentView = paintable;
        if (visitServer) {
            client.updateVariable(client.getPid(this), "navigated", 1, true);
        }
    }

    private void preparePlaceHolder(VNavigationButton vNavigationButton) {
        String innerText = vNavigationButton.getNextViewCaption();
        getPlaceHolder().setHTML(innerText);
        getPlaceHolder().moveToNextPosition();
        if (nextView != null) {
            moveAside(nextView);
        }

        prevView = currentView;
        currentView = null;

    }

    private void resetPositionsAndChildSizes() {
        VConsole.log("handleChildSizesAndPositions");
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
            client.handleComponentRelativeSize((Widget) currentView);
        }
        if (prevView != null) {
            setPosition(prevView, -currentWrapperPos - 1);
            client.handleComponentRelativeSize((Widget) prevView);
        }
        if (nextView != null) {
            setPosition(nextView, -currentWrapperPos + 1);
            client.handleComponentRelativeSize((Widget) nextView);
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
            /*
             * Expect only pixel sizes. Read exact size from the value.
             * getOffsetWidth messes things up in some cases (mobile safari,
             * orientation change).
             */
            if (width.endsWith("px")) {
                double parseDouble = Float.parseFloat(width.substring(0,
                        width.length() - 2));
                pixelWidth = (int) parseDouble;
            }
            if (!rendering) {
                resetPositionsAndChildSizes();
            }
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

    /**
     * TODO check if this can be removed.
     */
    VLazyExecutor verifyPositions = new VLazyExecutor(350,
            new ScheduledCommand() {
                public void execute() {
                    VConsole.log("Verifying positions");
                    resetPositionsAndChildSizes();
                }
            });

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

    public Paintable getPreviousView() {
        return prevView;
    }

    public Paintable getNextView() {
        return nextView;
    }

}
