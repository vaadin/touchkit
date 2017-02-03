package org.vaadin.touchkit.gwt.client.vcom;

import java.util.Map.Entry;

import org.vaadin.touchkit.extensions.Html5InputSettings;

import com.google.gwt.dom.client.InputElement;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(Html5InputSettings.class)
public class Html5InputSettingsConnector extends AbstractExtensionConnector {


    private InputElement input;
    
    private InputElement getInput() {
        if(input == null) {
            AbstractComponentConnector parent3 = (AbstractComponentConnector) getParent();
            input = (InputElement) parent3.getWidget().getElement().cast();
        }
        return input;
    }
    @Override
    protected void extend(ServerConnector target) {
    }
    
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        for (Entry<String,String> e : getState().props.entrySet()) {
            getInput().setPropertyString(e.getKey(), e.getValue());
        }
    }
    
    @Override
    public Html5InputSettingsState getState() {
        return (Html5InputSettingsState) super.getState();
    }

}
