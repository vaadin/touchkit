package com.vaadin.addon.touchkit.gwt.client.navigation;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.addon.touchkit.gwt.client.IconWidget;

public class VNavigationButton extends HTML {
    private static final String NAVBUTTON_CLASSNAME = "v-touchkit-navbutton";
    private boolean enabled = true;
    private Widget targetWidget;
    private String placeHolderCaption;
    private String caption;
    private ImageElement icon;
    private SpanElement descriptionElement;
    private boolean touchStarted = false;

    public VNavigationButton() {
        setStyleName(NAVBUTTON_CLASSNAME);
        sinkEvents(Event.ONCLICK | Event.TOUCHEVENTS);
    }
    
    protected void onClick() {
        if (enabled) {
            getElement().focus();
            navigate();
        }
    }
    
    @Override
    public void onBrowserEvent(Event event) {
		switch (event.getTypeInt()) {
		case Event.ONTOUCHSTART:
			touchStarted = true;
			break;
		case Event.ONTOUCHMOVE:
		case Event.ONTOUCHCANCEL:
			touchStarted = false;
			break;
		case Event.ONTOUCHEND:
			if (touchStarted) {
				event.preventDefault();
				event.stopPropagation();
				onClick();
			}
			touchStarted = false;
			break;
		case Event.ONCLICK:
			onClick();
			super.onBrowserEvent(event);
			break;
		default:
			super.onBrowserEvent(event);
		}
    }

    public Widget getTargetWidget() {
        return targetWidget;
    }

    private void navigate() {
        VNavigationManager panel = findNavigationPanel(this);
        if (panel != null) {
            if (getTargetWidget() != null) {
                if (getTargetWidget().getParent() == panel) {
                    panel.setCurrentWidget(getTargetWidget());
                } else {
                    panel.setNextWidget(getTargetWidget());
                    panel.navigateForward();
                }
            } else {
                panel.navigateToPlaceholder(getPlaceHolderCaption());
            }
        }
    }

    public static VNavigationManager findNavigationPanel(Widget w) {
        Widget parent2 = w.getParent();
        while (parent2 != null && !(parent2 instanceof VNavigationManager)) {
            parent2 = parent2.getParent();
        }
        return (VNavigationManager) parent2;
    }

    @Override
    public void setText(String text) {
        caption = text;
        super.setText(text);
    }

    public String getCaption() {
        return caption;
    }

    public void setTargetWidget(Widget widget) {
        targetWidget = widget;
    }

    public void setPlaceHolderCaption(String targetViewCaption) {
        placeHolderCaption = targetViewCaption;
    }

    public String getPlaceHolderCaption() {
        if (placeHolderCaption != null) {
            return placeHolderCaption;
        }
        return getCaption();
    }

    public void setIcon(String iconUrl) {
        if(icon == null) {
            icon = Document.get().createImageElement();
            icon.setClassName(IconWidget.CLASSNAME);
        }
        icon.setSrc(iconUrl);
        getElement().insertFirst(icon);
    }

    public void setDescription(String description) {
        if (description != null && !description.trim().isEmpty()) {
          
            if (descriptionElement == null) {
                descriptionElement = Document.get().createSpanElement();
                descriptionElement.setClassName(NAVBUTTON_CLASSNAME + "-desc");
                getElement().insertFirst(descriptionElement);
            }
            descriptionElement.setInnerHTML(description);
        
        } else if (descriptionElement != null) {
        
            descriptionElement.removeFromParent();
            descriptionElement = null;
        }
    }

}
