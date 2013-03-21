package com.vaadin.addon.touchkit.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.addon.touchkit.gwt.client.vcom.AbstractComponentGroupState;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;

/**
 * The AbstractComponentGroup class is the base class for the
 * {@link HorizontalButtonGroup}.
 * 
 * This class should not be used directly.
 */
public abstract class AbstractComponentGroup extends AbstractComponentContainer {

    protected List<Component> children = new ArrayList<Component>();

    @Override
    public AbstractComponentGroupState getState() {
        return (AbstractComponentGroupState) super.getState();
    }

    /**
     * Constructs a {@link AbstractComponentGroup} with the given caption.
     * 
     * @param caption
     *            The caption for the component group
     */
    protected AbstractComponentGroup(String caption) {
        setCaption(caption);
    }

    @Override
    public void addComponent(Component c) {
        children.add(c);
        super.addComponent(c);
        markAsDirty();
    }

    @Override
    public void removeComponent(Component c) {
        children.remove(c);
        super.removeComponent(c);
        markAsDirty();
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        int index = children.indexOf(oldComponent);
        if (index != -1) {
            children.remove(index);
            children.add(index, newComponent);
            fireComponentDetachEvent(oldComponent);
            fireComponentAttachEvent(newComponent);
            markAsDirty();
        }
    }

    @Override
    public int getComponentCount() {
        return children.size();
    }

    @Override
    public Iterator<Component> getComponentIterator() {
        return children.iterator();
    }

    @Override
    public Iterator<Component> iterator() {
        return getComponentIterator();
    }
}
