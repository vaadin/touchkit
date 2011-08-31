package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.ui.TouchKitApplication;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.ui.Label;

public class TouchKitTestApp extends TouchKitApplication {

    @Override
    public void onBrowserDetailsReady() {
        getMainWindow().addComponent(new Label("HelloWorld"));

    }

    /**
     * Returns test views (instances of TouchKitWindow) by their name.
     * 
     * @see com.vaadin.addon.touchkit.ui.TouchKitApplication#getWindow(java.lang.
     *      String)
     */
    @Override
    public TouchKitWindow getWindow(String name) {
        TouchKitWindow window = super.getWindow(name);
        if (window == null && name != null && !"".equals(name)
                && !name.contains(".ico")) {

            try {

                String className = getClass().getPackage().getName() + "."
                        + name;
                Class<?> forName = Class.forName(className);
                if (forName != null) {
                    TouchKitWindow newInstance = (TouchKitWindow) forName
                            .newInstance();
                    window = newInstance;
                    addWindow(window);
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return window;
    }

}
