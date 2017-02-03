/*
 * Copyright 2000-2013 Vaadin Ltd.
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

package org.vaadin.touchkit.gwt.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.TouchScrollDelegate;
import com.vaadin.v7.client.ui.VNativeSelect;
import com.vaadin.client.ui.VOverlay;

public class Android2NativeSelectReplacement extends VNativeSelect {
    
    public static interface Styles extends CssResource {
        
        String android23SelectOverlayGlass();

        String android23SelectOverlay();
        
        String android23Select();
        
        String android23SelectSelectedItem();

    }
    
    public static interface Bundle extends ClientBundle {
        @Source({"theme/android2select.css"})
        public Styles css();
    }
    
    static Bundle B = GWT.create(Bundle.class);

    private VerticalPanel options = new VerticalPanel();

    private Option value;

    private class Option extends Label implements ClickHandler {

        private String id;

        public Option(String id, String caption) {
            this.setId(id);
            setText(caption);
            addClickHandler(this);
            B.css().ensureInjected();
        }

        @Override
        public void onClick(ClickEvent event) {
            if (isEnabled() && !isReadonly()) {
                if(value != null) {
                    value.setSelected(false);
                }
                setSelected(true);
                l.setText(getText());
                onChange();
                overlay.hide();
            }
        }

        public void setSelected(boolean selected) {
            value = this;
            setStyleName(B.css().android23SelectSelectedItem(), selected);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    private boolean firstValueIsTemporaryNullItem = false;

    private VOverlay overlay;

    private Label l;
    
    public Android2NativeSelectReplacement() {
        setStyleName(B.css().android23Select());
        container.clear();
        l = new Label(" ");
        container.add(l);
        l.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                overlay = new VOverlay(true, true);
                overlay.setStyleName(B.css().android23SelectOverlay());
                overlay.setGlassEnabled(true);
                overlay.setGlassStyleName(B.css().android23SelectOverlayGlass());
                overlay.add(options);
                TouchScrollDelegate.enableTouchScrolling(overlay, overlay.getElement());
                overlay.center();
            }
        });
    }

    @Override
    public void buildOptions(UIDL uidl) {
        options.clear();

        firstValueIsTemporaryNullItem = false;

        if (isNullSelectionAllowed() && !isNullSelectionItemAvailable()) {
            // can't unselect last item in singleselect mode
            options.add(new Option("", " - "));
        }
        boolean selected = false;
        for (final Iterator<?> i = uidl.getChildIterator(); i.hasNext();) {
            final UIDL optionUidl = (UIDL) i.next();
            Option w = new Option(optionUidl.getStringAttribute("key"),
                    optionUidl.getStringAttribute("caption"));
            options.add(w);

            if (optionUidl.hasAttribute("selected")) {
                w.setSelected(true);
                selected = true;
                l.setText(w.getText());
            }
        }
        if (!selected && !isNullSelectionAllowed()) {
            // null-select not allowed, but value not selected yet; add null and
            // remove when something is selected
            Option w = new Option("", "");
            w.setSelected(true);
            options.insert(w, 0);
            
            firstValueIsTemporaryNullItem = true;
        }
    }

    @Override
    protected String[] getSelectedItems() {
        final ArrayList<String> selectedItemKeys = new ArrayList<String>();
        if(value != null) {
           selectedItemKeys.add(value.getId());
        }
        return selectedItemKeys.toArray(new String[selectedItemKeys.size()]);
    }

    public void onChange() {
        client.updateVariable(paintableId, "selected", new String[] { ""
                + getSelectedItem() }, isImmediate());
        if (firstValueIsTemporaryNullItem) {
            // remove temporary empty item
            options.remove(0);
            firstValueIsTemporaryNullItem = false;
        }
    }

    @Override
    public void setTabIndex(int tabIndex) {
    }

    @Override
    protected void updateEnabledState() {
        // NOP
    }

    @Override
    public void focus() {
        
    }
}
