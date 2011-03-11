package com.vaadin.addons.touchkit;

import com.vaadin.addons.touchkit.ui.EmailField;
import com.vaadin.addons.touchkit.ui.NavigationButton;
import com.vaadin.addons.touchkit.ui.NumberField;
import com.vaadin.addons.touchkit.ui.OptionLayout;
import com.vaadin.addons.touchkit.ui.NavigationPanel;
import com.vaadin.addons.touchkit.ui.NavigationView;
import com.vaadin.addons.touchkit.ui.Toolbar;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;

public class NavPanelTestWithViews extends NavigationPanel implements
		ComponentContainer {

	private SimpleNavView[] views;

	public NavPanelTestWithViews() {

		views = new SimpleNavView[7];
		for (int i = 0; i < views.length; i++) {
			SimpleNavView v = new SimpleNavView(null, i);
			views[i] = v;
		}

		navigateTo(views[0]);

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
			Button button = new Button(null,this);
			button.setIcon(new ThemeResource("../runo/icons/64/cancel.png"));
			return button;
		}

		private Component createActionButton1() {
			Button button = new Button(null,this);
			button.setIcon(new ThemeResource("../runo/icons/64/email.png"));
			return button;
		}

		private Component createActionButton2() {
			Button button = new Button(null,this);
			button.setIcon(new ThemeResource("../runo/icons/64/email-reply.png"));
			return button;
		}

		private Component createActionButton3() {
			Button button = new Button(null,this);
			button.setIcon(new ThemeResource("../runo/icons/64/email-send.png"));
			return button;
		}

		private Component createActionButton4() {
			Button button = new Button(null,this);
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
			optionLayout.addComponent(new TextField("Name"));
			// email field
			optionLayout.addComponent(new EmailField("Email"));
			// number field
			optionLayout.addComponent(new NumberField("Age"));

			addSliderWithIcons(optionLayout);

			optionLayout.addComponent(new CheckBox("Setting böö"));

			cssLayout.addComponent(optionLayout);

			label = new Label("Foobar");
			label.setStyleName("grey-title");
			cssLayout.addComponent(label);

			optionLayout = new OptionLayout();
			optionLayout.addComponent(new TextField("Name"));
			// email field
			optionLayout.addComponent(new EmailField("Email"));
			// number field
			optionLayout.addComponent(new NumberField("Age"));

			addSliderWithIcons(optionLayout);

			
			optionLayout.addComponent(new CheckBox("Setting böö"));

			cssLayout.addComponent(optionLayout);

			label = new Label("Foobar");
			label.setStyleName("grey-title");
			cssLayout.addComponent(label);

			optionLayout = new OptionLayout();
			optionLayout.addComponent(new TextField("Name"));
			// email field
			optionLayout.addComponent(new EmailField("Email"));
			// number field
			optionLayout.addComponent(new NumberField("Age"));

			addSliderWithIcons(optionLayout);
			
			optionLayout.addComponent(new CheckBox("Setting böö"));

			cssLayout.addComponent(optionLayout);

			setContent(cssLayout);

		}

		private void addSliderWithIcons(OptionLayout optionLayout) {
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
			for (int i = 0; i < 20; i++) {
				SimpleNavView simpleNavView = new SimpleNavView(this, i);
				NavigationButton navigationButton = new NavigationButton();
				navigationButton.setIcon(getNextIcon());
				navigationButton.setTargetView(simpleNavView);
				if ((i + 1) % 5 == 0) {
					navigationButton.setDescription("Status quo");
				}
				components.addComponent(navigationButton);
			}
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
			getWindow().showNotification("Just a demo!");
		}

	}

}
