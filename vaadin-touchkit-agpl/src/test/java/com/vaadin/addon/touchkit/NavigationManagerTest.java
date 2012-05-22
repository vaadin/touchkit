package com.vaadin.addon.touchkit;

import org.junit.Test;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;

public class NavigationManagerTest {

    @Test
    public void testSettingNextAndPreviousWithNull() {

        NavigationView nv1 = new NavigationView("Foo1");
        NavigationView nv2 = new NavigationView("Foo2");
        NavigationView nv3 = new NavigationView("Foo3");

        NavigationManager navigationManager = new NavigationManager();
        navigationManager.setNextComponent(nv3);
        navigationManager.setCurrentComponent(nv2);
        navigationManager.setPreviousComponent(nv1);

        navigationManager.setNextComponent(null);
        navigationManager.setPreviousComponent(null);

    }

}
