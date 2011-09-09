package com.vaadin.addon.touchkit.ui;

import com.vaadin.addon.touchkit.gwt.client.VPopover;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;

/**
 * A modal sub window suitable for mobile devices. Often used to quickly display
 * more options or small form related to an action. {@link Popover} does not
 * support dragging or resizing by the end user.
 * <p>
 * Typical use case by example: In a mobile mail application when you hit an
 * icon, you'll see a {@link Popover} with actions: "Reply", "Forward" and
 * "Print". In e.g iPad the actions would be shown "next to" the clicked icon so
 * they are close to the users finger (the related button can be set with
 * {@link #showRelativeTo(Component)}), while on smaller screens (e.g iPhone)
 * this kind of UI element fills the whole width of the screen.
 * <p>
 * Popover can also be made full screen with setSizeFull(). All borders will in
 * this case be hidden and the window will block all other content of the main
 * window. Using full screen subwindow, instead of changing the whole content of
 * the main window may cause a slightly faster return to the original view.
 * <p>
 * Finally, on a pad-sized device, if the window is not full screen nor related
 * to a component, the window will be completely undecorated by default,
 * allowing the content to dictate the look.
 * 
 */
@ClientWidget(value = VPopover.class, loadStyle = LoadStyle.EAGER)
public class Popover extends Window {

    private Component relatedComponent;

    /**
     * Constructs a new Popover. By default, the {@link Popover} is modal. This
     * is usually the most sensible approach on devices.
     */
    public Popover() {
        setModal(true);
    }

    /**
     * Constructs a new Popover with given content.
     * 
     * @param content
     */
    public Popover(ComponentContainer content) {
        this();
        setContent(content);
    }

    /**
     * This method will add the {@link Popover} to the top level window so that
     * it is aligned below or on top of the given component, <em>unless</em>
     * this is a 'small screen' device, e.g phone. By default, an arrow pointing
     * to the given related component will be shown.
     * <p>
     * On a small screen device, a 100% wide overlay will slide in from the
     * bottom or top depending on the given related components position (in an
     * attempt to leave the related component visible, though this is not
     * guaranteed).
     * 
     * @param relatedComponent
     */
    public void showRelativeTo(Component relatedComponent) {
        this.relatedComponent = relatedComponent;
        requestRepaint();
        if (relatedComponent != null && getParent() == null) {
            Window window = relatedComponent.getWindow();
            if (window.getParent() != null) {
                window = window.getParent();
            }
            window.addWindow(this);
        }
    }

    @Override
    public synchronized void paintContent(PaintTarget target)
            throws PaintException {
        super.paintContent(target);
        if (relatedComponent != null) {
            target.addAttribute("rel", relatedComponent);
        }
        if (getWidth() == 100 && getWidthUnits() == UNITS_PERCENTAGE
                && getHeight() == 100 && getHeightUnits() == UNITS_PERCENTAGE) {
            target.addAttribute("fc", true);
        }
    }

    /**
     * Sets whether the {@link Popover} is automatically closed when the user
     * clicks (taps) outside of it. Note that no close button is displayed by
     * default, so some other way to close the window must be arranged if set to
     * <code>false</code>.
     * 
     * @param closable
     *            true if the user can close {@link Popover} by tapping outside
     *            of it
     */
    @Override
    public void setClosable(boolean closable) {
        super.setClosable(closable);
    }

    /**
     * Removes the popover from the parent {@link Window}.
     */
    public void removeFromParent() {
        if (getParent() != null) {
            getParent().removeWindow(this);
        }
    }

}
