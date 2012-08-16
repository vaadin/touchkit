package com.vaadin.addon.touchkit.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.addon.touchkit.gwt.client.vaadincomm.AbstractComponentGroupState;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout.MarginInfo;

/**
 * Parent class that encapsulates similarities between
 * {@link HorizontalComponentGroup} and {@link VerticalComponentGroup}. Does not
 * support changing orientation on the fly.
 */
public abstract class AbstractComponentGroup extends AbstractComponentContainer {

    protected List<Component> children = new ArrayList<Component>();
    protected MarginInfo margins = new MarginInfo(false);

    @Override
    public AbstractComponentGroupState getState() {
        return (AbstractComponentGroupState) super.getState();

    }

    @Override
    public void addComponent(Component c) {
        children.add(c);
        super.addComponent(c);
        requestRepaint();
    }

    @Override
    public void removeComponent(Component c) {
        children.remove(c);
        super.removeComponent(c);
        requestRepaint();
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        int index = children.indexOf(oldComponent);
        if (index != -1) {
            children.remove(index);
            children.add(index, newComponent);
            fireComponentDetachEvent(oldComponent);
            fireComponentAttachEvent(newComponent);
            requestRepaint();
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

    /**
     * Constructs a {@link AbstractComponentGroup} with the given caption.
     * 
     * @param caption
     *            The caption for the component group
     */
    protected AbstractComponentGroup(String caption) {
        setCaption("");
        getState().setVisibleCaption(caption);
    }

    public void setMargin(boolean enabled) {
        margins.setMargins(enabled);
        getState().setMarginsBitmask(margins.getBitMask());
        requestRepaint();
    }

    public MarginInfo getMargin() {
        return margins;
    }

    public void setMargin(MarginInfo marginInfo) {
        margins.setMargins(marginInfo);
        getState().setMarginsBitmask(margins.getBitMask());
        requestRepaint();
    }

    public void setMargin(boolean topEnabled, boolean rightEnabled,
            boolean bottomEnabled, boolean leftEnabled) {
        margins.setMargins(topEnabled, rightEnabled, bottomEnabled, leftEnabled);
        getState().setMarginsBitmask(margins.getBitMask());
        requestRepaint();
    }
}