package org.vaadin.touchkit.ui;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.util.ReflectTools;

/**
 * The TabBarView is a tabsheet implementation that mimics the native tabsheet
 * (TabBarView) in iOS. By default a bar with equally sized tab buttons is shown
 * below the tab content.
 */
public class TabBarView extends AbstractComponentContainer {

    private Toolbar toolbar = new Toolbar();
    private Component currentComponent;

    private LinkedList<Component> tabs = new LinkedList<Component>();

    /**
     * Constructs a {@link TabBarView} that is 100% wide and high.
     */
    public TabBarView() {
        super();
        setSizeFull();
        super.addComponent(toolbar);
    }

    /**
     * Adds a new sheet to the {@link TabBarView}, and adds a button
     * representing it to the tab bar. The button gets it's caption and icon
     * from the content.
     * 
     * @param tabContent
     *            the sheet content
     * @return tab 'meta-data' that can be used to configure the tab further
     * @see Tab
     * 
     */
    public Tab addTab(Component tabContent) {
        return addTab(tabContent, tabContent.getCaption(), tabContent.getIcon());
    }

    /**
     * Adds a new sheet to the {@link TabBarView}, and adds a button
     * representing it to the tab bar. The button gets the given caption and no
     * icon.
     * 
     * @param tabContent
     *            the sheet content
     * @param caption
     *            the caption to be used in the tab bar button
     * @return tab 'meta-data' that can be used to configure the tab further
     * @see Tab
     * 
     */
    public Tab addTab(Component tabContent, String caption) {
        return addTab(tabContent, caption, null);
    }

    /**
     * Adds a new sheet to the {@link TabBarView}, and adds a button
     * representing it to the tab bar. The button gets the given caption and
     * icon.
     * 
     * @param tabContent
     *            the sheet content
     * @param caption
     *            the caption to be used in the tab bar button
     * @param icon
     *            the icon to be used in the tab bar button
     * @return tab 'meta-data' that can be used to configure the tab further
     * @see Tab
     * 
     */
    public Tab addTab(Component tabContent, String caption, Resource icon) {
        TabButton tabButton = new TabButton(tabContent);
        toolbar.addComponent(tabButton);
        if (currentComponent == null) {
            setSelectedTab(tabContent);
        }
        tabs.add(tabContent);
        tabButton.setCaption(caption);
        tabButton.setIcon(icon);
        return tabButton;
    }

    /**
     * @deprecated The behavior differs from regular {@link ComponentContainer}
     *             s, use the specialized API instead:
     *             {@link #addTab(Component)} and {@link #removeTab(Component)}
     * 
     * @throws UnsupportedOperationException
     */
    @Deprecated
    @Override
    public void addComponent(Component c) {
        throw new UnsupportedOperationException(
                "Use the specialized API instead.");
    }

    /**
     * Removes the given tab content and the tab button associated with it.
     * 
     * @param c
     *            the tab content to remove
     */
    public void removeTab(Component c) {
        TabButton tabButton = getTabButton(c);
        tabs.remove(c);
        if (tabButton != null) {
            toolbar.removeComponent(tabButton);
            if (c == currentComponent) {
                super.removeComponent(c);

                Component next = tabs.iterator().next();

                if (next != null) {
                    setSelectedTab(next);
                }
            }
        }
    }

    /**
     * @deprecated The behavior differs from regular {@link ComponentContainer}
     *             s, use the specialized API instead:
     *             {@link #addTab(Component)} and {@link #removeTab(Component)}
     * 
     * @throws UnsupportedOperationException
     */
    @Deprecated
    @Override
    public void removeComponent(Component c) {
        throw new UnsupportedOperationException(
                "Use the specialized API instead.");
    }

    /**
     * Removes the given tab (content and tab-bar button) from the TabBarView
     * 
     * @param tab
     *            the tab to remove
     */
    public void removeTab(Tab tab) {
        removeTab(tab.getComponent());
    }

    /**
     * Sets the active tab, i.e. sets the content as currently visible and marks
     * its button in the tab-bar as active.
     * 
     * @param tab
     *            the content to show
     */
    public void setSelectedTab(Component tab) {
        if (currentComponent != null) {
            TabButton tabButton = getTabButton(currentComponent);
            if (tabButton != null) {
                tabButton.setSelected(false);
            }
            super.removeComponent(currentComponent);
        }
        super.addComponent(tab);
        currentComponent = tab;
        getTabButton(currentComponent).setSelected(true);
        markAsDirty();
        fireSelectedTabChange();

    }

    /**
     * Sets the active {@link Tab}, i.e. sets the tab content as currently
     * visible and marks its button in the tab-bar as active.
     * 
     * @param tab
     *            the {@link Tab} to show
     */
    public void setSelectedTab(Tab tab) {
        setSelectedTab(tab.getComponent());
    }

    /**
     * @return the currently selected Tab
     */
    public Tab getSelelectedTab() {
        return getTabButton(currentComponent);
    }

    /**
     * @deprecated The behavior differs from regular {@link ComponentContainer}
     *             s, use the specialized API instead:
     *             {@link #addTab(Component)} and {@link #removeTab(Component)}
     * 
     * @throws UnsupportedOperationException
     */
    @Override
    @Deprecated
    public void replaceComponent(Component oldComponent, Component newComponent) {
        throw new UnsupportedOperationException(
                "Use the specialized API instead.");
    }

    /**
     * Otherwise as {@link ComponentContainer#getComponentIterator()}, but note
     * that a tab is not in the component hierarchy before it is activated, even
     * tough it has been added with {@link #addTab(Component)}.
     * 
     * @see ComponentContainer#getComponentIterator()
     */
    @Override
    public Iterator<Component> getComponentIterator() {
        LinkedList<Component> list = new LinkedList<Component>();
        list.add(toolbar);
        if (currentComponent != null) {
            list.add(currentComponent);
        }
        return list.iterator();
    }

    private TabButton getTabButton(Component tab) {
        Iterator<Component> componentIterator = toolbar.getComponentIterator();
        while (componentIterator.hasNext()) {
            TabButton next = (TabButton) componentIterator.next();
            if (next.getComponent() == tab) {
                return next;
            }
        }
        return null;
    }

    private class TabButton extends Button implements Tab {

        private Component relatedComponent;

        public TabButton(Component tab1) {
            relatedComponent = tab1;
            setCaption(tab1.getCaption());

            addClickListener(new ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    setSelectedTab(getComponent());
                }
            });
        }

        public void setSelected(boolean b) {
            if (b) {
                addStyleName("selected");
            } else {
                removeStyleName("selected");
            }
        }

        @Override
        public boolean isClosable() {
            return false;
        }

        @Override
        public void setClosable(boolean closable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Component getComponent() {
            return relatedComponent;
        }

        @Override
        public void setDefaultFocusComponent(Focusable component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Focusable getDefaultFocusComponent() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    private static final Method SELECTED_TAB_CHANGE_METHOD;
    static {
        SELECTED_TAB_CHANGE_METHOD = ReflectTools.findMethod(
                SelectedTabChangeListener.class, "selectedTabChange",
                new Class[] { SelectedTabChangeEvent.class });
    }

    /**
     * Selected tab change event. This event is sent when the selected (shown)
     * tab in the tab sheet is changed.
     * 
     * @author Vaadin Ltd.
     */
    public static class SelectedTabChangeEvent extends Component.Event {

        /**
         * Constructs a new instance of SelectedTabChangeEvent
         * 
         * @param source
         *            the Source of the event.
         */
        public SelectedTabChangeEvent(TabBarView source) {
            super(source);
        }

        /**
         * Gets the TabSheet where the event occurred.
         * 
         * @return the Source of the event.
         */
        public TabBarView getTabSheet() {
            return (TabBarView) getSource();
        }
    }

    /**
     * Selected tab change event listener. The listener is called whenever
     * another tab is selected, including when adding the first tab to a
     * tabsheet.
     * 
     * @author Vaadin Ltd.
     * 
     */
    public interface SelectedTabChangeListener extends Serializable {

        /**
         * Selected (shown) tab in tab sheet has has been changed.
         * 
         * @param event
         *            the selected tab change event.
         */
        public void selectedTabChange(SelectedTabChangeEvent event);
    }

    /**
     * Adds a tab selection listener
     * 
     * @param listener
     *            the Listener to be added.
     */
    public void addListener(SelectedTabChangeListener listener) {
        addListener(SelectedTabChangeEvent.class, listener,
                SELECTED_TAB_CHANGE_METHOD);
    }

    /**
     * Removes a tab selection listener
     * 
     * @param listener
     *            the Listener to be removed.
     */
    public void removeListener(SelectedTabChangeListener listener) {
        removeListener(SelectedTabChangeEvent.class, listener,
                SELECTED_TAB_CHANGE_METHOD);
    }

    /**
     * Sends an event that the currently selected tab has changed.
     */
    protected void fireSelectedTabChange() {
        fireEvent(new SelectedTabChangeEvent(this));
    }

    @Override
    public int getComponentCount() {
        return 1 + (currentComponent != null ? 1 : 0);
    }

    @Override
    public Iterator<Component> iterator() {
        return getComponentIterator();
    }

}
