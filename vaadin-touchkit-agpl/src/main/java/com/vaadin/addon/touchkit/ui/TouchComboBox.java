package com.vaadin.addon.touchkit.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.vaadin.addon.touchkit.gwt.client.touchcombobox.TouchComboBoxOptionState;
import com.vaadin.addon.touchkit.gwt.client.touchcombobox.TouchComboBoxServerRpc;
import com.vaadin.addon.touchkit.gwt.client.touchcombobox.TouchComboBoxState;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSelect.NewItemHandler;

public class TouchComboBox extends AbstractField<Object> implements
        AbstractSelect.Filtering, FieldEvents.BlurNotifier,
        FieldEvents.FocusNotifier, Container, Container.Viewer,
        Container.PropertySetChangeListener,
        Container.PropertySetChangeNotifier, Container.ItemSetChangeNotifier,
        Container.ItemSetChangeListener {

    private static final long serialVersionUID = -6818738061622181603L;

    private String inputPrompt = null;

    private FilteringMode filteringMode = FilteringMode.CONTAINS;

    private String filterstring;
    private String prevfilterstring;

    /**
     * Cache of filtered options, used only by the in-memory filtering system.
     */
    private List<Object> filteredOptions;

    /**
     * True if the container is being filtered temporarily and item set change
     * notifications should be suppressed.
     */
    private boolean filteringContainer;

    private int pageLength = 6;
    private int currentPage = 0;

    /* From AbstractSelect */
    public enum ItemCaptionMode {
        /**
         * Item caption mode: Item's ID's <code>String</code> representation is
         * used as caption.
         */
        ID,
        /**
         * Item caption mode: Item's <code>String</code> representation is used
         * as caption.
         */
        ITEM,
        /**
         * Item caption mode: Index of the item is used as caption. The index
         * mode can only be used with the containers implementing the
         * {@link com.vaadin.data.Container.Indexed} interface.
         */
        INDEX,
        /**
         * Item caption mode: If an Item has a caption it's used, if not, Item's
         * ID's <code>String</code> representation is used as caption. <b>This
         * is the default</b>.
         */
        EXPLICIT_DEFAULTS_ID,
        /**
         * Item caption mode: Captions must be explicitly specified.
         */
        EXPLICIT,
        /**
         * Item caption mode: Only icons are shown, captions are hidden.
         */
        ICON_ONLY,
        /**
         * Item caption mode: Item captions are read from property specified
         * with <code>setItemCaptionPropertyId</code>.
         */
        PROPERTY;
    }

    /**
     * Select options.
     */
    protected Container items;

    /**
     * Is the user allowed to add new options?
     */
    private boolean allowNewOptions;

    /**
     * Keymapper used to map key values.
     */
    protected KeyMapper<Object> itemIdMapper = new KeyMapper<Object>();

    /**
     * Item icons.
     */
    private final HashMap<Object, Resource> itemIcons = new HashMap<Object, Resource>();

    /**
     * Item captions.
     */
    private final HashMap<Object, String> itemCaptions = new HashMap<Object, String>();

    /**
     * Item caption mode.
     */
    private ItemCaptionMode itemCaptionMode = ItemCaptionMode.EXPLICIT_DEFAULTS_ID;

    /**
     * Item caption source property id.
     */
    private Object itemCaptionPropertyId = null;

    /**
     * Item icon source property id.
     */
    private Object itemIconPropertyId = null;

    /**
     * List of property set change event listeners.
     */
    private Set<Container.PropertySetChangeListener> propertySetEventListeners = null;

    /**
     * List of item set change event listeners.
     */
    private Set<Container.ItemSetChangeListener> itemSetEventListeners = null;

    /**
     * Item id that represents null selection of this select.
     * 
     * <p>
     * Data interface does not support nulls as item ids. Selecting the item
     * identified by this id is the same as selecting no items at all. This
     * setting only affects the single select mode.
     * </p>
     */
    private Object nullSelectionItemId = null;

    // Null (empty) selection is enabled by default
    private boolean nullSelectionAllowed = true;
    private NewItemHandler newItemHandler;

    // Caption (Item / Property) change listeners
    CaptionChangeListener captionChangeListener;

    private TouchComboBoxServerRpc rpc = new TouchComboBoxServerRpc() {
        private static final long serialVersionUID = 8400073918577914053L;

        @Override
        public void textValueChanged(String filter) {
            currentPage = 0;
            prevfilterstring = filterstring;
            filterstring = filter.toLowerCase();
            updateOptions(false);
        }

        @Override
        public void selectionEvent(String key) {
            if (itemIdMapper.get(key) != null) {
                currentPage = 0;
                prevfilterstring = filterstring;
                filterstring = "";
                updateOptions(false);
                setValue(itemIdMapper.get(key));
            }
        }

        @Override
        public void next() {
            currentPage++;
            updateOptions(false);
        }

        @Override
        public void previous() {
            currentPage--;
            updateOptions(false);
        }

        @Override
        public void clearPageNumber() {
            currentPage = 0;
            updateOptions(false);
        }
    };

    /* Constructors */

    /**
     * Creates an empty Select.
     */
    public TouchComboBox() {
        registerRpc(rpc);
        setContainerDataSource(new IndexedContainer());
    }

    /**
     * Creates an empty Select with caption.
     */
    public TouchComboBox(String caption) {
        registerRpc(rpc);
        setContainerDataSource(new IndexedContainer());
        setCaption(caption);
    }

    /**
     * Creates a new select that is connected to a data-source.
     * 
     * @param caption
     *            the Caption of the component.
     * @param dataSource
     *            the Container datasource to be selected from by this select.
     */
    public TouchComboBox(String caption, Container dataSource) {
        registerRpc(rpc);
        setCaption(caption);
        setContainerDataSource(dataSource);
    }

    /**
     * Creates a new select that is filled from a collection of option values.
     * 
     * @param caption
     *            the Caption of this field.
     * @param options
     *            the Collection containing the options.
     */
    public TouchComboBox(String caption, Collection<?> options) {
        registerRpc(rpc);
        // Creates the options container and add given options to it
        final Container c = new IndexedContainer();
        if (options != null) {
            for (final Iterator<?> i = options.iterator(); i.hasNext();) {
                c.addItem(i.next());
            }
        }

        setCaption(caption);
        setContainerDataSource(c);
    }

    @Override
    public TouchComboBoxState getState() {
        return (TouchComboBoxState) super.getState();
    }

    /**
     * Returns the filtered options for the current page using a container
     * filter.
     * 
     * As a size effect, {@link #filteredSize} is set to the total number of
     * items passing the filter.
     * 
     * The current container must be {@link Filterable} and {@link Indexed}, and
     * the filtering mode must be suitable for container filtering (tested with
     * {@link #canUseContainerFilter()}).
     * 
     * Use {@link #getFilteredOptions()} and
     * {@link #sanitetizeList(List, boolean)} if this is not the case.
     * 
     * @param needNullSelectOption
     * @return filtered list of options (may be empty) or null if cannot use
     *         container filters
     */
    protected List<?> getOptionsWithFilter(boolean needNullSelectOption) {
        Container container = getContainerDataSource();

        if (!(container instanceof Filterable)
                || !(container instanceof Indexed)
                || getItemCaptionMode() != ItemCaptionMode.PROPERTY) {
            return null;
        }

        Filterable filterable = (Filterable) container;

        Filter filter = buildFilter(filterstring, filteringMode);

        // adding and removing filters leads to extraneous item set
        // change events from the underlying container, but the ComboBox does
        // not process or propagate them based on the flag filteringContainer
        if (filter != null) {
            filteringContainer = true;
            filterable.addContainerFilter(filter);
        }
        try {
            return new ArrayList<Object>(container.getItemIds());
        } finally {
            // to the outside, filtering should not be visible
            if (filter != null) {
                filterable.removeContainerFilter(filter);
                filteringContainer = false;
            }
        }
    }

    /**
     * Constructs a filter instance to use when using a Filterable container in
     * the <code>ITEM_CAPTION_MODE_PROPERTY</code> mode.
     * 
     * Note that the client side implementation expects the filter string to
     * apply to the item caption string it sees, so changing the behavior of
     * this method can cause problems.
     * 
     * @param filterString
     * @param filteringMode
     * @return
     */
    protected Filter buildFilter(String filterString,
            FilteringMode filteringMode) {
        Filter filter = null;

        if (null != filterString && !"".equals(filterString)) {
            switch (filteringMode) {
            case OFF:
                break;
            case STARTSWITH:
                filter = new SimpleStringFilter(getItemCaptionPropertyId(),
                        filterString, true, true);
                break;
            case CONTAINS:
                filter = new SimpleStringFilter(getItemCaptionPropertyId(),
                        filterString, true, false);
                break;
            }
        }
        return filter;
    }

    /**
     * Makes correct sublist of given list of options.
     * 
     * If paint is not an option request (affected by page or filter change),
     * page will be the one where possible selection exists.
     * 
     * Detects proper first and last item in list to return right page of
     * options. Also, if the current page is beyond the end of the list, it will
     * be adjusted.
     * 
     * @param options
     * @param needNullSelectOption
     *            flag to indicate if nullselect option needs to be taken into
     *            consideration
     */
    private List<?> sanitetizeList(List<?> options, boolean needNullSelectOption) {
        return options;
    }

    /**
     * Filters the options in memory and returns the full filtered list.
     * 
     * This can be less efficient than using container filters, so use
     * {@link #getOptionsWithFilter(boolean)} if possible (filterable container
     * and suitable item caption mode etc.).
     * 
     * @return
     */
    protected List<?> getFilteredOptions() {
        if (null == filterstring || "".equals(filterstring)
                || FilteringMode.OFF == filteringMode) {
            prevfilterstring = null;
            filteredOptions = new LinkedList<Object>(getItemIds());
            return filteredOptions;
        }

        if (filterstring.equals(prevfilterstring)) {
            return filteredOptions;
        }

        Collection<?> items;
        if (prevfilterstring != null
                && filterstring.startsWith(prevfilterstring)) {
            items = filteredOptions;
        } else {
            items = getItemIds();
        }
        prevfilterstring = filterstring;

        filteredOptions = new LinkedList<Object>();
        for (final Iterator<?> it = items.iterator(); it.hasNext();) {
            final Object itemId = it.next();
            String caption = getItemCaption(itemId);
            if (caption == null || caption.equals("")) {
                continue;
            } else {
                caption = caption.toLowerCase();
            }
            switch (filteringMode) {
            case CONTAINS:
                if (caption.contains(filterstring)) {
                    filteredOptions.add(itemId);
                }
                break;
            case STARTSWITH:
            default:
                if (caption.startsWith(filterstring)) {
                    filteredOptions.add(itemId);
                }
                break;
            }
        }

        return filteredOptions;
    }

    @Override
    public void setFilteringMode(FilteringMode filteringMode) {
        this.filteringMode = filteringMode;
    }

    @Override
    public FilteringMode getFilteringMode() {
        return filteringMode;
    }

    public String getInputPrompt() {
        return inputPrompt;
    }

    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
    }

    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        getState().width = width;
    }

    private void updateOptions(boolean addNotifier) {
        boolean nullFilteredOut = isNullFilteredOut();
        boolean nullOptionVisible = isNullOptionVisible(nullFilteredOut);
        List<?> options = getFilteredOptions(nullOptionVisible);

        final Iterator<?> i = options.iterator();
        List<TouchComboBoxOptionState> items = new LinkedList<TouchComboBoxOptionState>();
        // Paints the available selection options from data source
        while (i.hasNext()) {
            // Gets the option attribute values
            final Object id = i.next();
            if (!isNullSelectionAllowed() && id != null
                    && id.equals(getNullSelectionItemId())) {
                // Remove item if it's the null selection item but null
                // selection is not allowed
                continue;
            }

            getCaptionChangeListener().clear();
            addItemNotifier(id);

            items.add(createItemState(id));
        }
        getState().setFilteredOptions(items);
        getState().setPage(currentPage);
    }

    private List<?> getFilteredOptions(boolean nullOptionVisible) {
        List<?> options = getOptionsWithFilter(nullOptionVisible);
        if (null == options) {
            // not able to use container filters, perform explicit in-memory
            // filtering
            options = getFilteredOptions();
            options = sanitetizeList(options, nullOptionVisible);
        }
        if (!options.isEmpty()) {
            int startIndex = currentPage * pageLength;
            int endIndex = startIndex + pageLength;
            getState().setHasMore(endIndex < options.size());
            options = options.subList(startIndex,
                    endIndex < options.size() ? endIndex : options.size());
        }
        return options;
    }

    private void addItemNotifier(Object id) {
        // add listener for each item, to cause repaint if an item changes
        getCaptionChangeListener().addNotifierForItem(id);
        if (isSelected(id)) {
            getState().setSelectedKey(getItemCaption(id));
        }
    }

    private TouchComboBoxOptionState createItemState(Object id) {
        TouchComboBoxOptionState itemState = new TouchComboBoxOptionState();
        itemState.setKey(itemIdMapper.key(id));
        itemState.caption = getItemCaption(id);
        // itemState.setResource(getItemIcon(id));

        return itemState;
    }

    private boolean isNullFilteredOut() {
        return filterstring != null && !"".equals(filterstring)
                && filteringMode != FilteringMode.OFF;
    }

    private boolean isNullOptionVisible(boolean nullFilteredOut) {
        return (isNullSelectionAllowed() && getNullSelectionItemId() == null)
                && !nullFilteredOut;
    }

    /* Abstract select Component methods */

    /**
     * Gets the visible item ids. In Select, this returns list of all item ids,
     * but can be overriden in subclasses if they paint only part of the items
     * to the terminal or null if no items is visible.
     */
    public Collection<?> getVisibleItemIds() {
        return getItemIds();
    }

    /* Property methods */

    /**
     * Returns the type of the property. <code>getValue</code> and
     * <code>setValue</code> methods must be compatible with this type: one can
     * safely cast <code>getValue</code> to given type and pass any variable
     * assignable to this type as a parameter to <code>setValue</code>.
     * 
     * @return the Type of the property.
     */
    @Override
    public Class<?> getType() {
        return Object.class;
    }

    /**
     * Gets the selected item id or in multiselect mode a set of selected ids.
     * 
     * @see com.vaadin.ui.AbstractField#getValue()
     */
    @Override
    public Object getValue() {
        final Object retValue = super.getValue();

        return retValue;
    }

    /**
     * Sets the visible value of the property.
     * 
     * <p>
     * The value of the select is the selected item id.
     * </p>
     * 
     * @param newValue
     *            the New selected item.
     * @see com.vaadin.ui.AbstractField#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object newValue) throws Property.ReadOnlyException {
        if (newValue == getNullSelectionItemId()) {
            newValue = null;
        }

        setValue(newValue, false);
        getState().setSelectedKey(getItemCaption(newValue));
    }

    /**
     * Sets the visible value of the property.
     * 
     * <p>
     * The value of the select is the selected item id.
     * </p>
     * 
     * @param newValue
     *            the New selected item.
     * @param repaintIsNotNeeded
     *            True if caller is sure that repaint is not needed.
     * @see com.vaadin.ui.AbstractField#setValue(java.lang.Object,
     *      java.lang.Boolean)
     */
    @Override
    protected void setValue(Object newValue, boolean repaintIsNotNeeded)
            throws Property.ReadOnlyException {

        if (newValue == null || items.containsId(newValue)) {
            super.setValue(newValue, repaintIsNotNeeded);
        }
    }

    /* Container methods */

    /**
     * Gets the item from the container with given id. If the container does not
     * contain the requested item, null is returned.
     * 
     * @param itemId
     *            the item id.
     * @return the item from the container.
     */
    @Override
    public Item getItem(Object itemId) {
        return items.getItem(itemId);
    }

    /**
     * Gets the item Id collection from the container.
     * 
     * @return the Collection of item ids.
     */
    @Override
    public Collection<?> getItemIds() {
        return items.getItemIds();
    }

    /**
     * Gets the property Id collection from the container.
     * 
     * @return the Collection of property ids.
     */
    @Override
    public Collection<?> getContainerPropertyIds() {
        return items.getContainerPropertyIds();
    }

    /**
     * Gets the property type.
     * 
     * @param propertyId
     *            the Id identifying the property.
     * @see com.vaadin.data.Container#getType(java.lang.Object)
     */
    @Override
    public Class<?> getType(Object propertyId) {
        return items.getType(propertyId);
    }

    /*
     * Gets the number of items in the container.
     * 
     * @return the Number of items in the container.
     * 
     * @see com.vaadin.data.Container#size()
     */
    @Override
    public int size() {
        return items.size();
    }

    /**
     * Tests, if the collection contains an item with given id.
     * 
     * @param itemId
     *            the Id the of item to be tested.
     */
    @Override
    public boolean containsId(Object itemId) {
        if (itemId != null) {
            return items.containsId(itemId);
        } else {
            return false;
        }
    }

    /**
     * Gets the Property identified by the given itemId and propertyId from the
     * Container
     * 
     * @see com.vaadin.data.Container#getContainerProperty(Object, Object)
     */
    @Override
    public Property<?> getContainerProperty(Object itemId, Object propertyId) {
        return items.getContainerProperty(itemId, propertyId);
    }

    /**
     * Adds the new property to all items. Adds a property with given id, type
     * and default value to all items in the container.
     * 
     * This functionality is optional. If the function is unsupported, it always
     * returns false.
     * 
     * @return True if the operation succeeded.
     * @see com.vaadin.data.Container#addContainerProperty(java.lang.Object,
     *      java.lang.Class, java.lang.Object)
     */
    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException {

        final boolean retval = items.addContainerProperty(propertyId, type,
                defaultValue);
        if (retval && !(items instanceof Container.PropertySetChangeNotifier)) {
            firePropertySetChange();
        }
        return retval;
    }

    /**
     * Removes all items from the container.
     * 
     * This functionality is optional. If the function is unsupported, it always
     * returns false.
     * 
     * @return True if the operation succeeded.
     * @see com.vaadin.data.Container#removeAllItems()
     */
    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {

        final boolean retval = items.removeAllItems();
        itemIdMapper.removeAll();
        if (retval) {
            setValue(null);
            if (!(items instanceof Container.ItemSetChangeNotifier)) {
                fireItemSetChange();
            }
        }
        return retval;
    }

    /**
     * Creates a new item into container with container managed id. The id of
     * the created new item is returned. The item can be fetched with getItem()
     * method. if the creation fails, null is returned.
     * 
     * @return the Id of the created item or null in case of failure.
     * @see com.vaadin.data.Container#addItem()
     */
    @Override
    public Object addItem() throws UnsupportedOperationException {

        final Object retval = items.addItem();
        if (retval != null
                && !(items instanceof Container.ItemSetChangeNotifier)) {
            fireItemSetChange();
        }
        return retval;
    }

    /**
     * Create a new item into container. The created new item is returned and
     * ready for setting property values. if the creation fails, null is
     * returned. In case the container already contains the item, null is
     * returned.
     * 
     * This functionality is optional. If the function is unsupported, it always
     * returns null.
     * 
     * @param itemId
     *            the Identification of the item to be created.
     * @return the Created item with the given id, or null in case of failure.
     * @see com.vaadin.data.Container#addItem(java.lang.Object)
     */
    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {

        final Item retval = items.addItem(itemId);
        if (retval != null
                && !(items instanceof Container.ItemSetChangeNotifier)) {
            fireItemSetChange();
        }
        return retval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#removeItem(java.lang.Object)
     */
    @Override
    public boolean removeItem(Object itemId)
            throws UnsupportedOperationException {

        unselect(itemId);
        final boolean retval = items.removeItem(itemId);
        itemIdMapper.remove(itemId);
        if (retval && !(items instanceof Container.ItemSetChangeNotifier)) {
            fireItemSetChange();
        }
        return retval;
    }

    /**
     * Removes the property from all items. Removes a property with given id
     * from all the items in the container.
     * 
     * This functionality is optional. If the function is unsupported, it always
     * returns false.
     * 
     * @return True if the operation succeeded.
     * @see com.vaadin.data.Container#removeContainerProperty(java.lang.Object)
     */
    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {

        final boolean retval = items.removeContainerProperty(propertyId);
        if (retval && !(items instanceof Container.PropertySetChangeNotifier)) {
            firePropertySetChange();
        }
        return retval;
    }

    /* Container.Viewer methods */

    /**
     * Sets the Container that serves as the data source of the viewer.
     * 
     * As a side-effect the fields value (selection) is set to null due old
     * selection not necessary exists in new Container.
     * 
     * @see com.vaadin.data.Container.Viewer#setContainerDataSource(Container)
     * 
     * @param newDataSource
     *            the new data source.
     */
    @Override
    public void setContainerDataSource(Container newDataSource) {
        if (newDataSource == null) {
            newDataSource = new IndexedContainer();
        }

        getCaptionChangeListener().clear();

        if (items != newDataSource) {

            // Removes listeners from the old datasource
            if (items != null) {
                if (items instanceof Container.ItemSetChangeNotifier) {
                    ((Container.ItemSetChangeNotifier) items)
                            .removeItemSetChangeListener(this);
                }
                if (items instanceof Container.PropertySetChangeNotifier) {
                    ((Container.PropertySetChangeNotifier) items)
                            .removePropertySetChangeListener(this);
                }
            }

            // Assigns new data source
            items = newDataSource;

            // Clears itemIdMapper also
            itemIdMapper.removeAll();

            // Adds listeners
            if (items != null) {
                if (items instanceof Container.ItemSetChangeNotifier) {
                    ((Container.ItemSetChangeNotifier) items)
                            .addItemSetChangeListener(this);
                }
                if (items instanceof Container.PropertySetChangeNotifier) {
                    ((Container.PropertySetChangeNotifier) items)
                            .addPropertySetChangeListener(this);
                }
            }

            /*
             * We expect changing the data source should also clean value. See
             * #810, #4607, #5281
             */
            setValue(null);

            updateOptions(true);
        }
    }

    /**
     * Gets the viewing data-source container.
     * 
     * @see com.vaadin.data.Container.Viewer#getContainerDataSource()
     */
    @Override
    public Container getContainerDataSource() {
        return items;
    }

    /* Select attributes */

    /**
     * Is the select in multiselect mode? In multiselect mode
     * 
     * @return the Value of property multiSelect.
     */
    public boolean isMultiSelect() {
        return false;
    }

    /**
     * Sets the multiselect mode. Setting multiselect mode false may lose
     * selection information: if selected items set contains one or more
     * selected items, only one of the selected items is kept as selected.
     * 
     * Subclasses of AbstractSelect can choose not to support changing the
     * multiselect mode, and may throw {@link UnsupportedOperationException}.
     * 
     * @param multiSelect
     *            the New value of property multiSelect.
     */
    public void setMultiSelect(boolean multiSelect) {
        throw new UnsupportedOperationException("Multiselect not supported");
    }

    /**
     * Does the select allow adding new options by the user. If true, the new
     * options can be added to the Container. The text entered by the user is
     * used as id. Note that data-source must allow adding new items.
     * 
     * @return True if additions are allowed.
     */
    public boolean isNewItemsAllowed() {
        return allowNewOptions;
    }

    /**
     * Enables or disables possibility to add new options by the user.
     * 
     * @param allowNewOptions
     *            the New value of property allowNewOptions.
     */
    public void setNewItemsAllowed(boolean allowNewOptions) {

        // Only handle change requests
        if (this.allowNewOptions != allowNewOptions) {

            this.allowNewOptions = allowNewOptions;

            getState().setNewItemsAllowed(allowNewOptions);
        }
    }

    /**
     * Override the caption of an item. Setting caption explicitly overrides id,
     * item and index captions.
     * 
     * @param itemId
     *            the id of the item to be recaptioned.
     * @param caption
     *            the New caption.
     */
    public void setItemCaption(Object itemId, String caption) {
        if (itemId != null) {
            itemCaptions.put(itemId, caption);
            updateOptions(true);
        }
    }

    /**
     * Gets the caption of an item. The caption is generated as specified by the
     * item caption mode. See <code>setItemCaptionMode()</code> for more
     * details.
     * 
     * @param itemId
     *            the id of the item to be queried.
     * @return the caption for specified item.
     */
    public String getItemCaption(Object itemId) {

        // Null items can not be found
        if (itemId == null) {
            return null;
        }

        String caption = null;

        switch (getItemCaptionMode()) {

        case ID:
            caption = itemId.toString();
            break;

        case INDEX:
            if (items instanceof Container.Indexed) {
                caption = String.valueOf(((Container.Indexed) items)
                        .indexOfId(itemId));
            } else {
                caption = "ERROR: Container is not indexed";
            }
            break;

        case ITEM:
            final Item i = getItem(itemId);
            if (i != null) {
                caption = i.toString();
            }
            break;

        case EXPLICIT:
            caption = itemCaptions.get(itemId);
            break;

        case EXPLICIT_DEFAULTS_ID:
            caption = itemCaptions.get(itemId);
            if (caption == null) {
                caption = itemId.toString();
            }
            break;

        case PROPERTY:
            final Property<?> p = getContainerProperty(itemId,
                    getItemCaptionPropertyId());
            if (p != null) {
                Object value = p.getValue();
                if (value != null) {
                    caption = value.toString();
                }
            }
            break;
        }

        // All items must have some captions
        return caption != null ? caption : "";
    }

    /**
     * Sets tqhe icon for an item.
     * 
     * @param itemId
     *            the id of the item to be assigned an icon.
     * @param icon
     *            the icon to use or null.
     */
    public void setItemIcon(Object itemId, Resource icon) {
        if (itemId != null) {
            if (icon == null) {
                itemIcons.remove(itemId);
            } else {
                itemIcons.put(itemId, icon);
            }
            updateOptions(true);
        }
    }

    /**
     * Gets the item icon.
     * 
     * @param itemId
     *            the id of the item to be assigned an icon.
     * @return the icon for the item or null, if not specified.
     */
    public Resource getItemIcon(Object itemId) {
        final Resource explicit = itemIcons.get(itemId);
        if (explicit != null) {
            return explicit;
        }

        if (getItemIconPropertyId() == null) {
            return null;
        }

        final Property<?> ip = getContainerProperty(itemId,
                getItemIconPropertyId());
        if (ip == null) {
            return null;
        }
        final Object icon = ip.getValue();
        if (icon instanceof Resource) {
            return (Resource) icon;
        }

        return null;
    }

    /**
     * Sets the item caption mode.
     * 
     * <p>
     * The mode can be one of the following ones:
     * <ul>
     * <li><code>ITEM_CAPTION_MODE_EXPLICIT_DEFAULTS_ID</code> : Items
     * Id-objects <code>toString</code> is used as item caption. If caption is
     * explicitly specified, it overrides the id-caption.
     * <li><code>ITEM_CAPTION_MODE_ID</code> : Items Id-objects
     * <code>toString</code> is used as item caption.</li>
     * <li><code>ITEM_CAPTION_MODE_ITEM</code> : Item-objects
     * <code>toString</code> is used as item caption.</li>
     * <li><code>ITEM_CAPTION_MODE_INDEX</code> : The index of the item is used
     * as item caption. The index mode can only be used with the containers
     * implementing <code>Container.Indexed</code> interface.</li>
     * <li><code>ITEM_CAPTION_MODE_EXPLICIT</code> : The item captions must be
     * explicitly specified.</li>
     * <li><code>ITEM_CAPTION_MODE_PROPERTY</code> : The item captions are read
     * from property, that must be specified with
     * <code>setItemCaptionPropertyId</code>.</li>
     * </ul>
     * The <code>ITEM_CAPTION_MODE_EXPLICIT_DEFAULTS_ID</code> is the default
     * mode.
     * </p>
     * 
     * @param mode
     *            the One of the modes listed above.
     */
    public void setItemCaptionMode(ItemCaptionMode mode) {
        if (mode != null) {
            itemCaptionMode = mode;
            updateOptions(true);
        }
    }

    /**
     * Gets the item caption mode.
     * 
     * <p>
     * The mode can be one of the following ones:
     * <ul>
     * <li><code>ITEM_CAPTION_MODE_EXPLICIT_DEFAULTS_ID</code> : Items
     * Id-objects <code>toString</code> is used as item caption. If caption is
     * explicitly specified, it overrides the id-caption.
     * <li><code>ITEM_CAPTION_MODE_ID</code> : Items Id-objects
     * <code>toString</code> is used as item caption.</li>
     * <li><code>ITEM_CAPTION_MODE_ITEM</code> : Item-objects
     * <code>toString</code> is used as item caption.</li>
     * <li><code>ITEM_CAPTION_MODE_INDEX</code> : The index of the item is used
     * as item caption. The index mode can only be used with the containers
     * implementing <code>Container.Indexed</code> interface.</li>
     * <li><code>ITEM_CAPTION_MODE_EXPLICIT</code> : The item captions must be
     * explicitly specified.</li>
     * <li><code>ITEM_CAPTION_MODE_PROPERTY</code> : The item captions are read
     * from property, that must be specified with
     * <code>setItemCaptionPropertyId</code>.</li>
     * </ul>
     * The <code>ITEM_CAPTION_MODE_EXPLICIT_DEFAULTS_ID</code> is the default
     * mode.
     * </p>
     * 
     * @return the One of the modes listed above.
     */
    public ItemCaptionMode getItemCaptionMode() {
        return itemCaptionMode;
    }

    /**
     * Sets the item caption property.
     * 
     * <p>
     * Setting the id to a existing property implicitly sets the item caption
     * mode to <code>ITEM_CAPTION_MODE_PROPERTY</code>. If the object is in
     * <code>ITEM_CAPTION_MODE_PROPERTY</code> mode, setting caption property id
     * null resets the item caption mode to
     * <code>ITEM_CAPTION_EXPLICIT_DEFAULTS_ID</code>.
     * </p>
     * <p>
     * Note that the type of the property used for caption must be String
     * </p>
     * <p>
     * Setting the property id to null disables this feature. The id is null by
     * default
     * </p>
     * .
     * 
     * @param propertyId
     *            the id of the property.
     * 
     */
    public void setItemCaptionPropertyId(Object propertyId) {
        if (propertyId != null) {
            itemCaptionPropertyId = propertyId;
            setItemCaptionMode(ItemCaptionMode.PROPERTY);
            updateOptions(true);
        } else {
            itemCaptionPropertyId = null;
            if (getItemCaptionMode() == ItemCaptionMode.PROPERTY) {
                setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
            }
            updateOptions(true);
        }
    }

    /**
     * Gets the item caption property.
     * 
     * @return the Id of the property used as item caption source.
     */
    public Object getItemCaptionPropertyId() {
        return itemCaptionPropertyId;
    }

    /**
     * Sets the item icon property.
     * 
     * <p>
     * If the property id is set to a valid value, each item is given an icon
     * got from the given property of the items. The type of the property must
     * be assignable to Resource.
     * </p>
     * 
     * <p>
     * Note : The icons set with <code>setItemIcon</code> function override the
     * icons from the property.
     * </p>
     * 
     * <p>
     * Setting the property id to null disables this feature. The id is null by
     * default
     * </p>
     * .
     * 
     * @param propertyId
     *            the id of the property that specifies icons for items or null
     * @throws IllegalArgumentException
     *             If the propertyId is not in the container or is not of a
     *             valid type
     */
    public void setItemIconPropertyId(Object propertyId)
            throws IllegalArgumentException {
        if (propertyId == null) {
            itemIconPropertyId = null;
        } else if (!getContainerPropertyIds().contains(propertyId)) {
            throw new IllegalArgumentException(
                    "Property id not found in the container");
        } else if (Resource.class.isAssignableFrom(getType(propertyId))) {
            itemIconPropertyId = propertyId;
        } else {
            throw new IllegalArgumentException(
                    "Property type must be assignable to Resource");
        }
        updateOptions(true);
    }

    /**
     * Gets the item icon property.
     * 
     * <p>
     * If the property id is set to a valid value, each item is given an icon
     * got from the given property of the items. The type of the property must
     * be assignable to Icon.
     * </p>
     * 
     * <p>
     * Note : The icons set with <code>setItemIcon</code> function override the
     * icons from the property.
     * </p>
     * 
     * <p>
     * Setting the property id to null disables this feature. The id is null by
     * default
     * </p>
     * .
     * 
     * @return the Id of the property containing the item icons.
     */
    public Object getItemIconPropertyId() {
        return itemIconPropertyId;
    }

    /**
     * Tests if an item is selected.
     * 
     * <p>
     * In single select mode testing selection status of the item identified by
     * {@link #getNullSelectionItemId()} returns true if the value of the
     * property is null.
     * </p>
     * 
     * @param itemId
     *            the Id the of the item to be tested.
     * @see #getNullSelectionItemId()
     * @see #setNullSelectionItemId(Object)
     * 
     */
    public boolean isSelected(Object itemId) {
        if (itemId == null) {
            return false;
        }
        final Object value = getValue();
        return itemId.equals(value == null ? getNullSelectionItemId() : value);
    }

    /**
     * Selects an item.
     * 
     * <p>
     * In single select mode selecting item identified by
     * {@link #getNullSelectionItemId()} sets the value of the property to null.
     * </p>
     * 
     * @param itemId
     *            the identifier of Item to be selected.
     * @see #getNullSelectionItemId()
     * @see #setNullSelectionItemId(Object)
     * 
     */
    public void select(Object itemId) {
        setValue(itemId);
    }

    /**
     * Unselects an item.
     * 
     * @param itemId
     *            the identifier of the Item to be unselected.
     * @see #getNullSelectionItemId()
     * @see #setNullSelectionItemId(Object)
     * 
     */
    public void unselect(Object itemId) {
        if (isSelected(itemId)) {
            setValue(null);
        }
    }

    /**
     * Notifies this listener that the Containers contents has changed.
     * 
     * @see com.vaadin.data.Container.PropertySetChangeListener#containerPropertySetChange(com.vaadin.data.Container.PropertySetChangeEvent)
     */
    @Override
    public void containerPropertySetChange(
            Container.PropertySetChangeEvent event) {
        firePropertySetChange();
    }

    @Override
    public Collection<?> getListeners(Class<?> eventType) {
        if (Container.ItemSetChangeEvent.class.isAssignableFrom(eventType)) {
            if (itemSetEventListeners == null) {
                return Collections.EMPTY_LIST;
            } else {
                return Collections
                        .unmodifiableCollection(itemSetEventListeners);
            }
        } else if (Container.PropertySetChangeEvent.class
                .isAssignableFrom(eventType)) {
            if (propertySetEventListeners == null) {
                return Collections.EMPTY_LIST;
            } else {
                return Collections
                        .unmodifiableCollection(propertySetEventListeners);
            }
        }

        return super.getListeners(eventType);
    }

    /**
     * Lets the listener know a Containers Item set has changed.
     * 
     * @see com.vaadin.data.Container.ItemSetChangeListener#containerItemSetChange(com.vaadin.data.Container.ItemSetChangeEvent)
     */
    @Override
    public void containerItemSetChange(Container.ItemSetChangeEvent event) {
        if (!filteringContainer) {
            // Clears the item id mapping table
            itemIdMapper.removeAll();

            // Notify all listeners
            fireItemSetChange();
        }
    }

    /**
     * Fires the property set change event.
     */
    protected void firePropertySetChange() {
        if (propertySetEventListeners != null
                && !propertySetEventListeners.isEmpty()) {
            final Container.PropertySetChangeEvent event = new PropertySetChangeEvent(
                    this);
            final Object[] listeners = propertySetEventListeners.toArray();
            for (int i = 0; i < listeners.length; i++) {
                ((Container.PropertySetChangeListener) listeners[i])
                        .containerPropertySetChange(event);
            }
        }
        updateOptions(true);
    }

    /**
     * Fires the item set change event.
     */
    protected void fireItemSetChange() {
        if (itemSetEventListeners != null && !itemSetEventListeners.isEmpty()) {
            final Container.ItemSetChangeEvent event = new ItemSetChangeEvent(
                    this);
            final Object[] listeners = itemSetEventListeners.toArray();
            for (int i = 0; i < listeners.length; i++) {
                ((Container.ItemSetChangeListener) listeners[i])
                        .containerItemSetChange(event);
            }
        }
        updateOptions(true);
    }

    /**
     * Implementation of item set change event.
     */
    private static class ItemSetChangeEvent extends EventObject implements
            Serializable, Container.ItemSetChangeEvent {

        /**
         * 
         */
        private static final long serialVersionUID = 7201410824579845680L;

        private ItemSetChangeEvent(Container source) {
            super(source);
        }

        /**
         * Gets the Property where the event occurred.
         * 
         * @see com.vaadin.data.Container.ItemSetChangeEvent#getContainer()
         */
        @Override
        public Container getContainer() {
            return (Container) getSource();
        }

    }

    /**
     * Implementation of property set change event.
     */
    private static class PropertySetChangeEvent extends EventObject implements
            Container.PropertySetChangeEvent, Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = -4762103059777968275L;

        private PropertySetChangeEvent(Container source) {
            super(source);
        }

        /**
         * Retrieves the Container whose contents have been modified.
         * 
         * @see com.vaadin.data.Container.PropertySetChangeEvent#getContainer()
         */
        @Override
        public Container getContainer() {
            return (Container) getSource();
        }

    }

    /**
     * Allow or disallow empty selection by the user. If the select is in
     * single-select mode, you can make an item represent the empty selection by
     * calling <code>setNullSelectionItemId()</code>. This way you can for
     * instance set an icon and caption for the null selection item.
     * 
     * @param nullSelectionAllowed
     *            whether or not to allow empty selection
     * @see #setNullSelectionItemId(Object)
     * @see #isNullSelectionAllowed()
     */
    public void setNullSelectionAllowed(boolean nullSelectionAllowed) {
        if (nullSelectionAllowed != this.nullSelectionAllowed) {
            this.nullSelectionAllowed = nullSelectionAllowed;
            getState().setNullSelectionAllowed(nullSelectionAllowed);
        }
    }

    /**
     * Checks if null empty selection is allowed by the user.
     * 
     * @return whether or not empty selection is allowed
     * @see #setNullSelectionAllowed(boolean)
     */
    public boolean isNullSelectionAllowed() {
        return nullSelectionAllowed;
    }

    /**
     * Returns the item id that represents null value of this select in single
     * select mode.
     * 
     * <p>
     * Data interface does not support nulls as item ids. Selecting the item
     * identified by this id is the same as selecting no items at all. This
     * setting only affects the single select mode.
     * </p>
     * 
     * @return the Object Null value item id.
     * @see #setNullSelectionItemId(Object)
     * @see #isSelected(Object)
     * @see #select(Object)
     */
    public Object getNullSelectionItemId() {
        return nullSelectionItemId;
    }

    /**
     * Sets the item id that represents null value of this select.
     * 
     * <p>
     * Data interface does not support nulls as item ids. Selecting the item
     * identified by this id is the same as selecting no items at all.
     * </p>
     * 
     * @param nullSelectionItemId
     *            the nullSelectionItemId to set.
     * @see #getNullSelectionItemId()
     * @see #isSelected(Object)
     * @see #select(Object)
     */
    public void setNullSelectionItemId(Object nullSelectionItemId) {
        this.nullSelectionItemId = nullSelectionItemId;
        getState().setNullSelectionItemId(createItemState(nullSelectionItemId));
    }

    /**
     * Notifies the component that it is connected to an application.
     * 
     * @see com.vaadin.ui.AbstractField#attach()
     */
    @Override
    public void attach() {
        super.attach();
    }

    /**
     * Detaches the component from application.
     * 
     * @see com.vaadin.ui.AbstractComponent#detach()
     */
    @Override
    public void detach() {
        getCaptionChangeListener().clear();
        super.detach();
    }

    // Caption change listener
    protected CaptionChangeListener getCaptionChangeListener() {
        if (captionChangeListener == null) {
            captionChangeListener = new CaptionChangeListener();
        }
        return captionChangeListener;
    }

    /**
     * This is a listener helper for Item and Property changes that should cause
     * a repaint. It should be attached to all items that are displayed, and the
     * default implementation does this in paintContent(). Especially
     * "lazyloading" components should take care to add and remove listeners as
     * appropriate. Call addNotifierForItem() for each painted item (and
     * remember to clear).
     * 
     * NOTE: singleton, use getCaptionChangeListener().
     * 
     */
    protected class CaptionChangeListener implements
            Item.PropertySetChangeListener, Property.ValueChangeListener {

        private static final long serialVersionUID = 235402023513179607L;
        // TODO clean this up - type is either Item.PropertySetChangeNotifier or
        // Property.ValueChangeNotifier
        HashSet<Object> captionChangeNotifiers = new HashSet<Object>();

        public void addNotifierForItem(Object itemId) {
            switch (getItemCaptionMode()) {
            case ITEM:
                final Item i = getItem(itemId);
                if (i == null) {
                    return;
                }
                if (i instanceof Item.PropertySetChangeNotifier) {
                    ((Item.PropertySetChangeNotifier) i)
                            .addPropertySetChangeListener(getCaptionChangeListener());
                    captionChangeNotifiers.add(i);
                }
                Collection<?> pids = i.getItemPropertyIds();
                if (pids != null) {
                    for (Iterator<?> it = pids.iterator(); it.hasNext();) {
                        Property<?> p = i.getItemProperty(it.next());
                        if (p != null
                                && p instanceof Property.ValueChangeNotifier) {
                            ((Property.ValueChangeNotifier) p)
                                    .addValueChangeListener(getCaptionChangeListener());
                            captionChangeNotifiers.add(p);
                        }
                    }

                }
                break;
            case PROPERTY:
                final Property<?> p = getContainerProperty(itemId,
                        getItemCaptionPropertyId());
                if (p != null && p instanceof Property.ValueChangeNotifier) {
                    ((Property.ValueChangeNotifier) p)
                            .addValueChangeListener(getCaptionChangeListener());
                    captionChangeNotifiers.add(p);
                }
                break;

            }
        }

        public void clear() {
            for (Iterator<Object> it = captionChangeNotifiers.iterator(); it
                    .hasNext();) {
                Object notifier = it.next();
                if (notifier instanceof Item.PropertySetChangeNotifier) {
                    ((Item.PropertySetChangeNotifier) notifier)
                            .removePropertySetChangeListener(getCaptionChangeListener());
                } else {
                    ((Property.ValueChangeNotifier) notifier)
                            .removeValueChangeListener(getCaptionChangeListener());
                }
            }
            captionChangeNotifiers.clear();
        }

        @Override
        public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
            updateOptions(true);
        }

        @Override
        public void itemPropertySetChange(
                com.vaadin.data.Item.PropertySetChangeEvent event) {
            updateOptions(true);
        }

    }

    /* Add/Remove Listener methods */

    @SuppressWarnings("deprecation")
    @Override
    public void addFocusListener(FocusListener listener) {
        addListener(FocusEvent.EVENT_ID, FocusEvent.class, listener,
                FocusListener.focusMethod);
    }

    @Override
    public void removeFocusListener(FocusListener listener) {
        removeListener(FocusEvent.EVENT_ID, FocusEvent.class, listener);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addBlurListener(BlurListener listener) {
        addListener(BlurEvent.EVENT_ID, BlurEvent.class, listener,
                BlurListener.blurMethod);
    }

    @Override
    public void removeBlurListener(BlurListener listener) {
        removeListener(BlurEvent.EVENT_ID, BlurEvent.class, listener);
    }

    /**
     * Adds a new Property set change listener for this Container.
     * 
     * @see com.vaadin.data.Container.PropertySetChangeNotifier#addListener(com.vaadin.data.Container.PropertySetChangeListener)
     */
    @Override
    public void addPropertySetChangeListener(
            Container.PropertySetChangeListener listener) {
        if (propertySetEventListeners == null) {
            propertySetEventListeners = new LinkedHashSet<Container.PropertySetChangeListener>();
        }
        propertySetEventListeners.add(listener);
    }

    /**
     * Removes a previously registered Property set change listener.
     * 
     * @see com.vaadin.data.Container.PropertySetChangeNotifier#removeListener(com.vaadin.data.Container.PropertySetChangeListener)
     */
    @Override
    public void removePropertySetChangeListener(
            Container.PropertySetChangeListener listener) {
        if (propertySetEventListeners != null) {
            propertySetEventListeners.remove(listener);
            if (propertySetEventListeners.isEmpty()) {
                propertySetEventListeners = null;
            }
        }
    }

    /**
     * Adds an Item set change listener for the object.
     * 
     * @see com.vaadin.data.Container.ItemSetChangeNotifier#addListener(com.vaadin.data.Container.ItemSetChangeListener)
     */
    @Override
    public void addItemSetChangeListener(
            Container.ItemSetChangeListener listener) {
        if (itemSetEventListeners == null) {
            itemSetEventListeners = new LinkedHashSet<Container.ItemSetChangeListener>();
        }
        itemSetEventListeners.add(listener);
    }

    /**
     * Removes the Item set change listener from the object.
     * 
     * @see com.vaadin.data.Container.ItemSetChangeNotifier#removeListener(com.vaadin.data.Container.ItemSetChangeListener)
     */
    @Override
    public void removeItemSetChangeListener(
            Container.ItemSetChangeListener listener) {
        if (itemSetEventListeners != null) {
            itemSetEventListeners.remove(listener);
            if (itemSetEventListeners.isEmpty()) {
                itemSetEventListeners = null;
            }
        }
    }

    /* Deprecated listeners */

    /**
     * @deprecated Since 7.0, replaced by
     *             {@link #addFocusListener(FocusListener)}
     **/
    @Override
    @Deprecated
    public void addListener(FocusListener listener) {
        addFocusListener(listener);
    }

    /**
     * @deprecated Since 7.0, replaced by
     *             {@link #removeFocusListener(FocusListener)}
     **/
    @Override
    @Deprecated
    public void removeListener(FocusListener listener) {
        removeFocusListener(listener);

    }

    /**
     * @deprecated Since 7.0, replaced by {@link #addBlurListener(BlurListener)}
     **/
    @Override
    @Deprecated
    public void addListener(BlurListener listener) {
        addBlurListener(listener);
    }

    /**
     * @deprecated Since 7.0, replaced by
     *             {@link #removeBlurListener(BlurListener)}
     **/
    @Override
    @Deprecated
    public void removeListener(BlurListener listener) {
        removeBlurListener(listener);
    }

    /**
     * @deprecated Since 7.0, replaced by
     *             {@link #addPropertySetChangeListener(com.vaadin.data.Container.PropertySetChangeListener)}
     **/
    @Override
    @Deprecated
    public void addListener(Container.PropertySetChangeListener listener) {
        addPropertySetChangeListener(listener);
    }

    /**
     * @deprecated Since 7.0, replaced by
     *             {@link #removePropertySetChangeListener(com.vaadin.data.Container.PropertySetChangeListener)}
     **/
    @Override
    @Deprecated
    public void removeListener(Container.PropertySetChangeListener listener) {
        removePropertySetChangeListener(listener);
    }

    /**
     * @deprecated Since 7.0, replaced by
     *             {@link #addItemSetChangeListener(com.vaadin.data.Container.ItemSetChangeListener)}
     **/
    @Override
    @Deprecated
    public void addListener(Container.ItemSetChangeListener listener) {
        addItemSetChangeListener(listener);
    }

    /**
     * @deprecated Since 7.0, replaced by
     *             {@link #removeItemSetChangeListener(com.vaadin.data.Container.ItemSetChangeListener)}
     **/
    @Override
    @Deprecated
    public void removeListener(Container.ItemSetChangeListener listener) {
        removeItemSetChangeListener(listener);
    }
}
