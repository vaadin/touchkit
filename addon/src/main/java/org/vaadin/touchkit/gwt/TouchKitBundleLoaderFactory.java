package org.vaadin.touchkit.gwt;

import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.ui.absolutelayout.AbsoluteLayoutConnector;
import com.vaadin.client.ui.accordion.AccordionConnector;
import com.vaadin.v7.client.ui.calendar.CalendarConnector;
import com.vaadin.v7.client.ui.combobox.ComboBoxConnector;
import com.vaadin.client.ui.customlayout.CustomLayoutConnector;
import com.vaadin.client.ui.gridlayout.GridLayoutConnector;
import com.vaadin.client.ui.menubar.MenuBarConnector;
import com.vaadin.client.ui.orderedlayout.HorizontalLayoutConnector;
import com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector;
import com.vaadin.client.ui.panel.PanelConnector;
import com.vaadin.client.ui.popupview.PopupViewConnector;
import com.vaadin.v7.client.ui.richtextarea.RichTextAreaConnector;
import com.vaadin.client.ui.splitpanel.HorizontalSplitPanelConnector;
import com.vaadin.client.ui.splitpanel.VerticalSplitPanelConnector;
import com.vaadin.v7.client.ui.table.TableConnector;
import com.vaadin.client.ui.tabsheet.TabsheetConnector;
import com.vaadin.v7.client.ui.treetable.TreeTableConnector;
import com.vaadin.v7.client.ui.twincolselect.TwinColSelectConnector;
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
 * <li>{@link com.vaadin.ui.Calendar}</li>
 * <li>{@link com.vaadin.ui.Table}</li>
 * <li>{@link com.vaadin.ui.TreeTable}</li>
 * </ul>
 */
public class TouchKitBundleLoaderFactory extends ConnectorBundleLoaderFactory {

    Collection<String> lazyComponents = new HashSet<String>();

    public TouchKitBundleLoaderFactory() {
        add(VerticalLayoutConnector.class);
        add(HorizontalLayoutConnector.class);
        add(GridLayoutConnector.class);
        add(AbsoluteLayoutConnector.class);
        add(HorizontalSplitPanelConnector.class);
        add(VerticalSplitPanelConnector.class);
        add(AccordionConnector.class);
        add(ComboBoxConnector.class);
        add(TabsheetConnector.class);
        add(MenuBarConnector.class);
        add(PanelConnector.class);
        add(WindowConnector.class);
        add(RichTextAreaConnector.class);
        add(TwinColSelectConnector.class);
        add(CustomLayoutConnector.class);
        add(PopupViewConnector.class);
        add(CalendarConnector.class);
        add(TableConnector.class);
        add(TreeTableConnector.class);
    }

    private void add(Class<? extends ServerConnector> c) {
        lazyComponents.add(c.getCanonicalName());
    }

    @Override
    protected LoadStyle getLoadStyle(JClassType connectorType) {
        if (lazyComponents.contains(connectorType.getQualifiedSourceName())) {
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
