package com.vaadin.addon.touchkit.gwt.client.ui;

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
    private static final String SHORT_CLASSNAME = "v-touchkit-componentgroup";
    public static final String CAPTION_CLASSNAME = "v-caption";
    public static final String ROW_CLASSNAME = SHORT_CLASSNAME + "-row";
    public static final String ROW_WITH_CAPTION_STYLENAME = ROW_CLASSNAME
            + "-cap";
    public static final String ROW_WITHOUT_CAPTION_STYLENAME = ROW_CLASSNAME
            + "-nocap";
    public static final String ROW_WITH_FULLSIZE_WIDGET_STYLENAME = ROW_CLASSNAME
            + "-full";
    public static final String CELL_CLASSNAME = SHORT_CLASSNAME + "-cell";

    protected List<Widget> widgets = new ArrayList<Widget>();

    protected Map<Widget, ImageElement> iconElements = new HashMap<Widget, ImageElement>();
    protected Map<Widget, DivElement> captionElements = new HashMap<Widget, DivElement>();

    public VerticalComponentGroupWidget() {
        setElement(Document.get().createDivElement());
        setStyleName(CLASSNAME);
    }

    public void setCaption(final Widget widget, String caption,
            String componentWidth) {

        DivElement captionElement = captionElements.get(widget);
        DivElement widgetElement = getRowElement(widget);

        if (caption != null && !caption.isEmpty()) {

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

            widgetElement.removeClassName(ROW_WITHOUT_CAPTION_STYLENAME);
            widgetElement.addClassName(ROW_WITH_CAPTION_STYLENAME);
            captionElement.setInnerText(caption);
        } else {
            if ((caption == null || caption.isEmpty())
                    && captionElement != null) {

                captionElement.removeFromParent();
                captionElement = null;
                captionElements.remove(widget);

                if (widgetElement != null) {
                    widgetElement.removeClassName(ROW_WITH_CAPTION_STYLENAME);
                }
            }
            if (widgetElement != null) {
                widgetElement.addClassName(ROW_WITHOUT_CAPTION_STYLENAME);
            }
        }

    }

    private DivElement getRowElement(Widget widget) {
        com.google.gwt.dom.client.Element parentElement = widget.getElement()
                .getParentElement();
        return (DivElement) (parentElement == null ? null : parentElement
                .getParentElement().cast());
    }

    public void setIcon(final Widget widget, String iconUrl) {
        if (!widgets.contains(widget)) {
            return;
        }

        DivElement divElement = captionElements.get(widget);
        ImageElement iconElement = iconElements.get(widget);

        if (iconUrl != null) {
            if (iconElement == null) {
                iconElement = Document.get().createImageElement();
                iconElement.setClassName(IconWidget.CLASSNAME);
                divElement.insertFirst(iconElement);
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
     * 
     * @param widget
     * @param index
     * @param componentWidth
     * @param caption
     */
    public void addOrMove(final Widget widget, int index,
            String componentWidth, String caption) {
        if (widgets.contains(widget)) {
            if (widgets.indexOf(widget) == index) {
                return;
            } else {
                remove(widget);
                addWidget(widget, index, caption, componentWidth);
            }
        } else {
            addWidget(widget, index, caption, componentWidth);
        }
    }

    /**
     * Add widget to given position
     * 
     * @param widget
     * @param index
     * @param componentWidth
     * @param caption
     */
    public void addWidget(final Widget widget, int index, String caption,
            String componentWidth) {
        if (widgets.contains(widget)) {
            return;
        }

        DivElement row = Document.get().createDivElement();
        row.addClassName(ROW_CLASSNAME);

        if (index < 0 || index >= widgets.size()) {
            getElement().appendChild(row);
            widgets.add(widget);
        } else {
            getElement().insertBefore(row, getRowElement(widgets.get(index)));
            widgets.add(index, widget);
        }

        DivElement wrapper = Document.get().createDivElement();
        wrapper.addClassName(CELL_CLASSNAME);
        row.appendChild(wrapper);
        add(widget, (Element) Element.as(wrapper));
        setCaption(widget, caption, componentWidth);

        if (!captionElements.containsKey(widget)) {
            row.removeClassName(ROW_WITH_FULLSIZE_WIDGET_STYLENAME);
        } else if ("100.0%".equals(componentWidth)) {
            row.addClassName(ROW_WITH_FULLSIZE_WIDGET_STYLENAME);
        } else {
            row.removeClassName(ROW_WITH_FULLSIZE_WIDGET_STYLENAME);
        }

    }

    /**
     * Adds Widget to group
     * 
     * @param widget
     */
    public void add(final Widget widget) {
        addWidget(widget, -1, null, null);
    }

    public boolean remove(Widget widget) {
        if (!widgets.contains(widget)) {
            return false;
        }
        DivElement element = getRowElement(widget);

        boolean ret = super.remove(widget);

        if (ret) {
            element.removeFromParent();
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
