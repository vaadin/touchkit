package org.vaadin.touchkit.gwt.client.vcom;

import org.vaadin.touchkit.gwt.client.ui.VSwitch;
import org.vaadin.touchkit.ui.Switch;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.EventHelper;
import com.vaadin.client.VTooltip;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.v7.client.ui.AbstractFieldConnector;
import com.vaadin.shared.communication.FieldRpc.FocusAndBlurServerRpc;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.shared.ui.checkbox.CheckBoxServerRpc;
import com.vaadin.v7.shared.ui.checkbox.CheckBoxState;

@Connect(Switch.class)
public class SwitchConnector extends AbstractFieldConnector implements
        FocusHandler, BlurHandler {

    private transient HandlerRegistration focusHandlerRegistration;
    private transient HandlerRegistration blurHandlerRegistration;

    private CheckBoxServerRpc rpc = RpcProxy.create(CheckBoxServerRpc.class,
            this);
    private FocusAndBlurServerRpc focusBlurRpc = RpcProxy.create(
            FocusAndBlurServerRpc.class, this);

    @Override
    protected void init() {
        super.init();

        // Register a ValueChangeHandler that handles passing of the changed
        // value to server-side.
        getWidget().addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
            	getState().checked = getWidget().getValue();
            	rpc.setChecked(event.getValue(), null);
                if (getState().immediate) {
                        getConnection().sendPendingVariableChanges();
                    }            
                }
        });
    }

    @Override
    public CheckBoxState getState() {
        return (CheckBoxState) super.getState();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(VSwitch.class);
    }

    @Override
    public VSwitch getWidget() {
        return (VSwitch) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        focusHandlerRegistration = EventHelper.updateFocusHandler(this,
                focusHandlerRegistration);
        blurHandlerRegistration = EventHelper.updateBlurHandler(this,
                blurHandlerRegistration);

        if (null != getState().errorMessage) {
            if (getWidget().getErrorIndicator() == null) {
                com.google.gwt.user.client.Element errorIndicatorElement = DOM
                        .createSpan();
                errorIndicatorElement.setInnerHTML("&nbsp;");
                DOM.setElementProperty(errorIndicatorElement, "className",
                        "v-errorindicator");
                DOM.appendChild(getWidget().getElement(), errorIndicatorElement);
                DOM.sinkEvents(errorIndicatorElement, VTooltip.TOOLTIP_EVENTS
                        | Event.ONCLICK);
                getWidget().setErrorIndicator(errorIndicatorElement);
            } else {
                DOM.setStyleAttribute(getWidget().getErrorIndicator(),
                        "display", "");
            }
        } else if (getWidget().getErrorIndicator() != null) {
            DOM.setStyleAttribute(getWidget().getErrorIndicator(), "display",
                    "none");
        }

        if (isReadOnly()) {
            getWidget().setEnabled(false);
        }

        getWidget().setValue(getState().checked);

        // getState().isImmediate();
    }

    @Override
    public void onFocus(FocusEvent event) {
        // EventHelper.updateFocusHandler ensures that this is called only when
        // there is a listener on server side
        focusBlurRpc.focus();
    }

    @Override
    public void onBlur(BlurEvent event) {
        // EventHelper.updateFocusHandler ensures that this is called only when
        // there is a listener on server side
        focusBlurRpc.blur();
    }
}
