package com.vaadin.addons.touchkit.ui;

import com.vaadin.addons.touchkit.gwt.client.VComponentGroup;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.CssLayout;

/**
 * A layout to group controls. Uses margins, white background and rounded
 * corners to visualize the grouping.
 * <p>
 * Relative sizes work as in CssLayout, except if the component has a caption.
 * Components with caption are rendered on the same line with their caption and
 * setting this kind of component 100% width stretches it to consume all the
 * space from captions end to the content areas end.
 * <p>
 * The default theme of ComponentGroup in {@link Orientation#HORIZONTAL} mode,
 * renders components into a "button group" with bit stronger decoration than in
 * the vertical mode.
 */
@ClientWidget(VComponentGroup.class)
public class ComponentGroup extends CssLayout {

	public enum Orientation {
		HORIZONTAL, VERTICAL
	}

	public void setOrientation(Orientation orientation) {
		if (orientation == Orientation.HORIZONTAL) {
			addStyleName("v-tk-componentgroup-h");
			removeStyleName("v-tk-componentgroup");
		} else {
			removeStyleName("v-tk-componentgroup-h");
			addStyleName("v-tk-componentgroup");
		}
	}

	public ComponentGroup() {
		this(Orientation.VERTICAL);
	}

	public ComponentGroup(String caption) {
		this();
		setCaption(caption);
	}

	public ComponentGroup(String caption, Orientation orientation) {
		this(orientation);
		setCaption(caption);
	}

	public ComponentGroup(Orientation orientation) {
		setOrientation(orientation);
		if (orientation == Orientation.VERTICAL) {
			setWidth("100%");
		} else {
			setMargin(false);
		}
	}
}
