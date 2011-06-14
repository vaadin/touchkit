package com.vaadin.addons.touchkit;

import com.vaadin.addons.touchkit.ui.EmailField;
import com.vaadin.addons.touchkit.ui.NavigationButton;
import com.vaadin.addons.touchkit.ui.NavigationPanel;
import com.vaadin.addons.touchkit.ui.NavigationView;
import com.vaadin.addons.touchkit.ui.NumberField;
import com.vaadin.addons.touchkit.ui.OptionLayout;
import com.vaadin.addons.touchkit.ui.Switch;
import com.vaadin.addons.touchkit.ui.Tabsheet;
import com.vaadin.addons.touchkit.ui.Toolbar;
import com.vaadin.addons.touchkit.ui.TouchKitSubWindow;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class NavPanelTestWithViews extends NavigationPanel implements
        ComponentContainer {

    private SimpleNavView[] views;
    private Button fullScreen;

    public NavPanelTestWithViews() {

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
            public void buttonClick(ClickEvent event) {
                TouchKitSubWindow touchKitSubWindow = new TouchKitSubWindow();
                touchKitSubWindow.setWidth("360px");
                touchKitSubWindow.setHeight("80%");
                // VerticalLayout content = new VerticalLayout();
                // content.setSpacing(true);
                // content.setMargin(true);

                NavigationPanel content = new NavPanelTestWithViews();

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

                touchKitSubWindow.setContent(content);

                if (event.getButton() == fullScreen) {
                    touchKitSubWindow.setSizeFull();
                    touchKitSubWindow.setModal(false);
                    Tabsheet tabsheet = new Tabsheet();
                    touchKitSubWindow.setContent(tabsheet);

                    content.setCaption("TAB1");
                    tabsheet.addTab(content);
                    CssLayout cssLayout = new CssLayout();
                    cssLayout.setCaption("TAB2");
                    tabsheet.addTab(cssLayout);
                    cssLayout.addComponent(new Label(
                            "Nothing here actually, close button on tab1."));

                }

                touchKitSubWindow.showRelativeTo(event.getButton());

            }
        };
        final Button button = new Button(
                "Try me (TouchKit's modal window impl)", listener);
        fullScreen = new Button("Fullscreen modal", listener);

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

        testView.setContent(cssLayout);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        Button b = new Button("Yes");
        b.setStyleName("green");
        buttons.addComponent(b);
        b = new Button("No");
        b.setStyleName("red");
        buttons.addComponent(b);
        b = new Button("Ok");
        b.setStyleName("blue");
        buttons.addComponent(b);

        HorizontalLayout group = new HorizontalLayout();
        buttons.addComponent(group);
        group.setStyleName("buttongroup");
        group.addStyleName("red");
        testView.setNavigationBarComponent(buttons);
        Component metoo = new Button("TopRight", listener);
        // metoo.setWidth("40px");
        group.addComponent(new Button("No toolbar", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                TouchKitSubWindow touchKitSubWindow = new TouchKitSubWindow();
                touchKitSubWindow.setWidth("360px");
                ((VerticalLayout) touchKitSubWindow.getContent())
                        .setMargin(false);
                // touchKitSubWindow.setHeight("80%");
                Button b = new Button("Asd");
                b.setWidth("100%");
                touchKitSubWindow.addComponent(b);
                b = new Button("Foo");
                b.setWidth("100%");
                touchKitSubWindow.addComponent(b);
                b = new Button("Bar");
                b.setWidth("100%");
                touchKitSubWindow.addComponent(b);

                touchKitSubWindow.showRelativeTo(event.getButton());
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
            setDebugId("SNV" + counter++);

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

            setNavigationBarComponent(createActionButton5());
        }

        private Component createActionButton5() {
            Button button = new Button(null, this);
            button.setIcon(new ThemeResource("../runo/icons/64/cancel.png"));
            return button;
        }

        private Component createActionButton1() {
            Button button = new Button(null, this);
            button.setIcon(new ThemeResource("../runo/icons/64/email.png"));
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
            cssLayout.setWidth("100%");
            Component label = new Label("Foobar");
            label.setStyleName("grey-title");
            cssLayout.addComponent(label);

            OptionLayout optionLayout = new OptionLayout();
            Component textField = new TextField("Name");
            textField.setWidth("100%");
            optionLayout.addComponent(textField);
            // email field
            EmailField emailField = new EmailField("Email");
            emailField.setWidth("100%");
            optionLayout.addComponent(emailField);
            // number field
            NumberField numberField = new NumberField("Age");
            numberField.setWidth("100%");
            optionLayout.addComponent(numberField);

            addSliderWithIcons(optionLayout);

            optionLayout.addComponent(new CheckBox("Setting böö"));

            cssLayout.addComponent(optionLayout);

            label = new Label("Foobar");
            label.setStyleName("grey-title");
            cssLayout.addComponent(label);

            optionLayout = new OptionLayout();
            textField = new TextField("Name");
            textField.setWidth("100%");
            optionLayout.addComponent(textField);
            // email field
            emailField = new EmailField("Longer caption");
            emailField.setWidth("100%");
            optionLayout.addComponent(emailField);
            // number field
            numberField = new NumberField("Age");
            numberField.setWidth("100%");
            optionLayout.addComponent(numberField);

            addSliderWithIcons(optionLayout);

            optionLayout.addComponent(new CheckBox("Setting böö"));

            cssLayout.addComponent(optionLayout);

            label = new Label("FormLayout in OptionLayout");
            label.setStyleName("grey-title");
            cssLayout.addComponent(label);

            
            OptionLayout optionLayout2 = new OptionLayout();
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

            addSliderWithIcons(formLayout);

            formLayout.addComponent(new CheckBox("Setting böö"));

            optionLayout2.addComponent(formLayout);
            cssLayout.addComponent(optionLayout2);

            setContent(cssLayout);

        }

        private void addSliderWithIcons(Layout optionLayout) {
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
            OptionLayout components = new OptionLayout();
            int amount = getDepth() % 2 == 1 ? 3 : 25;
            for (int i = 0; i < amount; i++) {
                SimpleNavView simpleNavView = new SimpleNavView(this, i);
                NavigationButton navigationButton = new NavigationButton();
                navigationButton.setIcon(getNextIcon());
                navigationButton.setTargetView(simpleNavView);
                if ((i + 1) % 5 == 0) {
                    navigationButton.setDescription("Status quo");
                }
                components.addComponent(navigationButton);
            }
            Switch switch1 = new Switch();
            switch1.setCaption("ios wannabe check");
            components.addComponent(switch1);
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

        public void buttonClick(ClickEvent event) {
            getWindow().showNotification("Just a demo!");
        }

    }

}
