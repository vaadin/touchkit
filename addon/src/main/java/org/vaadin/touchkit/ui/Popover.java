package org.vaadin.touchkit.ui;

import org.vaadin.touchkit.gwt.client.vcom.popover.PopoverRpc;
import org.vaadin.touchkit.gwt.client.vcom.popover.PopoverState;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * The Popover is a modal sub window suitable for mobile devices. It is often
 * used to quickly display more options or small form related to an action.
 * Popover does not support dragging or resizing by the end user.
 * <p>
 * An example of a typical use case would be the following: In a mobile mail
 * application when you hit an icon, you'll see a {@link Popover} with actions:
 * "Reply", "Forward" and "Print". E.g. on the iPad the actions would be shown
 * next to the touched icon so they are close to the users finger (the related
 * button can be set with {@link #showRelativeTo(Component)}), while on smaller
 * screens (e.g. the iPhone) this kind of UI element fills the whole width of
 * the screen.
 * <p>
 * A Popover can also be made full screen with {@link #setSizeFull()}. In this
 * case, all borders will be hidden and the window will block all other content
 * of the main window. Using a full screen subwindow, instead of changing the
 * whole content of the main window may cause a slightly faster return to the
 * original view.
 * <p>
 * Finally, on a pad-sized device, if the window is not full screen nor related
 * to a component, it will be completely undecorated by default, allowing the
 * content to dictate the look.
 */
public class Popover extends Window {

    private PopoverRpc rpc = new PopoverRpc() {
        @Override
        public void close() {
            Popover.this.close();
        }
    };

    /**
     * Constructs a new Popover. By default, the Popover is modal. This is
     * usually the most sensible approach on devices.
     */
    public Popover() {
        setContent(new VerticalLayout());
        setModal(true);
        registerRpc(rpc);
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        if (getWidth() == 100 && getWidthUnits() == Unit.PERCENTAGE
                && getHeight() == 100 && getHeightUnits() == Unit.PERCENTAGE) {
            getState().setFullscreen(true);
        }
        super.beforeClientResponse(initial);
    }

    /**
     * Constructs a new Popover with the given content.
     * 
     * @param content
     *            the content for the popover.
     */
    public Popover(ComponentContainer content) {
        this();
        setContent(content);
    }

    @Override
    public PopoverState getState() {
        return (PopoverState) super.getState();
    }

    /**
     * This method will add the Popover to the top level window so that it is
     * aligned below or on top of the given component, <em>unless</em> this is a
     * small screen device, e.g a smart phone. By default, an arrow pointing to
     * the given related component will be shown.
     * <p>
     * On a small screen device, a 100% wide overlay will slide in from the
     * bottom or top depending on the given related components position (in an
     * attempt to leave the related component visible, though this is not
     * guaranteed).
     * 
     * @param relatedComponent
     *            the component next to which the popover is shown.
     */
    public void showRelativeTo(Component relatedComponent) {
        getState().setRelatedComponent(relatedComponent);
        if (relatedComponent != null && getParent() == null) {
            relatedComponent.getUI().addWindow(this);
        }
        markAsDirty();
    }

    /**
     * Sets whether the Popover is automatically closed when the user clicks
     * (taps) outside of it. Note that no close button is displayed by default,
     * so some other way to close the window must be arranged if set to
     * <code>false</code>.
     * 
     * @param closable
     *            true to allow the user to close the Popover by tapping outside
     *            of it
     */
    @Override
    public void setClosable(boolean closable) {
        super.setClosable(closable);
    }

    /**
     * Removes the popover from the UI.
     */
    public void removeFromParent() {
        if (getParent() != null) {
            getUI().removeWindow(this);
        }
    }

    /**
     * A convenience method that adds a component to the popover's content
     * container.
     * 
     * @param c
     *            the component to add.
     */
    public void addComponent(Component c) {
        Component content2 = getContent();
        if (content2 instanceof ComponentContainer) {
            ComponentContainer contentPanel = (ComponentContainer) content2;
            contentPanel.addComponent(c);
        } else {
            throw new RuntimeException(
                    "Content component is not of type ComponentContainer");
        }
    }

}
