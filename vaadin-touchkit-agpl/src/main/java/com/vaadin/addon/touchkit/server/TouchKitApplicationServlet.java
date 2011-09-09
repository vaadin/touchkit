/*
 * Copyright 2011 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.addon.touchkit.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.Application;
import com.vaadin.addon.touchkit.service.ApplicationIcon;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.ui.Window;

public class TouchKitApplicationServlet extends
		com.vaadin.terminal.gwt.server.ApplicationServlet {

	private Window window;

	@Override
	protected void writeAjaxPage(HttpServletRequest request,
			HttpServletResponse response, Window window, Application application)
			throws IOException, MalformedURLException, ServletException {
		/*
		 * Temporary save window as we may need it if we write e.g. viewport
		 * definitions.
		 */
		this.window = window;
		super.writeAjaxPage(request, response, window, application);
		this.window = null;
	}

	@Override
	protected void writeAjaxPageHtmlHeader(BufferedWriter page, String title,
			String themeUri, HttpServletRequest request) throws IOException {
		super.writeAjaxPageHtmlHeader(page, title, themeUri, request);
		if (window != null && window instanceof TouchKitWindow) {
			TouchKitWindow w = (TouchKitWindow) window;

			boolean viewportOpen = false;
			if (w.getViewPortWidth() != null) {
				viewportOpen = prepareViewPort(viewportOpen, page);
				page.write("width=" + w.getViewPortWidth());
			}
			if (w.isViewPortUserScalable() != null) {
				viewportOpen = prepareViewPort(viewportOpen, page);
				page.write("user-scalable="
						+ (w.isViewPortUserScalable() ? "yes" : "no"));
			}
			if (w.getViewPortInitialScale() != null) {
				viewportOpen = prepareViewPort(viewportOpen, page);
				page.write("initial-scale=" + w.getViewPortInitialScale());
			}
			if (w.getViewPortMaximumScale() != null) {
				viewportOpen = prepareViewPort(viewportOpen, page);
				page.write("maximum-scale=" + w.getViewPortMaximumScale());
			}

			if (w.getViewPortMinimumScale() != null) {
				viewportOpen = prepareViewPort(viewportOpen, page);
				page.write("minimum-scale=" + w.getViewPortMinimumScale());
			}
			if (viewportOpen) {
				closeSingleElementTag(page);
			}

			boolean webAppCapable = w.isWebAppCapable();
			if (webAppCapable) {
				page.write("<meta name=\"apple-mobile-web-app-capable\" "
						+ "content=\"yes\" />\n");
			}

			if (w.getStatusBarStyle() != null) {
				page.append("<meta name=\"apple-mobile-web-app-status-bar-style\" "
						+ "content=\"" + w.getStatusBarStyle() + "\" />\n");
			}
			ApplicationIcon[] icons = w.getApplicationIcons();
			for (int i = 0; i < icons.length; i++) {
				ApplicationIcon icon = icons[i];
				page.write("<link rel=\"apple-touch-icon\" ");
				if (icon.getSizes() != null) {
					page.write("sizes=\"");
					page.write(icon.getSizes());
					page.write("\"");
				}
				page.write(" href=\"");
				page.write(icon.getHref());
				closeSingleElementTag(page);
			}
			if (w.getStartupImage() != null) {
				page.append("<link rel=\"apple-touch-startup-image\" "
						+ "href=\"" + w.getStartupImage() + "\" />");
			}

		}
	}

	private void closeSingleElementTag(BufferedWriter page) throws IOException {
		page.write("\" />\n");
	}

	private boolean prepareViewPort(boolean viewportOpen, BufferedWriter page)
			throws IOException {
		if (viewportOpen) {
			page.write(", ");
		} else {
			page.write("\n<meta name=\"viewport\" content=\"");
		}
		return true;
	}
}
