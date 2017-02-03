package org.vaadin.touchkit.itest.oldtests;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationView;
import org.vaadin.touchkit.ui.VerticalComponentGroup;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Link;

public class PersonView extends AbstractTouchKitIntegrationTest {
    private NavigationView view;

    public PersonView() {
        view = new NavigationView();
        addComponent(view);
        view.setCaption("Pekka Puupää");
        buildSimpleReadOnlyView();
        TestUtils.makeSmallTabletSize(view);
    }

    private void buildSimpleReadOnlyView() {
        VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup();
        Label label = new Label();
        label.setCaption("Name");
        label.setValue("Pekka Puupää");
        label.setSizeUndefined();
        verticalComponentGroup.addComponent(label);

        CssLayout linkWrap = new CssLayout();
        linkWrap.setCaption("Email");
        Link link = new Link();
        link.setCaption("pekka@pekka.fi");
        link.setResource(new ExternalResource("mailto:pekka@pekka.fi"));
        linkWrap.addComponent(link);
        verticalComponentGroup.addComponent(linkWrap);
        
        linkWrap = new CssLayout();
        linkWrap.setCaption("Phone");
        link = new Link();
        link.setCaption("010101010");
        link.setResource(new ExternalResource("tel:" +"010101010"));
        linkWrap.addComponent(link);
        verticalComponentGroup.addComponent(linkWrap);

        
        view.setContent(verticalComponentGroup);
    }


}
