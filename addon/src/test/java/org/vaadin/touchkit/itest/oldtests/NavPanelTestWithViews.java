package org.vaadin.touchkit.itest.oldtests;

import java.util.Date;
import java.util.logging.Logger;

import org.vaadin.touchkit.ui.EmailField;
import org.vaadin.touchkit.ui.HorizontalButtonGroup;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.NumberField;
import org.vaadin.touchkit.ui.Popover;
import org.vaadin.touchkit.ui.Switch;
import org.vaadin.touchkit.ui.TabBarView;
import org.vaadin.touchkit.ui.Toolbar;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.server.ClassResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.Slider;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;

public class NavPanelTestWithViews extends NavigationManager implements
        ComponentContainer {

    private SimpleNavView[] views;
    private Button fullScreen;
    static UI app;
    private Button notRelative;

    public NavPanelTestWithViews() {

        addNavigationListener(new NavigationListener() {
            @Override
            public void navigate(NavigationEvent event) {
                Logger.getAnonymousLogger().info(
                        "Navigated " + event.getDirection());
            }
        });

        views = new SimpleNavView[7];
        for (int i = 0; i < views.length; i++) {
            SimpleNavView v = new SimpleNavView(null, i);
            views[i] = v;
        }

        navigateTo(views[0]);

        NavigationView currentComponent2 = (NavigationView) getCurrentComponent();
        ComponentContainer content = (ComponentContainer) currentComponent2
                .getContent();

        NavigationView testView = new NavigationView(
                "TestView modal sub windows");
        testView.setPreviousComponent(currentComponent2);

        Button.ClickListener listener = new Button.ClickListener() {
            private int i;

            @Override
            public void buttonClick(ClickEvent event) {
                Popover popover = new Popover();
                popover.setWidth("360px");
                popover.setHeight("80%");
                // VerticalLayout content = new VerticalLayout();
                // content.setSpacing(true);
                // content.setMargin(true);

                NavigationManager content = new NavPanelTestWithViews();

                /*
                 * HorizontalLayout topStuff = new HorizontalLayout();
                 * topStuff.setWidth("100%"); content.addComponent(topStuff);
                 * topStuff.setSpacing(true); topStuff.setMargin(false);
                 * 
                 * topStuff.addComponent(new Button("Foo"));
                 * 
                 * Label header = new Label("Some section");
                 * topStuff.addComponent(header);
                 * topStuff.setExpandRatio(header, 1.0f);
                 * 
                 * topStuff.addComponent(new Button("Close", new
                 * Button.ClickListener() {
                 * 
                 * public void buttonClick(ClickEvent event) { event.getButton()
                 * .getWindow() .getParent() .removeWindow(
                 * event.getButton().getWindow());
                 * 
                 * } }));
                 */

                if (i++ % 2 == 1) {
                    // Test that popover with navigationview should look fine
                    // without toolbar too
                    NavigationView currentComponent3 = (NavigationView) content
                            .getCurrentComponent();
                    currentComponent3.setToolbar(null);
                }

                popover.setContent(content);

                if (event.getButton() == fullScreen) {
                    popover.setSizeFull();
                    popover.setModal(false);
                    TabBarView touchKitTabsheet = new TabBarView();
                    popover.setContent(touchKitTabsheet);

                    content.setCaption("TAB1");
                    Tab addTab = touchKitTabsheet.addTab(content);
                    addTab.setCaption("TAB1??");
                    CssLayout cssLayout = new CssLayout();
                    cssLayout.setCaption("TAB2");
                    touchKitTabsheet.addTab(cssLayout);
                    cssLayout.addComponent(new Label(
                            "Nothing here actually, close button on tab1."));

                }

                if (event.getButton() == notRelative) {

                    popover.setWidth("50%");
                    popover.setHeight("100%");
                    popover.center();
                    popover.requestRepaint();
                    getUI().addWindow(popover);
                } else {
                    popover.showRelativeTo(event.getButton());
                }

            }
        };
        final Button button = new Button(
                "Try me (TouchKit's modal window impl)", listener);
        fullScreen = new Button("Fullscreen modal", listener);
        notRelative = new Button("Non-relative modal", listener);

        CssLayout cssLayout = new CssLayout() {
            @Override
            protected String getCss(Component c) {
                if (c == button) {
                    /*
                     * To test centering.
                     */
                    return "margin-left: 40%;";
                }
                return super.getCss(c);
            }
        };

        cssLayout.addComponent(button);
        cssLayout.addComponent(fullScreen);
        cssLayout.addComponent(notRelative);

        testView.setContent(cssLayout);

        Button b = new Button("Yes");
        b.setStyleName("green");
        cssLayout.addComponent(b);
        b = new Button("No");
        b.setStyleName("red");
        cssLayout.addComponent(b);
        b = new Button("OK");
        b.setStyleName("blue");
        cssLayout.addComponent(b);

        Button button2 = new Button("Link style");
        button2.setStyleName(BaseTheme.BUTTON_LINK);
        cssLayout.addComponent(button2);

        HorizontalButtonGroup group = new HorizontalButtonGroup();
        testView.setRightComponent(group);
        Component metoo = new Button("T", listener);
        metoo.addStyleName("blue");
        group.addComponent(new Button("No toolbar", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Popover popover = new Popover();
                popover.setWidth("360px");
                ((VerticalLayout) popover.getContent()).setMargin(false);
                // touchKitSubWindow.setHeight("80%");
                Button b = new Button("Save Draft");
                b.addStyleName("white");
                b.setWidth("100%");
                popover.addComponent(b);
                b = new Button("Delete Draft");
                b.addStyleName("red");
                b.setWidth("100%");
                popover.addComponent(b);
                b = new Button("Cancel");
                b.setWidth("100%");
                popover.addComponent(b);

                popover.showRelativeTo(event.getButton());
            }
        }));
        group.addComponent(metoo);

        NavigationButton navigationButton = new NavigationButton(testView);
        content.addComponent(navigationButton);

        Toolbar toolbar = new Toolbar();

        toolbar.addComponent(new Button("below", listener));

        testView.setToolbar(toolbar);

    }

    static class SimpleNavView extends NavigationView implements ClickListener {
        static int counter = 0;

        public SimpleNavView(SimpleNavView parent, int index) {
            setId("SNV" + counter++);

            String caption2;
            if (parent == null) {
                caption2 = "View " + index;
            } else {
                setPreviousComponent(parent);
                caption2 = parent.getCaption() + "." + index;
            }
            setCaption(caption2);

            if (getDepth() < 3) {
                generateSubViews();
            } else {
                generateLeafContent();
            }

            Toolbar toolbar2 = new Toolbar();
            setToolbar(toolbar2);

            toolbar2.addComponent(createActionButton1());
            toolbar2.addComponent(createActionButton2());
            toolbar2.addComponent(createActionButton3());
            toolbar2.addComponent(createActionButton4());
            toolbar2.addComponent(createActionButton1());

            setRightComponent(createActionButton5());
        }

        private Component createActionButton5() {
            Button button = new Button("Delete", this);
            button.addStyleName("red");
            return button;
        }

        private Component createActionButton1() {
            Button button = new Button(null, this);
            button.setIcon(new ThemeResource("../runo/icons/64/email.png"));

            button.setIcon(new ClassResource("mail.png"));
            return button;
        }

        private Component createActionButton2() {
            Button button = new Button(null, this);
            button.setIcon(new ThemeResource("../runo/icons/64/email-reply.png"));
            return button;
        }

        private Component createActionButton3() {
            Button button = new Button(null, this);
            button.setIcon(new ThemeResource("../runo/icons/64/email-send.png"));
            return button;
        }

        private Component createActionButton4() {
            Button button = new Button(null, this);
            button.setIcon(new ThemeResource("../runo/icons/64/folder.png"));
            button.setCaption("Send");
            return button;
        }

        private void generateLeafContent() {
            CssLayout cssLayout = new CssLayout();
//            cssLayout.setWidth("100%");
            // cssLayout.setMargin(true);
            Component label = new Label("Foobar");
            label.setStyleName("grey-title");
            cssLayout.addComponent(label);

            VerticalComponentGroup componentGroup = new VerticalComponentGroup();
            Component textField = new TextField("Name");
            textField.setWidth("100%");
            componentGroup.addComponent(textField);
            // email field
            EmailField emailField = new EmailField("Email");
            emailField.setIcon(new ThemeResource("../runo/icons/64/email.png"));
            emailField.setWidth("100%");
            componentGroup.addComponent(emailField);
            // number field
            NumberField numberField = new NumberField("Age");
            numberField.setWidth("100%");
            componentGroup.addComponent(numberField);

            DateField df = new DateField("Datefield");
            df.setWidth("100%");
            df.setValue(new Date());
            componentGroup.addComponent(df);

            ComboBox comboBox = new ComboBox("Combobox");
            comboBox.setWidth("100%");
            comboBox.addItem("Pekka");
            comboBox.addItem("Pirjo");
            comboBox.addItem("Irma");
            comboBox.addItem("Marko");
            componentGroup.addComponent(comboBox);

            addSliderWithIcons(componentGroup);

            componentGroup.addComponent(new CheckBox("Setting böö"));

            NativeSelect nativeSelect = new NativeSelect("Some setting");
            nativeSelect.addItem("Foo");
            nativeSelect.addItem("Bar");
            nativeSelect.addItem("Three");

            componentGroup.addComponent(nativeSelect);

            cssLayout.addComponent(componentGroup);

            label = new Label("Foobar");
            label.setStyleName("grey-title");
            cssLayout.addComponent(label);

            componentGroup = new VerticalComponentGroup();
            textField = new TextField("Name");
            textField.setWidth("100%");
            componentGroup.addComponent(textField);

            // email field
            emailField = new EmailField(
                    "Longer caption for non 100% width field which might be clipped with ellipsis");
            // emailField.setWidth("100%");
            componentGroup.addComponent(emailField);
            // number field
            numberField = new NumberField("Age");
            numberField.setWidth("100%");
            componentGroup.addComponent(numberField);

            addSliderWithIcons(componentGroup);

            componentGroup.addComponent(new CheckBox("Setting böö"));

            cssLayout.addComponent(componentGroup);

            label = new Label("FormLayout in ComponentGroup");
            label.setStyleName("grey-title");
            cssLayout.addComponent(label);

            VerticalComponentGroup optionLayout2 = new VerticalComponentGroup();
            FormLayout formLayout = new FormLayout();
            formLayout.setSpacing(false);
            formLayout.setMargin(false);
            textField = new TextField("Name");
            textField.setWidth("100%");
            formLayout.addComponent(textField);
            // email field
            emailField = new EmailField("Email");
            emailField.setWidth("100%");
            formLayout.addComponent(emailField);
            // number field
            numberField = new NumberField("Age");
            numberField.setWidth("100%");
            formLayout.addComponent(numberField);

            df = new DateField("Datefield");
            df.setWidth("100%");
            df.setValue(new Date());
            formLayout.addComponent(df);

            comboBox = new ComboBox("Combobox");
            comboBox.addItem("Pekka");
            comboBox.addItem("Pirjo");
            comboBox.addItem("Irma");
            comboBox.addItem("Marko");
            comboBox.setWidth("100%");
            formLayout.addComponent(comboBox);

            addSliderWithIcons(formLayout);

            formLayout.addComponent(new CheckBox("Setting böö"));

            optionLayout2.addComponent(formLayout);
            cssLayout.addComponent(optionLayout2);

            setContent(cssLayout);

        }

        private void addSliderWithIcons(ComponentContainer optionLayout) {
            final Component emb = new Embedded(null, getNextIcon());
            emb.setWidth("32px");
            final Embedded emb2 = new Embedded(null, getNextIcon());
            emb2.setWidth("32px");
            final Slider slider = new Slider(0, 100);
            slider.setWidth(100, UNITS_PERCENTAGE);

            HorizontalLayout hl = new HorizontalLayout();
            hl.addComponent(emb);
            hl.addComponent(slider);
            hl.addComponent(emb2);
            hl.setWidth(100, UNITS_PERCENTAGE);
            hl.setExpandRatio(slider, 1);
            hl.setComponentAlignment(slider, Alignment.MIDDLE_CENTER);
            optionLayout.addComponent(hl);
        }

        private int getDepth() {
            int depth = 1;
            SimpleNavView parent = (SimpleNavView) getPreviousComponent();
            if (parent != null) {
                depth += parent.getDepth();
            }
            return depth;
        }

        private void generateSubViews() {
            VerticalComponentGroup components = new VerticalComponentGroup();
            // components.setMargin(true);
            int amount = getDepth() % 2 == 1 ? 25 : 3;
            for (int i = 0; i < amount; i++) {
                SimpleNavView simpleNavView = new SimpleNavView(this, i);
                NavigationButton navigationButton = new NavigationButton();
                navigationButton.setIcon(getNextIcon());
                navigationButton.setTargetView(simpleNavView);
                if ((i + 1) % 5 == 0) {
                    navigationButton.setDescription("Status quo");
                }
                if ((i + 1) % 7 == 0) {
                    navigationButton.setDescription("Important");
                    navigationButton.addStyleName("emphasis");
                }
                if ((i + 1) % 9 == 0) {
                    navigationButton.setDescription("123");
                    navigationButton.addStyleName("pill");
                }
                components.addComponent(navigationButton);
            }
            Switch switch1 = new Switch();
            switch1.setCaption("ios wannabe check");
            switch1.setIcon(getNextIcon());
            switch1.setImmediate(true);
            components.addComponent(switch1);

            switch1 = new Switch();
            switch1.setCaption("ios wannabe checkbox with very long caption that will easily be clipped");
            switch1.setIcon(getNextIcon());
            components.addComponent(switch1);
            
            Switch switch2 = new Switch();
            switch2.setCaption("ios wannabe checkbox with so long caption that nobody is sure where it starts and where it ends. Caption's length is infinite, just like the universe.");
            switch2.setIcon(getNextIcon());
            components.addComponent(switch2);

            NavigationButton navigationButton = new NavigationButton(
                    "NavigationButton with rather long caption in it, which shouldn't cause the caption to pop on next line.");

            navigationButton.setIcon(getNextIcon());
            navigationButton.setDescription("Foobar");
            navigationButton.setTargetView(new SimpleNavView(this, i));

            components.addComponent(navigationButton);

            Button b = new Button("Normal button");
            // FIXME normal buttons in verticalcomponentgroup
            // components.addComponent(b);

            setContent(components);
        }

        static Resource[] icons = new Resource[] {

        new ThemeResource("../runo/icons/64/cancel.png"),
                new ThemeResource("../runo/icons/64/document-web.png"),
                new ThemeResource("../runo/icons/64/document-delete.png"),
                new ThemeResource("../runo/icons/64/document-image.png"),
                new ThemeResource("../runo/icons/64/document-ppt.png"),
                new ThemeResource("../runo/icons/64/document-txt.png"),
                new ThemeResource("../runo/icons/64/lock.png"),
                new ThemeResource("../runo/icons/64/ok.png"),
                new ThemeResource("../runo/icons/64/reload.png"),
                new ThemeResource("../runo/icons/64/trash.png"),
                new ThemeResource("../runo/icons/64/user.png"), };

        static int i = 0;

        private Resource getNextIcon() {
            return icons[i++ % icons.length];
        }

        @Override
        public void buttonClick(ClickEvent event) {
            Notification.show("Just a demo!");
        }

    }

}
