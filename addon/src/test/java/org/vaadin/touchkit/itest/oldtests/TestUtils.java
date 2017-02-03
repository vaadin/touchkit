package org.vaadin.touchkit.itest.oldtests;

import org.junit.Ignore;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.ComponentContainer;

@Ignore
public class TestUtils {
    public static void makeSmallTabletSize(ComponentContainer c) {
        c.setWidth(450, Sizeable.UNITS_PIXELS);
        c.setHeight(640, Sizeable.UNITS_PIXELS);
    }

    public static void injectCss(String cssString) {
        String script = "if ('\\v'=='v') /* ie only */ {\n"
                + "        document.createStyleSheet().cssText = '"
                + cssString
                + "';\n"
                + "    } else {var tag = document.createElement('style'); tag.type = 'text/css';"
                + " document.getElementsByTagName('head')[0].appendChild(tag);tag[ (typeof "
                + "document.body.style.WebkitAppearance=='string') /* webkit only */ ? 'innerText' "
                + ": 'innerHTML'] = '" + cssString + "';}";

        Page.getCurrent().getJavaScript().execute(script);
    }

}
