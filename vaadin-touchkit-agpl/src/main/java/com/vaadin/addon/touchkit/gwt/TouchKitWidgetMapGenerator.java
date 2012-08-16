package com.vaadin.addon.touchkit.gwt;


/**
 * TouchKitWidgetMapGenerator makes some heavy classes that are rarely used in
 * mobile devices loaded lazily. This way saving bandwidth and making the
 * initial loading time smaller.
 */
public class TouchKitWidgetMapGenerator
//extends WidgetMapGenerator
{

//    HashSet<Class<? extends Paintable>> lazyComponents = new HashSet<Class<? extends Paintable>>();
//
//    public TouchKitWidgetMapGenerator() {
//        lazyComponents.add(Upload.class);
//        lazyComponents.add(VerticalLayout.class);
//        lazyComponents.add(HorizontalLayout.class);
//        lazyComponents.add(OrderedLayout.class);
//        lazyComponents.add(GridLayout.class);
//        lazyComponents.add(AbsoluteLayout.class);
//        lazyComponents.add(SplitPanel.class);
//        lazyComponents.add(HorizontalSplitPanel.class);
//        lazyComponents.add(VerticalSplitPanel.class);
//        lazyComponents.add(Accordion.class);
//        lazyComponents.add(ComboBox.class);
//        lazyComponents.add(TabSheet.class);
//        lazyComponents.add(MenuBar.class);
//        lazyComponents.add(Panel.class);
//        lazyComponents.add(Window.class);
//        lazyComponents.add(RichTextArea.class);
//        lazyComponents.add(TwinColSelect.class);
//        lazyComponents.add(CustomLayout.class);
//        lazyComponents.add(PopupView.class);
//    }
//
//    /**
//     * @return the components made explicitly lazy by
//     *         {@link TouchKitWidgetMapGenerator}. Modifiable collection for
//     *         tuning in the project.
//     */
//    protected Collection<Class<? extends Paintable>> getLazyComponents() {
//        return lazyComponents;
//    }
//
//    @Override
//    protected LoadStyle getLoadStyle(Class<? extends Paintable> paintableType) {
//        if (lazyComponents.contains(paintableType)) {
//            return LoadStyle.LAZY;
//        }
//        LoadStyle loadStyle = super.getLoadStyle(paintableType);
//        if (isUseCacheManifest()) {
//            if (loadStyle == LoadStyle.DEFERRED) {
//                // with cache manifest everything is
//                // cached automatically anyways
//                loadStyle = LoadStyle.LAZY;
//            }
//        }
//        return loadStyle;
//    }

    protected boolean isUseCacheManifest() {
        return true;
    }

}
