package com.vaadin.addon.itest;

import com.vaadin.addon.touchkit.itest.oldtests.PersonView;

public class PersonViewITCase extends AbstractRenderingTest {
    
    @Override
    protected String getTestViewName() {
        return PersonView.class.getName();
    }

}
