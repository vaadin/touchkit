package com.vaadin.addon.touchkit.gwt;

import java.util.Collection;
import java.util.HashSet;

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
import com.vaadin.client.ui.upload.UploadConnector;
import com.vaadin.client.ui.window.WindowConnector;
import com.vaadin.server.widgetsetutils.WidgetMapGenerator;
import com.vaadin.shared.ui.Connect.LoadStyle;

/**
 * TouchKitWidgetMapGenerator makes some heavy classes that are rarely used in
 * mobile devices loaded lazily. This way saving bandwidth and making the
 * initial loading time smaller.
 */
public class TouchKitWidgetMapGenerator extends WidgetMapGenerator {

    HashSet<Class<? extends ServerConnector>> lazyComponents = new HashSet<Class<? extends ServerConnector>>();

    public TouchKitWidgetMapGenerator() {
        lazyComponents.add(UploadConnector.class);
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

    /**
     * @return the components made explicitly lazy by
     *         {@link TouchKitWidgetMapGenerator}. Modifiable collection for
     *         tuning in the project.
     */
    protected Collection<Class<? extends ServerConnector>> getLazyComponents() {
        return lazyComponents;
    }

    @Override
    protected LoadStyle getLoadStyle(
            Class<? extends ServerConnector> paintableType) {
        if (lazyComponents.contains(paintableType)) {
            return LoadStyle.LAZY;
        }
        LoadStyle loadStyle = super.getLoadStyle(paintableType);
        if (isUseCacheManifest()) {
            if (loadStyle == LoadStyle.DEFERRED) {
                // with cache manifest everything is
                // cached automatically anyways
                loadStyle = LoadStyle.LAZY;
            }
        }
        return loadStyle;
    }

    protected boolean isUseCacheManifest() {
        return true;
    }

}
