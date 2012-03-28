package com.vaadin.addon.touchkit.ui;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.addon.touchkit.gwt.client.VTabBar;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.gwt.client.MouseEventDetails;
import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet.Tab;

/**
 * A tabsheet implementation that mimics the native tabsheet (TabBarView) in e.g
 * iOS. By default a bar with equally sized tab buttons is shown below the tab
 * content.
 */
@ClientWidget(value = VTabBar.class, loadStyle = LoadStyle.EAGER)
public class TabBarView extends AbstractComponentContainer {

    private Toolbar toolbar = new Toolbar();
    private Component currentComponent;

    private LinkedList<Component> tabs = new LinkedList<Component>();

    /**
     * Creates a {@link TabBarView} that is 100% wide and high.
     */
    public TabBarView() {
        super();
        setSizeFull();
        toolbar.setHeight("46px");
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
     * @deprecated Behavior differs from regular {@link ComponentContainer}s,
     *             use the specialized API instead: {@link #addTab(Component)}
     *             and {@link #removeTab(Component)}
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
        if (tabButton != null) {
            toolbar.removeComponent(tabButton);
            if (c == currentComponent) {
                super.removeComponent(c);
                Component next = getComponentIterator().next();
                if (next != null) {
                    setSelectedTab(next);
                }
            }
        }
        tabs.remove(c);

    }

    /**
     * @deprecated Behavior differs from regular {@link ComponentContainer}s,
     *             use the specialized API instead: {@link #addTab(Component)}
     *             and {@link #removeTab(Component)}
     */
    @Deprecated
    @Override
    public void removeComponent(Component c) {
        throw new UnsupportedOperationException(
                "Use the specialized API instead.");
    }

    /**
     * Removes the given tab (content and tab-bar button) from the
     * {@link TabBarView}
     * 
     * @param tab
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
            getTabButton(currentComponent).setSelected(false);
            super.removeComponent(currentComponent);
        }
        super.addComponent(tab);
        currentComponent = tab;
        getTabButton(currentComponent).setSelected(true);
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
     * @deprecated Behavior differs from regular {@link ComponentContainer}s,
     *             use the specialized API instead: {@link #addTab(Component)}
     *             and {@link #removeTab(Component)}
     */
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

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        toolbar.paint(target);
        currentComponent.paint(target);

    }

    private class TabButton extends Button implements Tab {

        private Component relatedComponent;

        public TabButton(Component tab1) {
            relatedComponent = tab1;
            setCaption(tab1.getCaption());
        }

        public void setSelected(boolean b) {
            if (b) {
                addStyleName("selected");
            } else {
                removeStyleName("selected");
            }
        }

        public boolean isClosable() {
            return false;
        }

        public void setClosable(boolean closable) {
            throw new UnsupportedOperationException();
        }

        public Component getComponent() {
            return relatedComponent;
        }

        @Override
        protected void fireClick() {
            setSelectedTab(getComponent());
            super.fireClick();
        }

        @Override
        protected void fireClick(MouseEventDetails details) {
            setSelectedTab(getComponent());
            super.fireClick(details);
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
         * New instance of selected tab change event
         * 
         * @param source
         *            the Source of the event.
         */
        public SelectedTabChangeEvent(TabBarView source) {
            super(source);
        }

        /**
         * TabSheet where the event occurred.
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

}
