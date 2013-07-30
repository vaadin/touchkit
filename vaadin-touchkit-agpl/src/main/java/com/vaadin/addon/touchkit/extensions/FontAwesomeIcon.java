package com.vaadin.addon.touchkit.extensions;

import org.apache.commons.lang.RandomStringUtils;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;

/**
 * FIXME Add be better crafted solution for icon font or consider using Marc's
 * tool (haven't look at it at all). If possible, with icons that suit better
 * for the new theme (= lighter).
 */
public class FontAwesomeIcon {

	public static FontAwesomeIcon HOME = new FontAwesomeIcon("F015");
	public static FontAwesomeIcon SAVE = new FontAwesomeIcon("f0c7");
	public static FontAwesomeIcon SHARE = new FontAwesomeIcon("f14d");
	public static FontAwesomeIcon SITEMAP = new FontAwesomeIcon("f0e8");
	public static FontAwesomeIcon TRASH = new FontAwesomeIcon("f014");
	public static FontAwesomeIcon PRINT = new FontAwesomeIcon("f02f");
	public static FontAwesomeIcon BAR_CHART = new FontAwesomeIcon("f080");
	public static FontAwesomeIcon PLUS = new FontAwesomeIcon("f067");

	private String utf;

	private FontAwesomeIcon(String utfcode) {
		this.utf = utfcode;
	};

	public void add(AbstractComponent c) {
		String stylename = "fa-" + RandomStringUtils.randomAlphabetic(4);
		c.addStyleName(stylename);
		StringBuilder sb = new StringBuilder(".");
		sb.append(stylename);
		if (c instanceof Button) {
			sb.append(" .v-button-wrap");
		}
		sb.append(":before");
		sb.append(" { font-family: 'TkIcons' ;content:\"\\");
		sb.append(utf);
		sb.append("\";");
		if (c instanceof NavigationButton) {
			sb.append("font-size:20px;font-weight:normal; vertical-align:middle; margin-right:5px;");
		} else if (c instanceof Button) {
			sb.append("font-size:24px;font-weight:normal; line-height:45px;");
		}
		sb.append("}");
		if (c instanceof Button) {
			sb.append(" .v-touchkit-navbar ");
			sb.append(".");
			sb.append(stylename);
			sb.append(" .v-button-wrap:before {");
			sb.append("line-height:24px;vertical-align:-4px;");
			sb.append("}");
		}

		Page.getCurrent().getStyles().add(sb.toString());
	}
}
