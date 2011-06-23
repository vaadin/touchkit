package com.vaadin.addons.touchkit.ui;

import com.vaadin.addons.touchkit.gwt.client.VPopover;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

/**
 * A modal sub window suitable for mobile devices. Most commonly this kind of
 * window contains no caption, but instead just quickly displays more options or
 * small form related to an action. Popover does not support dragging
 * or resizing by the end user.
 * <p>
 * Typical use case by example: In ios mail when you hit the arrow (the
 * "reply logo") you'll see option of actions: "Reply", "Forward" and "Print".
 * In iPad actions are shown "next to" the clicked button so they are close to
 * users finger. The related button can be set with
 * {@link #showRelativeTo(Component)} method. In smaller screens this kind of UI
 * element fills the whole width of the screan.
 * <p>
 * Popover can also be made full screen with setSizeFull(). All
 * borders will in this case be hidden and the window will block all other
 * content of the main window. Using full screen subwindow, instead of changing
 * the whole content of the main window may cause a slightly faster return to
 * the original view.
 * 
 * TODO implement
 * 
 */
@ClientWidget(VPopover.class)
public class Popover extends Window {

	public Popover() {
		setModal(true);
	}

	private Component relatedComponent;

	public void showRelativeTo(Component relatedComponent) {
		this.relatedComponent = relatedComponent;
		requestRepaint();
		if (relatedComponent != null && getParent() == null) {
			Window window = relatedComponent.getWindow();
			if(window.getParent() != null) {
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
	 * Sets wheter the sub window is closed if clicked outside it. Note, that no
	 * close box is rendered as in a normal window.
	 */
	@Override
	public void setClosable(boolean closable) {
		super.setClosable(closable);
	}

}
