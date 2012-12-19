package com.vaadin.addon.touchkit.gwt.client.vcom.popover;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.vaadin.addon.touchkit.gwt.client.ui.VPopover;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.client.ui.window.WindowConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(com.vaadin.addon.touchkit.ui.Popover.class)
public class PopoverConnector extends WindowConnector implements
        NativePreviewHandler {

    PopoverRpc rpc = RpcProxy.create(PopoverRpc.class, this);
    private HandlerRegistration previewHandler;

    public PopoverConnector() {
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        // Related component can be null
        ComponentConnector relatedConnector = (ComponentConnector) getState()
                .getRelatedComponent();
        getWidget().setRelatedComponent(
                relatedConnector == null ? null : relatedConnector.getWidget());
    }

    @Override
    public VPopover getWidget() {
        return (VPopover) super.getWidget();
    }

    @Override
    public PopoverState getState() {
        return (PopoverState) super.getState();
    }

    private final static int ACCEPTEDEVENTS = Event.MOUSEEVENTS;

    @Override
    public void onPreviewNativeEvent(NativePreviewEvent event) {
        EventTarget target = event.getNativeEvent().getEventTarget();
        Element targetElement = null;
        if (Element.is(target)) {
            targetElement = Element.as(target);
        }

        if (getWidget().getModalityCurtain().isOrHasChild(targetElement)
                && getWidget().isClosable()
                && (event.getTypeInt() & ACCEPTEDEVENTS) == 0) {
            /*
             * Close on events outside window. Special handling for mousemove
             * etc to aid compatibility with desktop (testing purposes).
             */
            rpc.close();
        }
    }

    private final ElementResizeListener resizeListener = new ElementResizeListener() {
        private boolean specialPositioningRunning;

        @Override
        public void onElementResize(ElementResizeEvent e) {
            /*
             * FIXME this is currently called twice when the window is opened,
             * from setWidth/height and finally from the super.updateFromUidl
             */
            if (!specialPositioningRunning) {
                specialPositioningRunning = true;
                if (getState().isFullscreen()) {
                    getWidget().setPopupPosition(0, 0);
                } else {
                    /*
                     * fade in the modality curtain unless in fullscreen mode.
                     */
                    com.google.gwt.user.client.Element modalityCurtain = getWidget()
                            .getModalityCurtain();
                    modalityCurtain
                            .removeClassName("v-touchkit-opacity-transition");
                    DOM.sinkEvents(modalityCurtain, Event.TOUCHEVENTS);
                    final Style style = modalityCurtain.getStyle();
                    style.setOpacity(0);
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                        public void execute() {
                            getWidget().getModalityCurtain().addClassName(
                                    "v-touchkit-opacity-transition");
                            /* Final value from the theme */
                            style.setProperty("opacity", "");
                        }
                    });

                    if (getWidget().isSmallScreenDevice()) {
                        getWidget().slideIn();
                    } else if (getState().getRelatedComponent() != null) {
                        getWidget().showNextTo(
                                ((ComponentConnector) getState()
                                        .getRelatedComponent()).getWidget());
                    }
                }
                specialPositioningRunning = false;
            }
        }
    };

    @Override
    protected void init() {
        super.init();

        previewHandler = Event.addNativePreviewHandler(this);
        getLayoutManager().addElementResizeListener(getWidget().getElement(),
                resizeListener);
    }

    @Override
    public void onUnregister() {
        super.onUnregister();

        previewHandler.removeHandler();
        getLayoutManager().removeElementResizeListener(
                getWidget().getElement(), resizeListener);
    }

    @Override
    protected void setWidgetStyleName(String styleName, boolean add) {
        if (getState().isFullscreen()) {
            // fullscreen window
            super.setWidgetStyleName(
                    "v-touchkit-popover v-touchkit-fullscreen", true);
            getWidget().getModalityCurtain().addClassName("fullscreen");
        } else if (getState().getRelatedComponent() != null) {
            // real popover (black)
            super.setWidgetStyleName("v-touchkit-popover v-touchkit-relative",
                    true);
            getWidget().getModalityCurtain().addClassName("relative");
        } else {
            // regular (white)
            super.setWidgetStyleName("v-touchkit-popover v-touchkit-plain",
                    true);
        }
        if (VPopover.isSmallScreenDevice()) {
            super.setWidgetStyleName("v-touchkit-smallscreen", true);
        }
        super.setWidgetStyleName(styleName, add);
    }

}
