package com.vaadin.addon.touchkit.gwt.client.touchcombobox;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.addon.touchkit.gwt.client.touchcombobox.PageEvent.PageEventType;
import com.vaadin.addon.touchkit.ui.TouchComboBox;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Connector of ComboBox
 */
@Connect(TouchComboBox.class)
public class TouchComboBoxConnector extends AbstractFieldConnector implements
        ValueChangeHandler<String>, PageEventHandler {

    private static final long serialVersionUID = 1172198873389183416L;

    private TouchComboBoxServerRpc rpc = RpcProxy.create(
            TouchComboBoxServerRpc.class, this);
    private List<HandlerRegistration> handlerRegistration = new ArrayList<HandlerRegistration>();

    @Override
    public void init() {
        super.init();

        handlerRegistration.add(getWidget().addValueChangeHandler(this));
        handlerRegistration.add(getWidget().addPageEventHandler(this));
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setCurrentSuggestions(getState().getFilteredOptions());
        getWidget().setSelection(getState().getSelectedKey());
        getWidget().setPageLength(getState().getPageLength());
        getWidget().setHasNext(getState().isHasMore());
        getWidget().setHasPrev(getState().getPage() > 0);
        getWidget().setWidth(getState().width);
    }

    @Override
    public TouchComboBoxState getState() {
        return (TouchComboBoxState) super.getState();
    }

    @Override
    public VTouchComboBox getWidget() {
        return (VTouchComboBox) super.getWidget();
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        if (getState().optionsHasKey(event.getValue())) {
            rpc.selectionEvent(event.getValue());
        } else {
            rpc.textValueChanged(event.getValue());
        }
    }

    @Override
    public void onPageEvent(PageEvent event) {
        if (event.getEventType().equals(PageEventType.NEXT)) {
            rpc.next();
        } else if (event.getEventType().equals(PageEventType.PREVIOUS)) {
            rpc.previous();
        } else {
            rpc.clearPageNumber();
        }
    }

}
