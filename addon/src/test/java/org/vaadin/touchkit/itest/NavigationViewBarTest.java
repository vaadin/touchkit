package org.vaadin.touchkit.itest;

import java.util.Random;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.VerticalComponentGroup;
import org.vaadin.touchkit.ui.NavigationManager.NavigationEvent;
import org.vaadin.touchkit.ui.NavigationManager.NavigationListener;

import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextField;

public class NavigationViewBarTest extends AbstractTouchKitIntegrationTest {

    Random r = new Random(0);

    public NavigationViewBarTest() {
        setDescription("NavigationView and -Bar test");
        addComponent(makeNavigationManager());
    }

    NavigationManager makeNavigationManager() {
        final NavigationManager navman = new NavigationManager();

        final NavigationView one = createView("One", "one", true);
        final NavigationView two = createView("Two", "two", true);
        final NavigationView three = createView("Three", "three", true);
        final NavigationView four = createView("Four", "four", false);

        navman.setCurrentComponent(one);
        navman.setNextComponent(two);

        addNextButton(navman, one, two);
        addNextButton(navman, two, three);
        addNextButton(navman, three, four);

        navman.addNavigationListener(new NavigationListener() {
            @Override
            public void navigate(NavigationEvent event) {
                if (navman.getCurrentComponent() == one) {
                    navman.setNextComponent(two);
                } else if (navman.getCurrentComponent() == two) {
                    navman.setNextComponent(three);
                } else if (navman.getCurrentComponent() == three) {
                    navman.setNextComponent(four);
                }
            }
        });
        return navman;
    }

    private static void addNextButton(final NavigationManager navman,
            final NavigationView view, final NavigationView next) {
        ((Button) view.getRightComponent())
                .addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        navman.navigateTo(next);
                    }
                });
    }

    NavigationView createView(String caption, String debugId, boolean hasNext) {
        final CssLayout layout = new CssLayout();

        NativeSelect nativeSelect = new NativeSelect();
        nativeSelect.addItem("FOO");
        for (int i = 0; i < 200; i++) {
            nativeSelect.addItem("BAR " + i);
        }
        nativeSelect
                .addItem("CAaaaaaaaaaaaaaaaaa Rsdfs sfsdfsdfsdsd fdsf adsfdsf sdaf j sdlfkjs ld   adsfsd f");
        nativeSelect.setImmediate(true);

        nativeSelect.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Notification.show("Value:" + event.getProperty().getValue());

            }
        });

        
        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup("Textifedf");
        
        verticalComponentGroup.addComponent(new TextField("Böö"));
        
        layout.addComponent(verticalComponentGroup);
        
        layout.addComponent(nativeSelect);

        layout.setId(debugId);
        int max = (int) (r.nextInt(100));
        for (int i = 0; i < max; i++) {
            Label l = new Label("Foo " + i);
            layout.addComponent(l);
        }

        NavigationView navView = new NavigationView();
        if (hasNext) {
            navView.setRightComponent(new Button("Next"));
        }
        navView.setCaption(caption);
        navView.setContent(layout);
        navView.getLeftComponent().setCaption("Back");
        return navView;
    }
}
