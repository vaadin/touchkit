package com.vaadin.addon.touchkit.gwt.client.vcom.touchcombobox;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.shared.AbstractFieldState;
import com.vaadin.shared.ui.combobox.FilteringMode;

/**
 * State class for Mobile ComboBox
 */
public class TouchComboBoxState extends AbstractFieldState {

    private static final long serialVersionUID = -6291319383192864607L;

    private FilteringMode filteringMode;
    private String filterstring;
    private String prevFilterstring;

    private boolean textInputAllowed;

    private String selectedKey;
    private boolean nullSelectionAllowed;
    private List<TouchComboBoxOptionState> filteredOptions = new LinkedList<TouchComboBoxOptionState>();;
    private int page;
    private int pageLength;
    private boolean hasMore;

    private TouchComboBoxOptionState nullSelectionItemId;

    public FilteringMode getFilteringMode() {
        return filteringMode;
    }

    public void setFilteringMode(FilteringMode filteringMode) {
        this.filteringMode = filteringMode;
    }

    public String getFilterstring() {
        return filterstring;
    }

    public void setFilterstring(String filterstring) {
        this.filterstring = filterstring;
    }

    public String getPrevFilterstring() {
        return prevFilterstring;
    }

    public void setPrevFilterstring(String prevFilterstring) {
        this.prevFilterstring = prevFilterstring;
    }

    public boolean isTextInputAllowed() {
        return textInputAllowed;
    }

    public void setTextInputAllowed(boolean textInputAllowed) {
        this.textInputAllowed = textInputAllowed;
    }

    public String getSelectedKey() {
        return selectedKey;
    }

    public void setSelectedKey(String selectedKey) {
        this.selectedKey = selectedKey;
    }

    public boolean isNullSelectionAllowed() {
        return nullSelectionAllowed;
    }

    public void setNullSelectionAllowed(boolean nullSelectionAllowed) {
        this.nullSelectionAllowed = nullSelectionAllowed;
    }

    public List<TouchComboBoxOptionState> getFilteredOptions() {
        return filteredOptions;
    }

    public void setFilteredOptions(
            List<TouchComboBoxOptionState> filteredOptions) {
        this.filteredOptions = filteredOptions;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    public boolean optionsHasKey(String value) {
        for (TouchComboBoxOptionState optionState : filteredOptions) {
            if (optionState.getKey().equals(value))
                return true;
        }
        return false;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public TouchComboBoxOptionState getNullSelectionItemId() {
        return nullSelectionItemId;
    }

    public void setNullSelectionItemId(
            TouchComboBoxOptionState nullSelectionItemId) {
        this.nullSelectionItemId = nullSelectionItemId;
    }

}
