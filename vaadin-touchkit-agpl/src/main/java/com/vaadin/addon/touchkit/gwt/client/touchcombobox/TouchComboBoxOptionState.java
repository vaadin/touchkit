package com.vaadin.addon.touchkit.gwt.client.touchcombobox;

import com.vaadin.shared.AbstractComponentState;

public class TouchComboBoxOptionState extends AbstractComponentState {

    private static final long serialVersionUID = 6321196555049250896L;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TouchComboBoxOptionState) {
            return key.equals(((TouchComboBoxOptionState) obj).getKey());
        }
        return false;
    }
}
