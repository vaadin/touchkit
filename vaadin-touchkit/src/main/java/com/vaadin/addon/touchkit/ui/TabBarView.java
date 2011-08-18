package com.vaadin.addon.touchkit.ui;

import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.addon.touchkit.gwt.client.VTabBar;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.client.MouseEventDetails;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
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
     * Adds a new sheet to the {@link TabBarView}, and adds a button representing it
     * to the tab bar.
     * 
     * @param tabContent
     *            the sheet content
     * @return tab 'meta-data' that can be used to configure the tab further
     * @see Tab
     * 
     */
    public Tab addTab(Component tabContent) {
        TabButton tabButton = new TabButton(tabContent);
        toolbar.addComponent(tabButton);
        if (currentComponent == null) {
            setSelectedTab(tabContent);
        }
        tabs.add(tabContent);
        return tabButton;
    }

    /**
     * @deprecated Behavior differs from regular {@link ComponentContainer}s,
     *             use the specialized API instead: {@link #addTab(Component)}
     *             and {@link #removeTab(Component)}
     */
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
    public void removeComponent(Component c) {
        throw new UnsupportedOperationException(
                "Use the specialized API instead.");
    }

    /**
     * Removes the given tab (content and tab-bar button) from the
     * {@link TabSheet}
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
        list.add(currentComponent);
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

}
