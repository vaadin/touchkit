package com.vaadin.addons.touchkit.ui;

import java.util.Map;

import com.vaadin.addons.touchkit.gwt.client.VNavigationButton;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Button;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;

/**
 * A Button implementation optimized to be used inside a {@link NavigationManager}
 * .
 * <p>
 * Clicking button will automatically navigate to next view if one is defined in
 * this button (via constructor or {@link #setTargetView(Component)}). On the
 * client side, NavigationManager will start animation immediately when clicked.
 */
@ClientWidget(VNavigationButton.class)
public class NavigationButton extends Button {
	private Component targetView;

	public NavigationButton(String caption) {
		super(caption);
	}

	public NavigationButton(Component targetView) {
		super(targetView.getCaption());
		this.targetView = targetView;
	}

	public NavigationButton() {
	}

	// @Override
	// protected void fireClick() {
	// /*
	// * TODO remove debug timeout
	// */
	// // try {
	// // Thread.sleep(300);
	// // } catch (InterruptedException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// if (targetView != null) {
	// getNavigationPanel().navigateTo(targetView);
	// }
	// super.fireClick();
	// }

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		// FIXME since #6605 click implementation in Button sucks. Fix it then
		// uncomment the previous method or something.
		// super.changeVariables(source, variables);
		if (targetView != null) {
			getNavigationPanel().navigateTo(targetView);
		}
		fireClick();
	}

	/**
	 * @return the navigation panel in which the button is contained or null if
	 *         the button is not inside a {@link NavigationManager}
	 */
	public NavigationManager getNavigationPanel() {
		Component p = getParent();
		while (p != null && !(p instanceof NavigationManager)) {
			p = p.getParent();
		}
		return (NavigationManager) p;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		if (targetView != null && targetView.getApplication() != null) {
			target.addAttribute("nv", targetView);
		}
	}

	public void setTargetView(Component targetView) {
		this.targetView = targetView;
		requestRepaint();
	}

	@Override
	public String getCaption() {
		String caption2 = super.getCaption();
		if(caption2 == null) {
			/*
			 * Use caption from target view unless explicitly set for this button 
			 */
			if (targetView != null && targetView.getCaption() != null) {
				return targetView.getCaption();
			}
		}
		return caption2;
	}

	public Component getTargetView() {
		return targetView;
	}

}
