package com.vaadin.addon.touchkit.gwt.client.touchcombobox;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
import com.google.gwt.user.client.Timer;
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
import com.vaadin.client.VConsole;

/**
 * TouchComboBox client side implementation.
 */
public class VTouchComboBox extends Widget implements
        HasValueChangeHandlers<String>, ResizeHandler {

    private static final String CLASSNAME = "v-touchkit-combobox";

    private static final int DEFAULT_ITEM_HEIGHT = 40;
    private static final int SMALL_SCREEN_WIDTH_THRESHOLD = 500;

    private int pageLength = 6;
    private int HEADER_HEIGHT = 45;

    private SelectionPopup popup;
    private Label comboBoxDropDown;
    private boolean hasNext, hasPrev;

    /**
     * A collection of available suggestions (options) as received from the
     * server.
     */
    protected final LinkedList<TouchComboBoxOptionState> currentSuggestions = new LinkedList<TouchComboBoxOptionState>();
    protected Map<String, String> itemIcons = new HashMap<String, String>();

    protected boolean enabled;
    protected boolean readonly;
    protected boolean allowNewItem;
    protected boolean nullSelectionAllowed;
    protected TouchComboBoxOptionState nullSelectionItemId;
    protected TouchComboBoxOptionState firstVisibleItem, nextItem, prevItem;

    public native InputElement createInputElementWithType(Document doc, String type)
    /*-{
        var e = doc.createElement("INPUT");
        e.type = type;
        return e;
    }-*/;

    /**
     * Create new TouchKit ComboBox
     */
    public VTouchComboBox() {

        Element wrapper = Document.get().createDivElement();
        setElement(wrapper);

        comboBoxDropDown = new Label();
        comboBoxDropDown.setStyleName(CLASSNAME + "-drop-down");

        DOM.sinkEvents(comboBoxDropDown.getElement(), Event.ONCLICK);
        DOM.setEventListener(comboBoxDropDown.getElement(), dropDownListener);

        wrapper.appendChild(comboBoxDropDown.getElement());

        Window.addResizeHandler(this);

        if (isSmallScreen()) {
            HEADER_HEIGHT = 52;
        }

        setStyleName(CLASSNAME);
    }

    public void setCurrentSuggestions(
            List<TouchComboBoxOptionState> currentSuggestions) {
        this.currentSuggestions.clear();
        this.currentSuggestions.addAll(currentSuggestions);
        if (!currentSuggestions.isEmpty() && firstVisibleItem == null) {
            firstVisibleItem = currentSuggestions.iterator().next();
        }
        populateOptions();
    }

    public void setSelection(String selection) {
        comboBoxDropDown.setText(selection);
    }

    public void clearIcons() {
        itemIcons.clear();
    }

    public void putIcon(String key, String icon) {
        itemIcons.put(key, icon);
    }

    public void setPageLength(int length) {
        pageLength = length;
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

        int leftOffset = comboBoxDropDown.getOffsetWidth() - 30;
        Style style = comboBoxDropDown.getElement().getStyle();
        style.setProperty("background-position", leftOffset + "px -6px ");
    }

    /**
     * Update popup options
     * 
     * Always show buttons for full page length
     */
    private void populateOptions() {
        if (popup == null) {
            return;
        }
        popup.clearItems();
        List<TouchComboBoxOptionState> visible = new LinkedList<TouchComboBoxOptionState>();

        int from = currentSuggestions.indexOf(firstVisibleItem);
        if (from < 0) {
            from = 0;
            VConsole.error("Item not found");
        }

        int to = from + pageLength > currentSuggestions.size() ? currentSuggestions
                .size() - 1 : from + pageLength;

        if (to < 0) {
            fillWithEmptySelects(visible);
            return;
        }

        visible.addAll(currentSuggestions.subList(from, to));

        if (to + 1 == currentSuggestions.size()) {
            visible.add(currentSuggestions.getLast());
        }

        nextItem = to + 1 >= currentSuggestions.size() ? currentSuggestions
                .getLast() : currentSuggestions.get(to);
        prevItem = from - pageLength < 0 ? currentSuggestions.getFirst()
                : currentSuggestions.get(from - pageLength);

        setHasNext(to + 1 < currentSuggestions.size());
        setHasPrev(from > 0);

        for (TouchComboBoxOptionState option : visible) {
            Label itemLabel = createItemLabel(option.caption,
                    DEFAULT_ITEM_HEIGHT);

            if (itemIcons.containsKey(option.getKey())) {
                itemLabel.getElement().getStyle()
                        .setBackgroundImage(itemIcons.get(option.getKey()));
            }

            itemLabel.addClickHandler(new SelectionClickListener(option));
            DOM.sinkEvents(itemLabel.getElement(), Event.ONCLICK);

            popup.addItem(itemLabel);
        }
        fillWithEmptySelects(visible);
    }

    private void fillWithEmptySelects(List<TouchComboBoxOptionState> visible) {
        if (visible.size() < pageLength) {
            for (int i = visible.size(); i < pageLength; i++) {
                Label itemLabel = createItemLabel("", DEFAULT_ITEM_HEIGHT);
                itemLabel.addStyleName("empty");

                popup.addItem(itemLabel);
            }
        }
    }

    private Label createItemLabel(String caption, int itemHeight) {
        Label itemLabel = new Label(caption);
        itemLabel.setStyleName(CLASSNAME + "-select-item");

        itemLabel.setHeight(itemHeight + "px");
        itemLabel.getElement().getStyle()
                .setPropertyPx("lineHeight", itemHeight);

        return itemLabel;
    }

    private boolean isPortrait() {
        return Window.getClientHeight() > Window.getClientWidth();
    }

    private boolean isSmallScreen() {
        if (isPortrait()) {
            return Window.getClientWidth() < SMALL_SCREEN_WIDTH_THRESHOLD;
        }
        return Window.getClientHeight() < SMALL_SCREEN_WIDTH_THRESHOLD;
    }

    private void setRelativePosition(SelectionPopup popup) {
        popup.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop()
                + getOffsetHeight());
    }

    /* Event Listeners */

    private EventListener dropDownListener = new EventListener() {

        @Override
        public void onBrowserEvent(Event event) {
            if (popup == null) {
                popup = new SelectionPopup();
                popup.updateSize();

                populateOptions();
            }
        }
    };

    @Override
    public void onResize(ResizeEvent event) {
        if (popup != null && isSmallScreen()) {
            popup.updateSize();
        }
    };

    /* Handlers */
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

    private class ValueChange implements ValueChangeHandler<String> {

        public void onValueChange(ValueChangeEvent<String> event) {
            ValueChangeEvent.fire(VTouchComboBox.this, event.getValue());
            popup.clearTimer();
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

    protected class SelectionPopup extends VPopover {

        final FlowPanel select = new FlowPanel();
        final HorizontalPanel header = new HorizontalPanel();
        final TextBox filter = TextBox.wrap(createInputElementWithType(Document.get(),
                "search"));
        final FlowPanel content = new FlowPanel();

        Timer refresh = null;

        Button next, prev, close;

        public SelectionPopup() {
            init();
            setStyleName("v-touchkit-popover");
        }

        private void init() {
            select.getElement().getStyle().setBackgroundColor("white");
            select.setStyleName("v-touchkit-popover");
            if (!isSmallScreen()) {
                select.addStyleName(VTouchComboBox.CLASSNAME);
            }

            filter.setStyleName("v-touchkit-combobox-popup-filter");
            filter.setValue(comboBoxDropDown.getText());
            filter.getElement().getStyle()
                    .setProperty("-webkit-appearance", "textfield");
            filter.addKeyDownHandler(filterKeyHandler);

            header.setHeight(HEADER_HEIGHT + "px");
            header.setWidth("100%");

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

            if (isSmallScreen()) {
                addCloseButton();
            }

            header.add(next);
            filter.addValueChangeHandler(new ValueChange());

            select.add(header);
            select.add(filter);
            select.add(content);

            setWidget(select);

            if (isSmallScreen()) {
                super.slideIn();
                setPopupPosition(0, 0);
            } else {
                setRelativePosition(this);
                setAutoHideEnabled(true);
            }
            show();
        }

        private void addCloseButton() {
            close = new Button("x");
            close.setStyleName("v-touchkit-combobox-close-button");
            close.getElement().getStyle()
                    .setProperty("left", (getBoxWidth() / 2 - 12) + "px");
            close.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    hide();
                }
            });

            header.add(close);
        }

        private int getBoxWidth() {
            if (isSmallScreen()) {
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

        public void clearTimer() {
            if (refresh != null) {
                refresh.cancel();
                refresh = null;
            }
        }

        public void updateSize() {
            int newWidth = getBoxWidth();

            if (close != null) {
                close.getElement().getStyle()
                        .setProperty("left", (newWidth / 2 - 12) + "px");
            }
            header.setWidth(newWidth + "px");
            select.setWidth(newWidth + "px");
            filter.setWidth(newWidth + "px");
            content.setWidth(newWidth + "px");
            setWidth(newWidth + "px");

            if (isSmallScreen()) {
                handleSmallScreenItemAmount();
            }
        }

        private void handleSmallScreenItemAmount() {
            int availableHeight = Window.getClientHeight() - HEADER_HEIGHT
                    - filter.getOffsetHeight();

            int maxItems = (int) Math.floor(availableHeight
                    / DEFAULT_ITEM_HEIGHT);
            if (maxItems == 0) {
                maxItems = 1;
            }

            pageLength = maxItems;
            populateOptions();

            if (firstVisibleItem == null)
                VTouchComboBox.this.fireEvent(new PageEvent(
                        PageEventType.ITEM_AMOUNT, maxItems, null));
            else
                VTouchComboBox.this.fireEvent(new PageEvent(
                        PageEventType.ITEM_AMOUNT, maxItems, firstVisibleItem
                                .getKey()));
        }

        @Override
        public void onClose(CloseEvent<PopupPanel> event) {
            clearItems();
            hide();
            popup = null;
            nextItem = null;
            prevItem = null;
            firstVisibleItem = null;
            VTouchComboBox.this.fireEvent(new PageEvent(PageEventType.CLOSE));
        };

        private class NavigationClickListener implements ClickHandler {

            private PageEventType eventType;

            public NavigationClickListener(PageEventType eventType) {
                super();
                this.eventType = eventType;
            }

            @Override
            public void onClick(ClickEvent event) {
                switch (eventType) {
                case NEXT:
                    firstVisibleItem = nextItem;
                    break;
                case PREVIOUS:
                    firstVisibleItem = prevItem;
                    break;
                }
                VTouchComboBox.this.fireEvent(new PageEvent(eventType,
                        firstVisibleItem.getKey()));
            }
        };

        private KeyDownHandler filterKeyHandler = new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (refresh != null) {
                    refresh.cancel();
                }
                try {
                    refresh = new Timer() {

                        @Override
                        public void run() {
                            ValueChangeEvent.fire(VTouchComboBox.this,
                                    filter.getValue());
                            refresh = null;
                            firstVisibleItem = null;
                        }
                    };
                    refresh.schedule(1500);
                } catch (Throwable alert) {
                    VConsole.error(alert.getMessage());
                }
            }
        };
    }
}
