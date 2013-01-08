package com.vaadin.addon.touchkit.settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;

/**
 * This class is used to control HTML5 application cache settings.
 */
public class ApplicationCacheSettings implements BootstrapListener {

    private boolean cacheManifestEnabled = true;

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Document document = response.getDocument();
        if (isCacheManifestEnabled()) {

            // Add the widgetsetUrl parameter to the bootstrap parameters.
            // This is overridden to avoid adding the naive random query
            // parameter (used by core to avoid caching of js file).

            // FIXME there must be a safer way to fetch these parameters, now
            // another Bootstrap listener might break stuff here.
            // Candidate:
            // response.getBootstrapHandler().getWidgetsetForUI(context);
            // How to get context??

            Element scriptTag = document.getElementsByTag("script").last();
            String script = scriptTag.html();
            String vaadinDir = getAppConfigParameter("vaadinDir", script);
            String widgetset = getAppConfigParameter("widgetset", script);
            String widgetsetUrl = String.format(
                    "%swidgetsets/%s/%s.nocache.js", vaadinDir, widgetset,
                    widgetset);

            scriptTag.html("");
            scriptTag.appendChild(new DataNode(script.replace("\n});", String
                    .format(",\n    \"widgetsetUrl\": \"%s\"\n});",
                            widgetsetUrl)), scriptTag.baseUri()));

            // Add cache manifest attribute to html tag
            document.getElementsByTag("html").attr("manifest",
                    vaadinDir + "widgetsets/" + widgetset + "/cache.manifest");

        }
    }

    /**
     * Parses parameters from a JSON block of vaadin initialization parameters.
     * 
     * @param parameter
     *            the parameter to get the value for
     * @param script
     *            the entire contents of the script tag holding the vaadin app
     *            initialization parameters.
     * @return the value of the parameter
     */
    private String getAppConfigParameter(String parameter, String script) {
        Matcher m = Pattern.compile(
                String.format(".*\"%s\": \"(.*)\"", parameter)).matcher(script);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public boolean isCacheManifestEnabled() {
        return cacheManifestEnabled;
    }

    public void setCacheManifestEnabled(boolean cacheManifestEnabled) {
        this.cacheManifestEnabled = cacheManifestEnabled;
    }

}
