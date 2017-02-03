package org.vaadin.touchkit.settings;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;

/**
 * The ViewPortSettings class is responsible for configuring the viewport in the
 * application bootstrap page.
 * 
 * See the <a href=
 * "http://developer.apple.com/library/safari/#documentation/AppleApplications/Reference/SafariHTMLRef/Articles/MetaTags.html%23//apple_ref/doc/uid/TP40008193-SW6"
 * >Apple developer documentation</a> for details.
 */
public class ViewPortSettings implements BootstrapListener {

    private Boolean viewPortUserScalable = false;
    private Float viewPortInitialScale = 1f;
    private Float viewPortMinimumScale = null;
    private Float viewPortMaximumScale = null;
    private String viewPortWidth = null;

    /**
     * Sets whether the user should be allowed to zoom the content. The default
     * value for TouchKit apps is false, as we expect that applications are
     * designed for smaller/all devices.
     * <p>
     * 
     * @param viewPortUserScalable
     *            true to allow pinch-to-zoom
     */
    public void setViewPortUserScalable(Boolean viewPortUserScalable) {
        this.viewPortUserScalable = viewPortUserScalable;
    }

    /**
     * @see #setViewPortUserScalable(Boolean)
     * 
     * @return true if the view port is user scalable (pinch-to-zoom)
     */
    public Boolean isViewPortUserScalable() {
        return viewPortUserScalable;
    }

    /**
     * Sets the initial scale of the viewport.
     * 
     * @param viewPortInitialScale
     *            The initial scale of the viewport as a multiplier.
     */
    public void setViewPortInitialScale(Float viewPortInitialScale) {
        this.viewPortInitialScale = viewPortInitialScale;
    }

    /**
     * @return the initial scale of the viewport as a multiplier or null if none
     *         set.
     */
    public Float getViewPortInitialScale() {
        return viewPortInitialScale;
    }

    /**
     * Sets the maximum scale allowed for the user.
     * 
     * @param viewPortMaximumScale
     *            the maximum scale allowed when zooming
     */
    public void setViewPortMaximumScale(Float viewPortMaximumScale) {
        this.viewPortMaximumScale = viewPortMaximumScale;
    }

    /**
     * @return the maximum scale allowed for the user when zooming or null if
     *         none set.
     */
    public Float getViewPortMaximumScale() {
        return viewPortMaximumScale;
    }

    /**
     * Sets the viewport width into which the client browsers should render the
     * page. The value can be pixels or "device-width". The device width
     * constant is used as a default as we expect TouchKit Applications to be
     * designed for small devices. If the value is null, browsers try to figure
     * out a proper viewport width by themselves.
     * 
     * @param viewPortWidth
     *            the requested viewport width.
     */
    public void setViewPortWidth(String viewPortWidth) {
        this.viewPortWidth = viewPortWidth;
    }

    /**
     * @return the requested viewport width or null if none set.
     * @see #setViewPortWidth(String)
     */
    public String getViewPortWidth() {
        return viewPortWidth;
    }

    /**
     * Sets the minimum scale allowed by the user.
     * 
     * @param viewPortMinimumScale
     *            the minimum scale allowed when zooming.
     */
    public void setViewPortMinimumScale(Float viewPortMinimumScale) {
        this.viewPortMinimumScale = viewPortMinimumScale;
    }

    /**
     * @return the minimum scale allowed when zooming.
     * @see #setViewPortMinimumScale(Float)
     */
    public Float getViewPortMinimumScale() {
        return viewPortMinimumScale;
    }

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {

        Document document = response.getDocument();

        Element html = document.getElementsByTag("html").get(0);
        Element head = document.getElementsByTag("head").get(0);

        DocumentType doctype = (DocumentType) html.previousSibling();
        DocumentType html5doc = new DocumentType("html", "", "", "");
        doctype.replaceWith(html5doc);

        Element element = document.createElement("meta");
        element.attr("name", "viewport");
        StringBuilder content = new StringBuilder();
        boolean open = false;
        open = addViewPortRule(content, open, "width", getViewPortWidth());
        if (!isViewPortUserScalable()) {
            open = addViewPortRule(content, open, "user-scalable", "no");
        }
        open = addViewPortRule(content, open, "initial-scale",
                getViewPortInitialScale());
        open = addViewPortRule(content, open, "maximum-scale",
                getViewPortMaximumScale());
        open = addViewPortRule(content, open, "minimum-scale",
                getViewPortMaximumScale());
        element.attr("content", content.toString());
        head.appendChild(element);

        // meta tag to disable gray tap highlights in WP8 (note, for some reason
        // these do not appear in W8, not even RT)
        // <meta name="msapplication-tap-highlight" content="no" />
        element = document.createElement("meta");
        element.attr("name", "msapplication-tap-highlight");
        element.attr("content", "no");
        head.appendChild(element);

    }

    private boolean addViewPortRule(StringBuilder content, boolean open,
            String name, Object value) {
        if (value == null) {
            return open;
        }
        if (open) {
            content.append(",");
        }
        content.append(name);
        content.append("=");
        content.append(value);
        return true;
    }

}
