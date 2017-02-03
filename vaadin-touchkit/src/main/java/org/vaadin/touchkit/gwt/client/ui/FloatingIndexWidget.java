package org.vaadin.touchkit.gwt.client.ui;

import org.vaadin.touchkit.gwt.client.VTouchKitResources;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class FloatingIndexWidget extends SimplePanel {
    private FlowPanel p = new FlowPanel();
    private boolean capturing;

    private void startDrag(int y) {
        Event.setCapture(getElement());
        capturing = true;
        scrollTo(y);
    }

    public FloatingIndexWidget() {
        add(p);
        setStyleName(VTouchKitResources.INSTANCE.css().floatingIndex());
        addDomHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(MouseMoveEvent event) {
                if (capturing) {
                    scrollTo(event.getClientY());
                    event.stopPropagation();
                    event.preventDefault();
                }
            }
        }, MouseMoveEvent.getType());
        addDomHandler(new TouchMoveHandler() {
            @Override
            public void onTouchMove(TouchMoveEvent event) {
                if (capturing) {
                    scrollTo(event.getTouches().get(0).getClientY());
                    event.stopPropagation();
                    event.preventDefault();
                }
            }
        }, TouchMoveEvent.getType());
        addDomHandler(new TouchEndHandler() {
            @Override
            public void onTouchEnd(TouchEndEvent event) {
                Event.releaseCapture(getElement());
                event.stopPropagation();
                event.preventDefault();
                capturing = false;
            }
        }, TouchEndEvent.getType());
        addDomHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                Event.releaseCapture(getElement());
                event.stopPropagation();
                event.preventDefault();
                capturing = false;
            }
        }, MouseUpEvent.getType());

    }

    private void scrollTo(int y) {
        Widget target = null;
        for (Widget w : p) {
            int absoluteTop = w.getAbsoluteTop();
            if (absoluteTop < y) {
                target = w;
            } else {
                break;
            }
        }
        if (target != null) {
            ((Item) target).scrollToWidget();
        }
    }

    public void map(String key, final Widget w,
            final com.google.gwt.dom.client.Element element) {
        Item label = new Item(key, w, element);
        p.add(label);
    }

    private static native boolean isScrollable(
            com.google.gwt.dom.client.Element e) throws Exception
    /*-{
        var overflow = $wnd.getComputedStyle(e).getPropertyValue("overflow");
        if( overflow == 'auto' || overflow == 'scroll') {
            return true;
        } else {
            return false;
        }
    }-*/;

    class Item extends HTML {

        private Element element;
        private Widget w;

        public Item(String key, final Widget w, final Element element) {
            super("<div>" + key + "</div>");
            this.w = w;
            this.element = element;
            addTouchStartHandler(new TouchStartHandler() {
                @Override
                public void onTouchStart(TouchStartEvent event) {
                    startDrag(event.getTouches().get(0).getClientY());
                    event.stopPropagation();
                    event.preventDefault();
                }
            });
            addMouseDownHandler(new MouseDownHandler() {
                @Override
                public void onMouseDown(MouseDownEvent event) {
                    startDrag(event.getClientY());
                    event.stopPropagation();
                    event.preventDefault();
                }
            });
        }

        private void scrollToWidget() {
            try {
                com.google.gwt.dom.client.Element e;
                if (element == null) {
                    e = w.getElement().getParentElement();
                    while (e != null && !isScrollable(e)) {
                        e = e.getParentElement();
                    }
                } else {
                    e = element;
                }
                e.setScrollTop(e.getScrollHeight());
            } catch (Exception e) {
            }
            // Sanity check
            w.getElement().scrollIntoView();
        }
    }

}
