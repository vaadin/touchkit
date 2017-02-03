package org.vaadin.touchkit.itest.oldtests;

import org.vaadin.touchkit.ui.NavigationButton;
import org.vaadin.touchkit.ui.NavigationManager;
import org.vaadin.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import org.vaadin.touchkit.ui.NavigationButton.NavigationButtonClickListener;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.Label;

public class NavPanelTestWithNavButtons extends NavigationManager implements
        ComponentContainer {

    private SimpleNavView[] views;

    public NavPanelTestWithNavButtons() {

        views = new SimpleNavView[7];
        for (int i = 0; i < views.length; i++) {
            SimpleNavView v = new SimpleNavView();
            views[i] = v;
            if (i == 0) {
                v.getPrev().setEnabled(false);
                continue;
            }
            v.getPrev().setTargetView(views[i - 1]);
            views[i - 1].getNext().setTargetView(v);
            views[i - 1].getNext().setCaption(v.getCaption());

            if (i + 1 == views.length) {
                v.getNext().setEnabled(false);
            }
        }

        navigateTo(views[0]);

    }

    static class SimpleNavView extends CssLayout {
        static int counter = 0;
        private NavigationButton next = new NavigationButton("next");
        private NavigationButton prev = new NavigationButton("prev");

        public void setNext(NavigationButton next) {
            this.next = next;
        }

        public NavigationButton getNext() {
            return next;
        }

        public void setPrev(NavigationButton prev) {
            this.prev = prev;
        }

        public NavigationButton getPrev() {
            return prev;
        }

        public SimpleNavView() {
            setId("SNV" + counter);
            setCaption("Nav view " + counter);
            addComponent(new Label("Nav view " + counter++));
            addComponent(prev);
            addComponent(next);
            prev.addClickListener(new NavigationButtonClickListener() {
                public void buttonClick(NavigationButtonClickEvent event) {
                    /*
                     * Hack to make test somewhat working.
                     * addAttribute(Paintable) don't support painting un
                     * attached components properly. It works but breaks subtree
                     * caching in some cases. This is handled in
                     * NavigatioButton. Thats why we need to force repaint of
                     * prev button in next visible view. This will be
                     * automatically handled by NavigationManager for
                     * NavigationViews.
                     */
                    SimpleNavView targetView = (SimpleNavView) prev
                            .getTargetView();
                    targetView.prev.requestRepaint();
                    targetView.next.requestRepaint();
                }
            });
        }
    }

}
