package com.vaadin.addon.touchkit.gwt.client.touchcombobox;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.popover.VPopover;
import com.vaadin.addon.touchkit.gwt.client.touchcombobox.PageEvent.PageEventType;

/**
 * 
 */
public class VTouchComboBox extends Widget implements
        HasValueChangeHandlers<String>, ResizeHandler {

    private static final String CLASSNAME = "v-touchkit-combobox";

    private static final int SMALL_SCREEN_WIDTH_THRESHOLD = 500;
    private static final int DEFAULT_ITEM_HEIGHT = 25;
    private static final int TEXT_FIELD_DECORATION = 6;

    private int HEADER_HEIGHT = 35;
    private int SEARCH_FIELD_HEIGHT = 30;
    private int PAGE_LENGTH = 6;

    private Label comboBoxDropDown;
    private Element drop;
    private SelectionPopup popup;
    private boolean hasNext, hasPrev;

    /**
     * A collection of available suggestions (options) as received from the
     * server.
     */
    protected final List<TouchComboBoxOptionState> currentSuggestions = new ArrayList<TouchComboBoxOptionState>();

    protected boolean allowNewItem;
    protected boolean nullSelectionAllowed;
    protected boolean nullSelectItem;
    protected boolean enabled;
    protected boolean readonly;

    /**
     * Create new TouchKit ComboBox
     */
    public VTouchComboBox() {

        Element wrapper = Document.get().createDivElement();
        setElement(wrapper);

        comboBoxDropDown = new Label();
        comboBoxDropDown.setStyleName(CLASSNAME + "-drop-down");

        DOM.sinkEvents(comboBoxDropDown.getElement(), Event.ONCLICK);
        DOM.setEventListener(comboBoxDropDown.getElement(), elementListener);

        // drop = DOM.createDiv();
        // drop.setClassName("drop");

        wrapper.appendChild(comboBoxDropDown.getElement());
        // wrapper.appendChild(drop);

        Window.addResizeHandler(this);

        if (Window.getClientWidth() < SMALL_SCREEN_WIDTH_THRESHOLD) {
            HEADER_HEIGHT = 42;
        }

        setStyleName(CLASSNAME);
    }

    public void setCurrentSuggestions(
            List<TouchComboBoxOptionState> currentSuggestions) {
        this.currentSuggestions.clear();
        this.currentSuggestions.addAll(currentSuggestions);
        populateOptions();
    }

    public void setSelection(String selection) {
        comboBoxDropDown.setText(selection);
    }

    public void setPageLength(int length) {
        PAGE_LENGTH = length;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        if (popup != null)
            popup.updateNextButton();
    }

    public void setHasPrev(boolean hasPrev) {
        this.hasPrev = hasPrev;
        if (popup != null)
            popup.updatePrevButton();
    }

    public void setWidth(String width) {
        comboBoxDropDown.setWidth(width);
        // drop.getStyle().setProperty("left",
        // (comboBoxDropDown.getOffsetWidth() - 30) + "px");
    }

    /**
     * Update popup options
     */
    private void populateOptions() {
        if (popup == null) {
            return;
        }
        popup.clearItems();

        int itemHeight = getItemHeight();
        for (TouchComboBoxOptionState option : currentSuggestions) {
            Label itemLabel = new Label(option.caption);
            itemLabel.setStyleName(CLASSNAME + "-select-item");

            itemLabel.setHeight(itemHeight + "px");
            itemLabel.getElement().getStyle()
                    .setPropertyPx("line-height", itemHeight);

            itemLabel.addClickHandler(new SelectionClickListener(option));
            DOM.sinkEvents(itemLabel.getElement(), Event.ONCLICK);

            popup.addItem(itemLabel);
        }
    }

    private int getItemHeight() {
        int clientHeight = Window.getClientHeight();
        int itemHeight;
        if (clientHeight > (PAGE_LENGTH * DEFAULT_ITEM_HEIGHT)
                && Window.getClientWidth() < SMALL_SCREEN_WIDTH_THRESHOLD) {
            itemHeight = (clientHeight - HEADER_HEIGHT - SEARCH_FIELD_HEIGHT)
                    / PAGE_LENGTH;
        } else {
            itemHeight = DEFAULT_ITEM_HEIGHT;
        }
        return itemHeight;
    }

    /**
     * Input element listener //
     */
    private class SelectionClickListener implements ClickHandler {

        private TouchComboBoxOptionState object;

        public SelectionClickListener(TouchComboBoxOptionState object) {
            super();
            this.object = object;
        }

        @Override
        public void onClick(ClickEvent event) {
            ValueChangeEvent.fire(VTouchComboBox.this, object.getKey());
            if (popup != null) {
                popup.hide();
            }
        }
    };

    private EventListener elementListener = new EventListener() {

        @Override
        public void onBrowserEvent(Event event) {
            popup = new SelectionPopup();
            if (Window.getClientWidth() > SMALL_SCREEN_WIDTH_THRESHOLD) {
                popup.showNextTo(VTouchComboBox.this);
            }

            populateOptions();
        }
    };

    private class ValueChange implements ValueChangeHandler<String> {

        public void onValueChange(ValueChangeEvent<String> event) {
            ValueChangeEvent.fire(VTouchComboBox.this, event.getValue());
        }
    }

    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public HandlerRegistration addPageEventHandler(
            PageEventHandler pageEventHandler) {
        return addHandler(pageEventHandler, PageEvent.getType());
    }

    @Override
    public void onResize(ResizeEvent event) {
        if (popup != null) {
            populateOptions();
        }
    };

    protected class SelectionPopup extends VPopover {

        final FlowPanel select = new FlowPanel();
        final HorizontalPanel header = new HorizontalPanel();
        final TextBox filter = new TextBox();
        final FlowPanel content = new FlowPanel();

        Button next, prev;

        public SelectionPopup() {
            init();
            setStyleName("v-touchkit-popover");
        }

        private void init() {
            setAutoHideEnabled(false);

            int itemHeight = getItemHeight();
            if (itemHeight == DEFAULT_ITEM_HEIGHT) {
                // as items have a border-bottom of 1px
                itemHeight++;
            }

            int boxWidth = getBoxWidth();

            select.setHeight((HEADER_HEIGHT + SEARCH_FIELD_HEIGHT + (itemHeight * PAGE_LENGTH))
                    + "px");
            select.setWidth(boxWidth + "px");
            select.getElement().getStyle().setBackgroundColor("white");
            select.setStyleName("v-touchkit-popover");
            if (Window.getClientWidth() > SMALL_SCREEN_WIDTH_THRESHOLD) {
                select.addStyleName(VTouchComboBox.CLASSNAME);
            }

            filter.setWidth(boxWidth - TEXT_FIELD_DECORATION + "px");
            filter.setStyleName("v-touchkit-combobox-popup-filter");
            filter.setValue(comboBoxDropDown.getText());

            content.setHeight((itemHeight * PAGE_LENGTH) + "px");
            content.setWidth(boxWidth + "px");

            header.setHeight(HEADER_HEIGHT + "px");
            header.setWidth("100%");

            final Button close = new Button("x");
            close.setStyleName("v-touchkit-combobox-close-button");
            close.getElement().getStyle()
                    .setProperty("left", (boxWidth / 2 - 12) + "px");
            close.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    VTouchComboBox.this.fireEvent(new PageEvent(
                            PageEventType.CLOSE));
                    hide();
                }
            });

            next = new Button("next");
            next.setStyleName("v-touchkit-navbar-right v-touchkit-navbutton-forward");
            next.addClickHandler(new NavigationClickListener(PageEventType.NEXT));

            prev = new Button("prev");
            prev.setStyleName("v-touchkit-navbar-left v-touchkit-navbutton-back");
            prev.addClickHandler(new NavigationClickListener(
                    PageEventType.PREVIOUS));

            updatePrevButton();
            updateNextButton();

            header.add(prev);
            header.add(close);
            header.add(next);

            select.add(header);

            filter.addValueChangeHandler(new ValueChange());

            select.add(filter);

            select.add(content);
            setWidget(select);

            if (Window.getClientWidth() < SMALL_SCREEN_WIDTH_THRESHOLD) {
                super.slideIn();
                setPopupPosition(0, 0);
            }
            show();
        }

        private int getBoxWidth() {
            if (Window.getClientWidth() < SMALL_SCREEN_WIDTH_THRESHOLD) {
                return Window.getClientWidth();
            }
            return comboBoxDropDown.getOffsetWidth();
        }

        public void updateNextButton() {
            next.setVisible(hasNext);
        }

        public void updatePrevButton() {
            prev.setVisible(hasPrev);
        }

        public void addItem(Label item) {
            content.add(item);
        }

        public void clearItems() {
            content.clear();
        }

        @Override
        public void onClose(CloseEvent<PopupPanel> event) {
            clearItems();
            popup = null;
        };

        private class NavigationClickListener implements ClickHandler {

            private PageEventType eventType;

            public NavigationClickListener(PageEventType eventType) {
                super();
                this.eventType = eventType;
            }

            @Override
            public void onClick(ClickEvent event) {
                VTouchComboBox.this.fireEvent(new PageEvent(eventType));
            }
        };
    }
}
