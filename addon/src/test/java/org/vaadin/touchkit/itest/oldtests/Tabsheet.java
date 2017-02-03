package org.vaadin.touchkit.itest.oldtests;

import java.util.Date;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NumberField;
import org.vaadin.touchkit.ui.Switch;
import org.vaadin.touchkit.ui.TabBarView;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.InlineDateField;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.Slider;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.v7.ui.Table;

public class Tabsheet extends AbstractTouchKitIntegrationTest {

    public Tabsheet() {
        TabBarView tabBarView = new TabBarView();

        tabBarView.addTab(getTable(), "First", new ThemeResource(
                "../runo/icons/64/folder.png"));
        tabBarView.addTab(getDateSelector(), "Other", new ThemeResource(
                "../runo/icons/64/document.png"));
        tabBarView.addTab(getComboBox(), "Third", new ThemeResource(
                "../runo/icons/64/document-pdf.png"));
        Tab tab = tabBarView.addTab(getFields(), "4th", new ThemeResource(
                "../runo/icons/64/email.png"));
        tabBarView.setSelectedTab(tab);

        makeSmallTabletSize(tabBarView);
        addComponent(tabBarView);

    }

    private Component getFields() {
        CssLayout cssLayout = new CssLayout();
        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup();
        verticalComponentGroup.setCaption("Some fields");

        Slider slider = new Slider("Slide this");
        slider.setIcon(new ThemeResource("../runo/icons/64/email.png"));
        slider.setWidth("100%");
        verticalComponentGroup.addComponent(slider);
        Component s = new Switch("Switch this");
        s.setIcon(new ThemeResource("../runo/icons/64/folder.png"));
        verticalComponentGroup.addComponent(s);
        verticalComponentGroup.addComponent(new NumberField("Numbers only"));
        verticalComponentGroup.addComponent(new DateField("Date please"));
        
        NativeSelect nativeSelect = new NativeSelect();
        nativeSelect.setCaption("Native select");
        for(int i = 0; i < 10; i++) {
            nativeSelect.addItem(i + "One");
            nativeSelect.addItem(i + "Two");
            nativeSelect.addItem(i+ "Three");
        }
        nativeSelect.setImmediate(true);
        verticalComponentGroup.addComponent(nativeSelect);
        
        verticalComponentGroup.addComponent(new ComboBox("Combo", nativeSelect));
        
        
        
        
        
        cssLayout.addComponent(verticalComponentGroup);
        return cssLayout;
    }

    private Table getTable() {
        Table table = new Table();
        table.addContainerProperty("Col1", String.class, "some random data");
        table.addContainerProperty("Number", String.class, "123");
        table.addContainerProperty("Col3", String.class, "other data");
        for (int i = 0; i < 100; i++) {
            table.addItem();
        }
        return table;
    }

    private Component getDateSelector() {
        InlineDateField inlineDateField = new InlineDateField();
        inlineDateField.setValue(new Date(1314886401678l - 1000 * 60 * 60 * 24
                * 3));
        return inlineDateField;
    }

    private Component getComboBox() {
        ComboBox comboBox = new ComboBox();
        for (int i = 0; i < 100; i++) {
            comboBox.addItem("Item " + i);
        }

        comboBox.setValue("Item " + 2);
        return comboBox;
    }
}
