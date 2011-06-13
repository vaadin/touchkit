package com.vaadin.addons.touchkit.ui;

import java.util.Iterator;

import com.vaadin.addons.touchkit.gwt.client.VTabsheet;
import com.vaadin.terminal.gwt.client.MouseEventDetails;
import com.vaadin.ui.Button;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet.Tab;

/**
 * A Tabsheet implementation that mimics native tabsheet in ios. E.g. the tabbar
 * is below the main content.
 */
@ClientWidget(VTabsheet.class)
public class Tabsheet extends CssLayout {

    Toolbar tabbar = new Toolbar();
    private Component currentComponent;

    public Tabsheet() {
        super();
        setStyleName("touchkit-tabsbelow");
        setSizeFull();
        tabbar.addStyleName("tabbar");
        tabbar.setHeight("46px");
        addComponent(tabbar);
    }

    public Tab addTab(Component tab1) {
        TabButton tabButton = new TabButton(tab1);
        tabbar.addComponent(tabButton);
        if (currentComponent == null) {
            setSelectedTab(tab1);
        }
        return tabButton;
    }

    @Override
    public void removeComponent(Component c) {
        if (c == tabbar) {
            throw new UnsupportedOperationException(
                    "Tabbar cannot be removed from tabsheet");
        }
        TabButton tabButton = getTabButton(c);
        if (tabButton != null) {
            tabbar.removeComponent(tabButton);
            if (c == currentComponent) {
                super.removeComponent(c);
                Component next = getComponentIterator().next();
                if (next != null) {
                    setSelectedTab(next);
                }
            }
        }

    }

    public void removeTab(Tab tab) {
        removeComponent(tab.getComponent());
    }

    public void setSelectedTab(Component tab1) {
        if (currentComponent != null) {
            getTabButton(currentComponent).setSelected(false);
            super.removeComponent(currentComponent);
        }
        addComponent(tab1);
        currentComponent = tab1;
        getTabButton(currentComponent).setSelected(true);

    }

    private TabButton getTabButton(Component tab1) {
        Iterator<Component> componentIterator = tabbar.getComponentIterator();
        while (componentIterator.hasNext()) {
            TabButton next = (TabButton) componentIterator.next();
            if (next.getComponent() == tab1) {
                return next;
            }
        }
        return null;
    }

    private class TabButton extends Button implements Tab {

        private Component relatedComponent;

        public TabButton(Component tab1) {
            relatedComponent = tab1;
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
