package com.vaadin.addon.touchkit.ui;

import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.addon.touchkit.gwt.client.vcom.VerticalComponentGroupState;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;

/**
 * A layout to group controls vertically. Items in a
 * {@link VerticalComponentGroup} have by default white background, margins and
 * rounded corners.
 * <p>
 * Captions are rendered on the same row as the component. Relative widths are
 * relative to the {@link VerticalComponentGroup} width except if the component
 * has a caption, in which case a relative width is relative to the remaining
 * available space.
 * <p>
 * Due to the styling, {@link VerticalComponentGroup} is by default more
 * flexible than {@link HorizontalButtonGroup} and it can accommodate many
 * components.
 */
@SuppressWarnings("serial")
public class VerticalComponentGroup extends AbstractLayout {

    protected LinkedList<Component> components = new LinkedList<Component>();

    /**
     * Creates a vertical component group.
     * <p>
     * The default width is 100%.
     */
    public VerticalComponentGroup() {
        this(null);
    }

    /**
     * Creates a vertical component group that is 100% wide.
     */
    public VerticalComponentGroup(String caption) {
        getState().caption = caption;
        setWidth(null);
    }

    protected VerticalComponentGroupState getState() {
        return (VerticalComponentGroupState) super.getState();
    }

    @Override
    public void addComponent(Component component) {
        addComponent(component, -1);
    }

    public void addComponent(Component component, int index) {
        if (components.contains(component)) {
            if (components.indexOf(component) != index) {
                components.remove(component);
                if (index >= 0 && index < components.size()) {
                    components.add(index, component);
                } else {
                    components.add(component);
                }
                markAsDirty();
            }
        } else {
            if (index >= 0 && index < components.size()) {
                components.add(index, component);
            } else {
                components.add(component);
            }
            super.addComponent(component);
            markAsDirty();
        }
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        int index = components.indexOf(oldComponent);
        removeComponent(oldComponent);

        addComponent(newComponent, index);
    }

    @Override
    public void removeComponent(Component component) {
        if (components.contains(component)) {
            components.remove(component);
            super.removeComponent(component);
            markAsDirty();
        }
    }

    @Override
    public int getComponentCount() {
        return components.size();
    }

    @Override
    @Deprecated
    public Iterator<Component> getComponentIterator() {
        return components.iterator();
    }

    @Override
    public Iterator<Component> iterator() {
        return getComponentIterator();
    }
}
