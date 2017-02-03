package org.vaadin.touchkit.itest;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationBar;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.SwipeView;
import org.vaadin.touchkit.ui.VerticalComponentGroup;
import org.vaadin.touchkit.ui.NavigationManager.NavigationEvent.Direction;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Ignore
public class SwipeViewTest extends AbstractTouchKitIntegrationTest {

    public SwipeViewTest() {
        addComponent(new SwipeViewTestMgr());
    }

    @Override
    public void attach() {
        super.attach();
        VaadinSession session = getUI().getSession();
        session.addRequestHandler(new RequestHandler() {

            @Override
            public boolean handleRequest(VaadinSession session,
                    VaadinRequest request, VaadinResponse response)
                    throws IOException {
                String requestPathInfo = request.getPathInfo();
                if (requestPathInfo.contains("winterphotos/")) {
                    response.setCacheTime(60 * 60 * 1000);
                    response.setContentType("image/jpeg");

                    String ss = requestPathInfo.substring(requestPathInfo
                            .lastIndexOf("/") + 1);
                    InputStream resourceAsStream = getClass()
                            .getResourceAsStream("/winterphotos/" + ss);
                    IOUtils.copy(resourceAsStream, response.getOutputStream());
                    return true;
                }
                return false;
            }
        });
    }

    public static class SwipeViewTestMgr extends NavigationManager {

        int index = 0;

        boolean loop = true;

        SwipeView[] images;

        public SwipeViewTestMgr() {
            setMaintainBreadcrumb(false);
            setWidth("100%");
            images = loadImages();

            SwipeView prev = getImage(index - 1);
            SwipeView cur = getImage(index);
            SwipeView next = getImage(index + 1);
            setPreviousComponent(prev);
            setCurrentComponent(cur);
            setNextComponent(next);
            updateNextPreviousInCurrentCompoenent();

            addNavigationListener(new NavigationListener() {
                public void navigate(NavigationEvent event) {
                    if (event.getDirection() == Direction.FORWARD) {
                        index++;
                        int nextViewIndex = index + 1;
                        while (nextViewIndex >= images.length) {
                            nextViewIndex -= images.length;
                        }
                        if (loop || nextViewIndex != 0) {
                            SwipeView next = getImage(nextViewIndex);
                            setNextComponent(next);
                        }
                    } else {
                        index--;
                        int i = index - 1;
                        while (i < 0) {
                            i += images.length;
                        }
                        if (loop || i != images.length - 1) {
                            SwipeView prev = getImage(i);
                            setPreviousComponent(prev);
                        }

                    }
                    updateNextPreviousInCurrentCompoenent();
                }
            });

        }

        private void updateNextPreviousInCurrentCompoenent() {
            Component currentComponent2 = getCurrentComponent();

            if (currentComponent2 instanceof ImageView) {
                ImageView imageView = (ImageView) currentComponent2;
                NavigationButton leftComponent = (NavigationButton) imageView.navigationBar
                        .getLeftComponent();
                leftComponent.setTargetView(getPreviousComponent());
                NavigationButton rightComponent = (NavigationButton) imageView.navigationBar
                        .getRightComponent();
                rightComponent.setTargetView(getNextComponent());
            }
        }

        private SwipeView getImage(int i) {
            while (i < 0) {
                i += images.length;
            }
            return images[i % images.length];
        }

        private SwipeView[] loadImages() {
            String[] filenames = new String[] { "Peimari during winter.jpg",
                    "Peimari, another skier.jpg",
                    "Perfect sunshine on Peimari ice.jpg",
                    "Sanders_fished_from_Peimari_.jpg",
                    "Snow_trees_and_sunshine_ in_Trysil.jpg",
                    "Snowy view in Trysil.jpg", "Sunset in Trysil.jpg",
                    "Swamp in Trysil during the winter.jpg",
                    "Track and shadow in powder snow.jpg",
                    "Trysil, break before reaching the peak.jpg",
                    "View to south on Peimari ice.jpg" };

            SwipeView[] ss = new SwipeView[filenames.length + 1];

            for (int i = 0; i < filenames.length; i++) {
                final String f = filenames[i];
                ss[i] = new ImageView(f);
                ss[i].setId("l" + i);
            }
            ss[ss.length - 1] = new SwipeViewWithNormalContent();

            return ss;
        }

        class SwipeViewWithNormalContent extends SwipeView {
            @SuppressWarnings("deprecation")
            public SwipeViewWithNormalContent() {
                CssLayout cssLayout = new CssLayout();
                
                cssLayout.addComponent(new Button("Button"));
                Form form = new Form();
                FormLayout layout = (FormLayout) form.getLayout();
                layout.setMargin(false);
                form.setSizeUndefined();

                CheckBox cb = new CheckBox();

                cb.setCaption("Loop views");
                cb.addValueChangeListener(new Property.ValueChangeListener() {
                    public void valueChange(ValueChangeEvent event) {
                        loop = !loop;
                        if (loop) {
                            setNextComponent(images[0]);
                        } else {
                            setNextComponent(null);
                        }
                    }
                });
                cb.setImmediate(true);

                form.addField("loop", cb);

                TextField tf = new TextField("Foo");
                tf.setWidth("100%");
                tf.setValue("This is a test page that shows SwipeView can also contain other stuff but just images :-)");
                form.addField("foo", tf);
                form.addField("bar", new CheckBox("Bar"));
                NativeSelect field = new NativeSelect();
                field.setCaption("Car");
                field.addItem("Foo");
                field.addItem("Bar");
                field.addItem("Car");
                form.addField("car", field);

                VerticalComponentGroup fg = new VerticalComponentGroup();
                fg.setCaption("Form");
                fg.addComponent(form);
                cssLayout.addComponent(fg);

                VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup();
                verticalComponentGroup
                        .setCaption("Labels to make this view heavy");

                for (int i = 0; i < 40; i++) {
                    verticalComponentGroup
                            .addComponent(new Label("Label " + i));
                }

                cssLayout.addComponent(verticalComponentGroup);

                setContent(cssLayout);

            }
        }

        static class ImageView extends SwipeView {

            private String ss;
            private Embedded embedded = new Embedded();
            private NavigationBar navigationBar;
            private CssLayout layout = new CssLayout() {
                @Override
                protected String getCss(Component c) {
                    if (c == navigationBar) {
                        // Make background of bar semitranparent over the image.
                        return "background: rgba(255, 255, 255, 0.7); position:absolute;top:0;left:0;right:0;";
                    }
                    return super.getCss(c);
                }
            };

            public ImageView(String f) {
                setWidth("100%");
                setContent(layout);
                ss = f;
                navigationBar = new NavigationBar();
                NavigationButton button = new NavigationButton("<");
                button.setStyleName("back");
                navigationBar.setLeftComponent(button);
                navigationBar.setCaption(ss.replace(".jpg", "").replace("_",
                        " "));
                button = new NavigationButton(">");
                navigationBar.setRightComponent(button);
                layout.addComponent(navigationBar);
                button.setStyleName("forward");
                embedded.setWidth("100%");
                layout.addComponent(embedded);
            }

            @Override
            public void attach() {
                super.attach();

                UI ui = getUI();
                if (ui == null) {
                    throw new RuntimeException("WTF!!");
                }
                ExternalResource source = new ExternalResource(Page
                        .getCurrent().getLocation().getPath()
                        + "/winterphotos/" + ss);
                embedded.setSource(source);
            }

            @Override
            public void detach() {
                super.detach();
            }
        }

    }
}
