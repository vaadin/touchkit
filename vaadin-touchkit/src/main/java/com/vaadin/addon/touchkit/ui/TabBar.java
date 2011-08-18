package com.vaadin.addon.touchkit.ui;

import java.util.Iterator;

import com.vaadin.addon.touchkit.gwt.client.VTabBar;
import com.vaadin.terminal.gwt.client.MouseEventDetails;
import com.vaadin.ui.Button;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

/**
 * A tabsheet implementation that mimics the native tabsheet (TabBarView) in e.g
 * iOS. By default a bar with equally sized tab buttons is shown below the tab
 * content.
 */
@ClientWidget(value = VTabBar.class, loadStyle = LoadStyle.EAGER)
public class TabBar extends CssLayout {

    private Toolbar toolbar = new Toolbar();
    private Component currentComponent;

    /**
     * Creates a {@link TabBar} that is 100% wide and high.
     */
    public TabBar() {
        super();
        setSizeFull();
        toolbar.setHeight("46px");
        addComponent(toolbar);
    }

    /**
     * Adds a new sheet to the {@link TabBar}, and adds a button
     * representing it to the tab bar.
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
        return tabButton;
    }

    @Override
    public void removeComponent(Component c) {
        if (c == toolbar) {
            throw new UnsupportedOperationException(
                    "The toolbar cannot be removed from a TabBar");
        }
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

    }

    /**
     * Removes the given tab (content and tab-bar button) from the
     * {@link TabSheet}
     * 
     * @param tab
     */
    public void removeTab(Tab tab) {
        removeComponent(tab.getComponent());
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
        addComponent(tab);
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
