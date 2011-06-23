package com.vaadin.addons.touchkit.gwt.client;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

public class VNavigationPanel extends ComplexPanel implements Container {

    private static final String CONTAINER_CLASSNAME = "v-tk-navpanel-container";
    private static final String WRAPPER_CLASSNAME = "v-tk-navpanel-wrapper";
    private static final String CLASSNAME = "v-tk-navpanel";
    private ApplicationConnection client;
    private Paintable currentView;
    private Paintable prevView;
    private Paintable nextView;
    private DivElement wrapper = Document.get().createDivElement();
    private UIDL uidl;
    private ScheduledCommand unregisterOrphaned;

    public VNavigationPanel() {
        TouchKitResources.INSTANCE.css().ensureInjected();
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
    	        	me.@com.vaadin.addons.touchkit.gwt.client.VNavigationPanel::onTransitionEnd()()
    	        );
        	}
        },false);
    }-*/;

    private void onTransitionEnd() {
        VConsole.log("Trs end");
        transitionPending = false;
        if (uidl != null) {
            doUpdate();
        }
        if (unregisterOrphaned != null) {
            unregisterOrphaned.execute();
        }
        if (resizeWhenTransitionDone) {
            Scheduler.get().scheduleDeferred(handleChildSizesAndPositions);
        }
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        rendering = true;
        this.client = client;
        if (client.updateComponent(this, uidl, true)) {
            rendering = false;
            return;
        }

        this.uidl = uidl;
        /*
         * Postpone rendering if transition is on to avoid flickering.
         */
        if (!transitionPending) {
            doUpdate();
        } else {
            VConsole.log("Postponing content update to let animation finish.");
        }
    }

    private void doUpdate() {
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
        updatePaintable(newCurrent, getChildUidl(newCurrent));
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

        final UIDL newNextUidl = getChildUidl(newNext);
        final UIDL newPrevUidl = getChildUidl(newPrev);
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            public void execute() {
                VConsole.log("Updating next and previous...");
                if (newNext != null) {
                    updatePaintable(newNext, newNextUidl);
                    setPosition(newNext, -currentWrapperPos + 1);
                }
                if (newPrev != null) {
                    updatePaintable(newPrev, newPrevUidl);
                    setPosition(newPrev, -currentWrapperPos - 1);
                }
                VConsole.log("...done.");
            }
        });

        currentView = newCurrent;
        nextView = newNext;
        prevView = newPrev;

        /*
         * detach orphaned components. In timer, not to remove before transition
         * is complete.
         */

        unregisterOrphaned = new ScheduledCommand() {
            public void execute() {
                for (Widget widget : orphanedPaintables) {
                    com.google.gwt.dom.client.Element wrapperElement = widget
                            .getElement().getParentElement();
                    widget.removeFromParent();
                    client.unregisterPaintable((Paintable) widget);
                    wrapper.removeChild(wrapperElement);
                }
                unregisterOrphaned = null;
            }
        };
        if (!transitionPending) {
            unregisterOrphaned.execute();
        }

        uidl = null;
        rendering = false;

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

    private void animateHorizontally(final int views) {
        transitionPending = true;
        currentWrapperPos += views;
        Style style = wrapper.getStyle();
        setLeftUsingTranslate3d(style, currentWrapperPos);
    }

    /**
     * @param style
     * @param pos
     *            multiple of panel width
     */
    private void setLeftUsingTranslate3d(Style style, int pos) {
        style.setProperty("webkitTransform", "translate3d("
                + (pos * getPixelWidth()) + "px,0,0)");
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
        }
    }

    private void setPosition(Style style, int pos) {
        if (style != null) {
            style.setTop(0, Unit.PCT);
            // style.setLeft(pos * getOffsetWidth(), Unit.PX);
            setLeftUsingTranslate3d(style, pos);
            style.setOpacity(1);
        }
    }

    private void moveAside(com.google.gwt.dom.client.Element element) {
        element.getStyle().setOpacity(0);
    }

    private void moveAside(Paintable p) {
        com.google.gwt.dom.client.Element parentElement = ((Widget) p)
                .getElement().getParentElement();
        moveAside(parentElement);
    }

    private UIDL getChildUidl(Paintable p) {
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
            VConsole.log("Added new component...");
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
                    animateHorizontally(-1);
                    currentView = paintable;
                    return;
                } else if (paintable == prevView) {
                    /*
                     * Back button.
                     */
                    animateHorizontally(1);
                    currentView = paintable;
                    return;
                }
            }
        }
        preparePlaceHolder(vNavigationButton);
        animateHorizontally(-1);
    }

    private void preparePlaceHolder(VNavigationButton vNavigationButton) {
        String innerText = vNavigationButton.getCaption();
        getPlaceHolder().setHTML(innerText);
        getPlaceHolder().moveToNextPosition();
        if (nextView != null) {
            moveAside(nextView);
        }

        prevView = currentView;
        currentView = null;

    }

    ScheduledCommand handleChildSizesAndPositions = new ScheduledCommand() {
        public void execute() {
            VConsole.log("Set width outside render cycle -> resetting view positions");
            // update positions. Not set with percentages as ios safari bugs
            // occasionally with percentages in translate3d.

            /*
             * Disable animation for while.
             */
            wrapper.getStyle().setProperty("webkitTransition", "none");
            currentWrapperPos = 0;
            animateHorizontally(0);
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                public void execute() {
                    wrapper.getStyle().setProperty("webkitTransition", "");
                }
            });
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
            client.runDescendentsLayout(VNavigationPanel.this);
            resizeWhenTransitionDone = false;
        }
    };
    private boolean resizeWhenTransitionDone;
    private String width;

    @Override
    public void setWidth(String width) {
        VConsole.log("VNavp" + width);
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
                if (!transitionPending) {
                    Scheduler.get().scheduleDeferred(
                            handleChildSizesAndPositions);
                } else {
                    resizeWhenTransitionDone = true;
                }
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

}
