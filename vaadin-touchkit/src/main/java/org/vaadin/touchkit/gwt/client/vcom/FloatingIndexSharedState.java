package org.vaadin.touchkit.gwt.client.vcom;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.SharedState;

public class FloatingIndexSharedState extends SharedState {
    
    public List<String> keys = new ArrayList<String>();
    public List<Connector> connectors = new ArrayList<Connector>();
    
    public void map(String key, Connector component) {
        keys.add(key);
        connectors.add(component);
    }

}
