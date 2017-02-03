package org.vaadin.touchkit.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public void addComponent(Component component) {
        if(component.getParent() == this) {
            removeComponent(component);
        }
        addComponent(component, children.size());
    }

    public void addComponent(Component component, int index) {
        if (children.contains(component)) {
            if (children.indexOf(component) != index) {
                children.remove(component);
                children.add(index, component);
                markAsDirty();
            }
        } else {
            children.add(index, component);
            super.addComponent(component);
        }
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
            super.removeComponent(oldComponent);
            super.addComponent(newComponent);
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
