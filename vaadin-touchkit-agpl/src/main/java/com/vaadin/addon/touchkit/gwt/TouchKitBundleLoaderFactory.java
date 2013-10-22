package com.vaadin.addon.touchkit.gwt;

import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.ui.absolutelayout.AbsoluteLayoutConnector;
import com.vaadin.client.ui.accordion.AccordionConnector;
import com.vaadin.client.ui.calendar.CalendarConnector;
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
import com.vaadin.client.ui.table.TableConnector;
import com.vaadin.client.ui.tabsheet.TabsheetConnector;
import com.vaadin.client.ui.treetable.TreeTableConnector;
import com.vaadin.client.ui.twincolselect.TwinColSelectConnector;
import com.vaadin.client.ui.window.WindowConnector;
import com.vaadin.server.widgetsetutils.ConnectorBundleLoaderFactory;
import com.vaadin.shared.ui.Connect.LoadStyle;

/**
 * TouchKitWidgetMapGenerator enables lazy loading of some heavy widget classes
 * that are rarely used in mobile devices. This way saves bandwidth and improves
 * the loading time.
 * 
 * Components for which the widgets are lazily loaded:
 * <ul>
 * <li>{@link com.vaadin.ui.VerticalLayout}</li>
 * <li>{@link com.vaadin.ui.HorizontalLayout}</li>
 * <li>{@link com.vaadin.ui.GridLayout}</li>
 * <li>{@link com.vaadin.ui.AbsoluteLayout}</li>
 * <li>{@link com.vaadin.ui.HorizontalSplitPanel}</li>
 * <li>{@link com.vaadin.ui.VerticalSplitPanel}</li>
 * <li>{@link com.vaadin.ui.Accordion}</li>
 * <li>{@link com.vaadin.ui.ComboBox}</li>
 * <li>{@link com.vaadin.ui.TabSheet}</li>
 * <li>{@link com.vaadin.ui.MenuBar}</li>
 * <li>{@link com.vaadin.ui.Panel}</li>
 * <li>{@link com.vaadin.ui.Window}</li>
 * <li>{@link com.vaadin.ui.RichTextArea}</li>
 * <li>{@link com.vaadin.ui.TwinColSelect}</li>
 * <li>{@link com.vaadin.ui.CustomLayout}</li>
 * <li>{@link com.vaadin.ui.PopupView}</li>
 * </ul>
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
        lazyComponents.add(CalendarConnector.class);
        lazyComponents.add(TableConnector.class);
        lazyComponents.add(TreeTableConnector.class);
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
