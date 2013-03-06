package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;

public class NativeSelectInForm extends AbstractTouchKitIntegrationTest {

    public static class Bean {
        String aString;

        public String getaString() {
            return aString;
        }

        public void setaString(String aString) {
            this.aString = aString;
        }
    }

    public NativeSelectInForm() {
        setDescription("NativeSelect in forms should work fine on Android");

        BeanItem<Bean> bi = new BeanItem<Bean>(new Bean());
        Form form = new Form();
        form.setCaption("Old deprecated form");
        form.setFormFieldFactory(new FormFieldFactory() {
            @Override
            public Field<?> createField(Item item, Object propertyId,
                    Component uiContext) {
                return createNativeSelect();
            }
        });
        form.setItemDataSource(bi);
        form.setBuffered(false);
        addComponent(form);

        NativeSelect select = createNativeSelect();
        FormLayout layout = new FormLayout();
        layout.setCaption("New field group in a form layout");
        layout.addComponent(select);
        FieldGroup fg = new FieldGroup(bi);
        fg.bind(select, "aString");
        fg.setBuffered(false);
        addComponent(layout);
    }

    private NativeSelect createNativeSelect() {
        NativeSelect s = new NativeSelect();
        s.addItem("Foo");
        s.addItem("Bar");
        s.addItem("Baz");
        s.setImmediate(true);
        s.setBuffered(false);
        return s;
    }
}
