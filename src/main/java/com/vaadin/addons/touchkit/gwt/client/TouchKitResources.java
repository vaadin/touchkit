package com.vaadin.addons.touchkit.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public interface TouchKitResources extends ClientBundle {
	public static final TouchKitResources INSTANCE = GWT
			.create(TouchKitResources.class);

	/**
	 * TODO consider splitting this per components/related components
	 * @return
	 */
	@NotStrict
	@Source("touchkit.css")
	public CssResource css();

	@Source("back_button.png")
	public DataResource backButtonImage();

	@Source("navbar-action.png")
	public DataResource navBarActionImage();

	@Source("background_stripes.png")
	public DataResource backgroundStripesImage();

	@Source("header_middle.png")
	@ImageOptions(repeatStyle = RepeatStyle.Horizontal)
	public ImageResource toolbarBackground();

	@Source("slider-handle.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	public ImageResource sliderHandle();

	@Source("modalWindowArrow.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	public ImageResource modalWindowArrow();

	@Source("modalWindowArrowDown.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	public ImageResource modalWindowArrowDown();
	
	@Source("switch.png")
	@ImageOptions(repeatStyle = RepeatStyle.None)
	public ImageResource switchImage();


}