package com.vaadin.addon.touchkit.rootextensions;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;

import com.vaadin.terminal.AbstractExtension;
import com.vaadin.terminal.gwt.server.BootstrapFragmentResponse;
import com.vaadin.terminal.gwt.server.BootstrapListener;
import com.vaadin.terminal.gwt.server.BootstrapPageResponse;
import com.vaadin.terminal.gwt.server.BootstrapResponse;

public class ViewPortSettings extends AbstractExtension implements BootstrapListener {
    
    private static final String DEVICE_WIDTH = "device-width";
    private Boolean viewPortUserScalable = false;
    private Float viewPortInitialScale = 1f;
    private Float viewPortMinimumScale = 1f;
    private Float viewPortMaximumScale = 1f;
    private String viewPortWidth = DEVICE_WIDTH;

    /**
     * Sets whether the user should be allowed to zoom the content. The default
     * value for TouchKit apps is false, as we expect that applications are
     * designed for smaller/all devices.
     * <p>
     * 
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param viewPortUserScalable
     */
    public void setViewPortUserScalable(Boolean viewPortUserScalable) {
        this.viewPortUserScalable = viewPortUserScalable;
    }

    /**
     * @see #setViewPortUserScalable(Boolean)
     * 
     * @return true if the view port is user scalable
     */
    public Boolean isViewPortUserScalable() {
        return viewPortUserScalable;
    }

    /**
     * Sets the initial scale of the viewport.
     * 
     * <p>
     * 
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param viewPortInitialScale
     */
    public void setViewPortInitialScale(Float viewPortinitialScale) {
        viewPortInitialScale = viewPortinitialScale;
    }

    /**
     * 
     * @return the view port initial scale
     * @see #getViewPortInitialScale()
     */
    public Float getViewPortInitialScale() {
        return viewPortInitialScale;
    }

    /**
     * Sets the maximum scale allowed for the user.
     * <p>
     * 
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param viewPortmaximumScale
     */
    public void setViewPortMaximumScale(Float viewPortmaximumScale) {
        viewPortMaximumScale = viewPortmaximumScale;
    }

    /**
     * @return
     * @see #getViewPortMaximumScale()
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
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param viewPortWidth
     */
    public void setViewPortWidth(String viewPortWidth) {
        this.viewPortWidth = viewPortWidth;
    }

    /**
     * @return
     * @see #setViewPortWidth(String)
     */
    public String getViewPortWidth() {
        return viewPortWidth;
    }

    /**
     * Sets the minimum scale allowed by the user.
     * 
     * @param viewPortMinimumScale
     */
    public void setViewPortMinimumScale(Float viewPortMinimumScale) {
        this.viewPortMinimumScale = viewPortMinimumScale;
    }

    /**
     * @return
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
        // FIXME change this part to use variables in this object
        
        Document document = response.getDocument();
        
        Element html = document.getElementsByTag("html").get(0);
        Element head = document.getElementsByTag("head").get(0);
        
        DocumentType doctype = (DocumentType) html.previousSibling();
        DocumentType html5doc = new DocumentType("html", "", "", "");
        doctype.replaceWith(html5doc);
        
        Element element = document.createElement("meta");
        element.attr("name", "viewport");
        StringBuilder content = new StringBuilder();
        content.append("width=device-width");
        content.append(",");
        content.append("user-scalable=no");
        content.append(",");
        content.append("initial-scale=1");
        content.append(",");
        content.append("maximum-scale=1");
        content.append(",");
        content.append("minimum-scale=1");
        element.attr("content", content.toString());
        head.appendChild(element);
        
    }

    
    
}
