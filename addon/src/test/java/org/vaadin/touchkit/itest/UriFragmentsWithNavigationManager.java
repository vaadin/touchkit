package org.vaadin.touchkit.itest;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.VerticalComponentGroup;
import org.vaadin.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import org.vaadin.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import org.vaadin.touchkit.ui.NavigationManager.NavigationEvent;
import org.vaadin.touchkit.ui.NavigationManager.NavigationListener;

import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;

public class UriFragmentsWithNavigationManager extends
        AbstractTouchKitIntegrationTest {

    public String getDescription() {
        return "Example and test how to use low level "
                + "uri fragements witn NavigationManager. Note, "
                + "that this doesn't work in latest Safari on Maverics "
                + "has 'fixed' back button with fragmens.";
    };

    Random r = new Random(0);

    public UriFragmentsWithNavigationManager() {
        setDescription("NavigationView and -Bar test");
        addComponent(makeNavigationManager());
    }

    NavigationManager makeNavigationManager() {
        final NavigationManager navman = new NavigationManager();

        ViewWithFragment root = new ViewWithFragment("ROOT", 0);
        navman.navigateTo(root);
        Page.getCurrent().setUriFragment(root.getFragment());

        Page.getCurrent().addUriFragmentChangedListener(
                new UriFragmentChangedListener() {

                    @Override
                    public void uriFragmentChanged(UriFragmentChangedEvent event) {
                        String fragment = event.getUriFragment();
                        ViewWithFragment component = (ViewWithFragment) navman
                                .getPreviousComponent();
                        if (component != null
                                && component.getFragment().equals(fragment)) {
                            navman.navigateBack();
                        }
                    }
                });

        navman.addNavigationListener(new NavigationListener() {

            @Override
            public void navigate(NavigationEvent event) {
                String uriFragment = Page.getCurrent().getUriFragment();
                ViewWithFragment component = (ViewWithFragment) navman
                        .getCurrentComponent();
                if (!component.getFragment().equals(uriFragment)) {
                    // widget back button clicked
                    Page.getCurrent().setUriFragment(component.getFragment(),
                            false);
                }

            }
        });

        return navman;
    }

    class ViewWithFragment extends NavigationView {
        private final String fragment = RandomStringUtils.randomAlphanumeric(4);
        private int level;

        public ViewWithFragment(String t, int level) {
            this.level = level;
            setCaption(t + "(" + level + ")");

            VerticalComponentGroup group = new VerticalComponentGroup();
            for (int i = 0; i < 3; i++) {
                final String title = "View " + i;
                NavigationButton button = new NavigationButton(title);
                button.addClickListener(new NavigationButtonClickListener() {

                    @Override
                    public void buttonClick(NavigationButtonClickEvent event) {
                        ViewWithFragment viewWithFragment = new ViewWithFragment(
                                title, getLevel() + 1);
                        getNavigationManager().navigateTo(viewWithFragment);
                        Page.getCurrent().setUriFragment(
                                viewWithFragment.getFragment(), false);
                    }
                });
                group.addComponent(button);
            }
            setContent(group);
        }

        public int getLevel() {
            return level;
        }

        public String getFragment() {
            return fragment;
        }

    }

}
