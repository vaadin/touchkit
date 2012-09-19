package com.vaadin.addon.touchkit.gwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class VerticalComponentGroupWidget extends ComplexPanel {

    public static final String TAGNAME = "verticalcomponentgroup";
    private static final String CLASSNAME = "v-touchkit-" + TAGNAME;
    public static final String CAPTION_CLASSNAME = "v-caption";
    public static final String ROW_WITH_CAPTION_STYLENAME =
    		"v-touchkit-componentgroup-rowcap";
    
    protected List<Widget> widgets = new ArrayList<Widget>();
    
    protected Map<Widget,DivElement> widgetElements =
    		new HashMap<Widget,DivElement>();
    protected Map<Widget,ImageElement> iconElements =
    		new HashMap<Widget,ImageElement>();
    protected Map<Widget,DivElement> captionElements =
    		new HashMap<Widget,DivElement>();
    protected Map<Widget,DivElement> wrapperElements =
    		new HashMap<Widget,DivElement>();

    public VerticalComponentGroupWidget() {
    	setElement(Document.get().createDivElement());
    	setStyleName(CLASSNAME);
    }
    
    public void setCaption(final Widget widget, String caption) {
    	if (!widgets.contains(widget)) {
    		return;
    	}
    	
    	DivElement captionElement = captionElements.get(widget);
    	
    	if (caption != null && !caption.isEmpty()) {
    		
        	DivElement widgetElement = widgetElements.get(widget);
    		
    		if (captionElement == null) {
        		captionElement = Document.get().createDivElement();
                captionElement.addClassName(CAPTION_CLASSNAME);
                captionElements.put(widget, captionElement);
                if (iconElements.containsKey(widget)) {
                    widgetElement.insertAfter(captionElement,
                    		iconElements.get(widget));	
                } else {
                	widgetElement.insertFirst(captionElement);
                }
    		}
    		
    		captionElement.setInnerText(caption);
    		widgetElement.addClassName(ROW_WITH_CAPTION_STYLENAME);
    		wrapperElements.get(widget).removeClassName(
    				"v-touchkit-componentgroup-cell-fullwrapper");
    		captionElement.setInnerText(caption);
    	} else if ((caption == null || caption.isEmpty()) && captionElement != null) {
    		captionElement.removeFromParent();
    		captionElements.remove(widget);
    		widgetElements.get(widget).removeClassName(
    				ROW_WITH_CAPTION_STYLENAME);
    		wrapperElements.get(widget).addClassName(
    				"v-touchkit-componentgroup-cell-fullwrapper");
    	}
    }
    
    public void setIcon(final Widget widget, String iconUrl) {
    	if (!widgets.contains(widget)) {
    		return;
    	}
    	
    	ImageElement iconElement = iconElements.get(widget);
    	
    	if (iconUrl == null || iconUrl.isEmpty()) {
    		if (iconElement == null) {
    			iconElement = Document.get().createImageElement();
    			iconElement.setClassName(IconWidget.CLASSNAME);
    			widgetElements.get(widget).insertFirst(iconElement);
    		}
    		iconElement.setSrc(iconUrl);
    		iconElement.setAlt("");
    		
    	} else if (iconElement != null) {
    		iconElement.removeFromParent();
    		iconElements.remove(widget);
    	}
    }
    
    /**
     * Add or move widget to given position
     * @param widget
     * @param index
     */
    public void addOrMove (final Widget widget, int index) {
    	if (widgets.contains(widget)) {
    		if (widgets.indexOf(widget) == index) {
    			return;
    		} else {
    			remove(widget);	    		
	    		addWidget (widget, index);
    		}
    	} else {
    		addWidget (widget, index);
    	}
    }
    
    /**
     * Add widget to given position
     * @param widget
     * @param index
     */
    public void addWidget (final Widget widget, int index) {
    	if (widgets.contains(widget)) {
    		return;
    	}
    	
    	DivElement div = Document.get().createDivElement();   	
    	div.addClassName("v-touchkit-componentgroup-row");   
    	
    	if (index < 0 || index >= widgets.size()) {
    		getElement().appendChild(div);
    		widgets.add(widget);
    	} else {
    		getElement().insertBefore(div,
    				widgetElements.get(widgets.get(index)));
        	widgets.add(index, widget);
    	}
    	widgetElements.put(widget, div);
    	
    	DivElement wrapper = Document.get().createDivElement();
    	wrapper.addClassName("v-touchkit-componentgroup-cell-wrapper");
    	wrapperElements.put(widget, wrapper);
    	div.appendChild(wrapper);
    	
    	add(widget, (Element)Element.as(wrapper));
    }

    /**
     * Adds Widget to group
     * 
     * @param widget
     */
    public void add(final Widget widget) {
    	addWidget(widget, -1);
    }
    
    public boolean remove(Widget widget) {   
    	if (!widgets.contains(widget)) {
    		return false;
    	}
    	
    	boolean ret = super.remove(widget);
    	
    	if (ret) {
    		setIcon (widget, null);
    		setCaption (widget, null);
    		
    		DivElement wrapper = wrapperElements.get(widget);
    		wrapper.removeFromParent();
	    	wrapperElements.remove(widget);
	    	
	    	DivElement element = widgetElements.get(widget);
	    	element.removeFromParent();
	    	widgetElements.remove(widget);
	    	
	    	widgets.remove(widget);
    	}
    	
    	return ret;
    }
    
    @Override
    public void clear() {
    	for (Widget child : widgets) {
    		remove(child);
    	}
    }
    
}
