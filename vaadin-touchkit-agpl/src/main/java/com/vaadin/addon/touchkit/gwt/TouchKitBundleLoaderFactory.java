package com.vaadin.addon.touchkit.gwt;

import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.ui.absolutelayout.AbsoluteLayoutConnector;
import com.vaadin.client.ui.accordion.AccordionConnector;
import com.vaadin.client.ui.combobox.ComboBoxConnector;
import com.vaadin.client.ui.customlayout.CustomLayoutConnector;
import com.vaadin.client.ui.gridlayout.GridLayoutConnector;
import com.vaadin.client.ui.menubar.MenuBarConnector;
import com.vaadin.client.ui.orderedlayout.HorizontalLayoutConnector;
import com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector;
import com.vaadin.client.ui.panel.PanelConnector;
import com.vaadin.client.ui.popupview.PopupViewConnector;
import com.vaadin.client.ui.richtextarea.RichTextAreaConnector;
import com.vaadin.client.ui.splitpanel.HorizontalSplitPanelConnector;
import com.vaadin.client.ui.splitpanel.VerticalSplitPanelConnector;
import com.vaadin.client.ui.tabsheet.TabsheetConnector;
import com.vaadin.client.ui.twincolselect.TwinColSelectConnector;
import com.vaadin.client.ui.window.WindowConnector;
import com.vaadin.server.widgetsetutils.ConnectorBundleLoaderFactory;
import com.vaadin.shared.ui.Connect.LoadStyle;

/**
 * TouchKitWidgetMapGenerator makes some heavy classes that are rarely used in
 * mobile devices loaded lazily. This way saving bandwidth and making the
 * initial loading time smaller.
 */
public class TouchKitBundleLoaderFactory extends ConnectorBundleLoaderFactory {

    Collection<Class<? extends ServerConnector>> lazyComponents = new HashSet<Class<? extends ServerConnector>>();

    public TouchKitBundleLoaderFactory() {
        lazyComponents.add(VerticalLayoutConnector.class);
        lazyComponents.add(HorizontalLayoutConnector.class);
        lazyComponents.add(GridLayoutConnector.class);
        lazyComponents.add(AbsoluteLayoutConnector.class);
        lazyComponents.add(HorizontalSplitPanelConnector.class);
        lazyComponents.add(VerticalSplitPanelConnector.class);
        lazyComponents.add(AccordionConnector.class);
        lazyComponents.add(ComboBoxConnector.class);
        lazyComponents.add(TabsheetConnector.class);
        lazyComponents.add(MenuBarConnector.class);
        lazyComponents.add(PanelConnector.class);
        lazyComponents.add(WindowConnector.class);
        lazyComponents.add(RichTextAreaConnector.class);
        lazyComponents.add(TwinColSelectConnector.class);
        lazyComponents.add(CustomLayoutConnector.class);
        lazyComponents.add(PopupViewConnector.class);
    }

    @Override
    protected LoadStyle getLoadStyle(JClassType connectorType) {
        if (lazyComponents.contains(connectorType)) {
            return LoadStyle.LAZY;
        }
        LoadStyle loadStyle = super.getLoadStyle(connectorType);
        if (loadStyle == LoadStyle.DEFERRED) {
            // with cache manifest everything is
            // cached automatically anyways
            loadStyle = LoadStyle.LAZY;
        }
        return loadStyle;
    }
}
