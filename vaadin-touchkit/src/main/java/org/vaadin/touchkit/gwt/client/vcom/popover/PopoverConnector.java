package org.vaadin.touchkit.gwt.client.vcom.popover;

import org.vaadin.touchkit.gwt.client.ui.VPopover;

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
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.client.ui.window.WindowConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(org.vaadin.touchkit.ui.Popover.class)
public class PopoverConnector extends WindowConnector implements
        NativePreviewHandler {

    PopoverRpc rpc = RpcProxy.create(PopoverRpc.class, this);
    private HandlerRegistration previewHandler;

    public PopoverConnector() {
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        // Related component can be null
        ComponentConnector relatedConnector = (ComponentConnector) getState()
                .getRelatedComponent();
        getWidget().setRelatedComponent(
                relatedConnector == null ? null : relatedConnector.getWidget());
        super.onStateChanged(stateChangeEvent);
    }

    @Override
    protected void updateComponentSize() {
        super.updateComponentSize();

        if (getState().isFullscreen()) {
            getWidget().setPopupPosition(0, 0);
        } else {
            /*
             * fade in the modality curtain unless in fullscreen mode.
             */
            com.google.gwt.user.client.Element modalityCurtain = getWidget()
                    .getModalityCurtain();
            modalityCurtain.removeClassName("v-touchkit-opacity-transition");
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

            if (VPopover.isSmallScreenDevice()) {
                getWidget().slideIn();
            } else if (getState().getRelatedComponent() != null) {
                getWidget().showNextTo(
                        ((ComponentConnector) getState().getRelatedComponent())
                                .getWidget());
            }
        }

        updateWidgetStyleNames();
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
        @Override
        public void onElementResize(ElementResizeEvent e) {
            updateComponentSize();
        }
    };

    @Override
    protected void init() {
        super.init();

        previewHandler = Event.addNativePreviewHandler(this);
        getLayoutManager().addElementResizeListener(getWidget().getElement(),
                resizeListener);
        getLayoutManager().addElementResizeListener(
                getConnection().getUIConnector().getWidget().getElement(),
                resizeListener);
    }

    @Override
    public void onUnregister() {
        super.onUnregister();

        previewHandler.removeHandler();
        getLayoutManager().removeElementResizeListener(
                getWidget().getElement(), resizeListener);
        getLayoutManager().removeElementResizeListener(
                getConnection().getUIConnector().getWidget().getElement(),
                resizeListener);
    }

    @Override
    protected void updateWidgetStyleNames() {
        boolean fullscreen = getState().isFullscreen();
        boolean relative = getState().getRelatedComponent() != null;
        boolean smallScreen = VPopover.isSmallScreenDevice();

        setWidgetStyleName("v-touchkit-popover", true);
        setWidgetStyleName("v-touchkit-fullscreen", fullscreen);
        setWidgetStyleName("v-touchkit-relative", !fullscreen && relative);
        setWidgetStyleName("v-touchkit-plain", !fullscreen && !relative);
        setWidgetStyleName("v-touchkit-smallscreen", smallScreen);

        setModalityCurtainStyleName("fullscreen", fullscreen);
        setModalityCurtainStyleName("relative", !fullscreen && relative);

        super.updateWidgetStyleNames();
    }

    private void setModalityCurtainStyleName(String styleName, boolean add) {
        if (add) {
            getWidget().getModalityCurtain().addClassName(styleName);
        } else {
            getWidget().getModalityCurtain().removeClassName(styleName);
        }
    }
}
