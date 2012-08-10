package com.vaadin.addon.touchkit.gwt.client.vaadincomm;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.VNavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.shared.ComponentState;
import com.vaadin.shared.communication.SharedState;
import com.vaadin.shared.ui.Connect;
import com.vaadin.terminal.gwt.client.ComponentConnector;
import com.vaadin.terminal.gwt.client.ConnectorHierarchyChangeEvent;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentContainerConnector;
import com.vaadin.terminal.gwt.client.ui.layout.ElementResizeEvent;
import com.vaadin.terminal.gwt.client.ui.layout.ElementResizeListener;

@Connect(NavigationManager.class)
public class VNavigationManagerConnector extends
        AbstractComponentContainerConnector {

    @Override
    protected Widget createWidget() {
        return GWT.create(VNavigationManager.class);
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        super.onConnectorHierarchyChange(event);
        
        Widget current = ((ComponentConnector) getState().getCurrentComponent())
                .getWidget();
        getWidget().setCurrentWidget(current);
        
        Widget previous = null;
        if (getState().getPreviousComponent() != null) {
            previous = ((ComponentConnector) getState().getPreviousComponent())
                    .getWidget();
        }
        Widget next = null;
        if(getState().getNextComponent() != null) {
            next = ((ComponentConnector) getState().getNextComponent()).getWidget();
        }
        getWidget().setPreviousWidget(previous);
        getWidget().setNextWidget(next);

    }

    @Override
    public NavigationManagerSharedState getState() {
        return (NavigationManagerSharedState) super.getState();
    }

    @Override
    public VNavigationManager getWidget() {
        return (VNavigationManager) super.getWidget();
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
        // TODO Auto-generated method stub

    }

    private final ElementResizeListener listener = new ElementResizeListener() {
        public void onElementResize(ElementResizeEvent e) {
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    getWidget().resetPositionsAndChildSizes();
                }
            });
        }
    };

    @Override
    protected void init() {
        super.init();
        getLayoutManager().addElementResizeListener(getWidget().getElement(),
                listener);
    }

    @Override
    public void onUnregister() {
        super.onUnregister();
        getLayoutManager().removeElementResizeListener(
                getWidget().getElement(), listener);
    }

    // private VTouchKitApplicationConnection ac;
    //
    //
    // public void onTransitionEnd() {
    // ac.resumeRendering(this);
    //
    // }
    //
    // public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
    // // ac = (VTouchKitApplicationConnection) client;
    // rendering = true;
    // // this.client = client;
    // if (client.updateComponent(this, uidl, true)) {
    // rendering = false;
    // return;
    // }
    //
    // doUpdate(uidl);
    //
    // verifyPositions.trigger();
    //
    // }
    //
    // private void updatePaintable(Paintable paintable, UIDL childUIDL) {
    // int widgetIndex = getWidgetIndex((Widget) paintable);
    // if (widgetIndex == -1) {
    // // new widget, attach
    // add((Widget) paintable, createContainerElement());
    // }
    // paintable.updateFromUIDL(childUIDL, client);
    //
    // }
    //
    //
    // // client.updateVariable(client.getPid(this), "navigated", -1, true);
    //
    //
    // private void doUpdate(UIDL uidl) {
    // // final ArrayList<Widget> orphanedPaintables = new ArrayList<Widget>();
    // // for (Widget w : getChildren()) {
    // // if (w instanceof Paintable) {
    // // orphanedPaintables.add(w);
    // // }
    // // }
    // // /*
    // // * First render visible component. We'll lazy render others but the
    // next
    // // * visible.
    // // */
    // // int childCount = uidl.getChildCount();
    // // for (int i = 0; i < childCount; i++) {
    // // UIDL childUIDL = uidl.getChildUIDL(i);
    // // Paintable paintable = client.getPaintable(childUIDL);
    // // orphanedPaintables.remove(paintable);
    // // }
    // // final Paintable newNext = uidl.getPaintableAttribute("n", client);
    // // Paintable newCurrent = uidl.getPaintableAttribute("c", client);
    // // final Paintable newPrev = uidl.getPaintableAttribute("p", client);
    // // updatePaintable(newCurrent, getChildUidl(newCurrent, uidl));
    // // if (newCurrent == currentView) {
    // // /*
    // // * already at correct position due to NavigationButtonClick -> no
    // // * transition.
    // // */
    // // } else if (prevView == newCurrent) {
    // // /*
    // // * Back navigation, slide right then ensure positions.
    // // */
    // // slideFromLeft();
    // // } else if (currentView == null) {
    // // /*
    // // * Placeholder or initial rendering.
    // // */
    // // setPosition(newCurrent, -currentWrapperPos);
    // // } else {
    // // /*
    // // * Forward navigation, slide left. First ensure newNext is on the
    // // * right side.
    // // */
    // // slideFromRight(newCurrent);
    // // }
    // //
    // // final UIDL newNextUidl = getChildUidl(newNext, uidl);
    // // final UIDL newPrevUidl = getChildUidl(newPrev, uidl);
    // //
    // // currentView = newCurrent;
    // // nextView = newNext;
    // // prevView = newPrev;
    // //
    // // /*
    // // * Detach orphaned components. Must be eagerly done so that orphaned
    // // * components don't send variables anymore.
    // // */
    // //
    // // for (Widget widget : orphanedPaintables) {
    // // com.google.gwt.dom.client.Element wrapperElement = widget
    // // .getElement().getParentElement();
    // // widget.removeFromParent();
    // // client.unregisterPaintable((Paintable) widget);
    // // wrapper.removeChild(wrapperElement);
    // // }
    // //
    // // if (newNext != null) {
    // // updatePaintable(newNext, newNextUidl);
    // // setPosition(newNext, -currentWrapperPos + 1);
    // // }
    // // if (newPrev != null) {
    // // updatePaintable(newPrev, newPrevUidl);
    // // setPosition(newPrev, -currentWrapperPos - 1);
    // // }
    // //
    // // /**
    // // * Stylesheet fades in new navigation views in 150ms. After they have
    // // * become visible, remove placeholder below them.
    // // */
    // // new Timer() {
    // // @Override
    // // public void run() {
    // // hidePlaceHolder();
    // // }
    // // }.schedule(160);
    // //
    // // rendering = false;
    //
    // }

}
