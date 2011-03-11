package com.vaadin.addons.touchkit.ui;

import com.vaadin.addons.touchkit.gwt.client.VNavigationButton;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Button;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;

/**
 * A Button implementation optimized to be used inside a {@link NavigationPanel}
 * .
 * <p>
 * Clicking button will automatically navigate to next view if one is defined in
 * this button (via constructor or {@link #setTargetView(Component)}). On the
 * client side, NavigationPanel will start animation immediately when clicked.
 */
@ClientWidget(VNavigationButton.class)
public class NavigationButton extends Button {
	private Component nextView;

	public NavigationButton(String caption) {
		super(caption);
	}

	public NavigationButton(Component nextView) {
		super(nextView.getCaption());
		this.nextView = nextView;
	}

	public NavigationButton() {
	}

	@Override
	protected void fireClick() {
		/*
		 * TODO remove debug timeout
		 */
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (nextView != null) {
			getNavigationPanel().navigateTo(nextView);
		}
		super.fireClick();
	}

	private NavigationPanel getNavigationPanel() {
		Component p = getParent();
		while (p != null && !(p instanceof NavigationPanel)) {
			p = p.getParent();
		}
		return (NavigationPanel) p;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		if (nextView != null && nextView.getApplication() != null) {
			target.addAttribute("nv", nextView);
		}
	}

	public void setTargetView(Component nextView) {
		this.nextView = nextView;
		requestRepaint();
	}

	@Override
	public String getCaption() {
		if (nextView != null && nextView.getCaption() != null) {
			return nextView.getCaption();
		}
		return super.getCaption();
	}

	public Component getTargetView() {
		return nextView;
	}

}
