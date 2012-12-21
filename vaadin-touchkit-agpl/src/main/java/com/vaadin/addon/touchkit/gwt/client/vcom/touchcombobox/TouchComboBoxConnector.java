package com.vaadin.addon.touchkit.gwt.client.vcom.touchcombobox;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.addon.touchkit.gwt.client.ui.VTouchComboBox;
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

        // getWidget().setPageLength(getState().getPageLength());
        getWidget().setCurrentSuggestions(getState().getFilteredOptions());
        if (getState().getSelectedKey() == null
                && getState().getNullSelectionItemId() != null) {
            getWidget().setSelection(
                    getState().getNullSelectionItemId().caption);
        } else {
            getWidget().setSelection(getState().getSelectedKey());
        }
        getWidget().clearIcons();
        // for(TouchComboBoxOptionState state :
        // getState().getFilteredOptions()){
        // getWidget().putIcon(state.getKey(), getResourceUrl(state.getKey()));
        // }
        // getWidget().setHasPrev(getState().getPage() > 0);
        // getWidget().setHasNext(getState().isHasMore());
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
        if (getState().optionsHasKey(event.getValue())
                || (event.getValue() == null && getState()
                        .isNullSelectionAllowed())) {
            rpc.selectionEvent(event.getValue());
        } else {
            rpc.filterTextValueChanged(event.getValue());
        }
    }

    @Override
    public void onPageEvent(PageEvent event) {
        switch(event.getEventType()) {
        case NEXT:
            rpc.nextPage(event.getKey());
            break;
        case PREVIOUS:
            rpc.previousPage(event.getKey());
            break;
        case ITEM_AMOUNT:
            rpc.pageLengthChange(event.getValue(), event.getKey());
            break;
        case CLOSE:
        default:
            rpc.clearPageNumber();
        }
    }

}
