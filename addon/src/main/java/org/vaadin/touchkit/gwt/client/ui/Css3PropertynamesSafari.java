package org.vaadin.touchkit.gwt.client.ui;

public class Css3PropertynamesSafari extends Css3Propertynames {

    protected Css3PropertynamesSafari() {
    }

    protected String _transitionEnd() {
        return "webkitTransitionEnd";
    }

    @Override
    protected String _transition() {
        return "webkitTransition";
    }

    @Override
    protected String _transform() {
        return "webkitTransform";
    }

}
