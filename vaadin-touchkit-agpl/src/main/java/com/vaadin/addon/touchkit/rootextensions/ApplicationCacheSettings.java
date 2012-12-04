package com.vaadin.addon.touchkit.rootextensions;

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
public class ApplicationCacheSettings extends AbstractTouchKitRootExtension
        implements BootstrapListener {

    private boolean cacheManifestEnabled = true;

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Document document = response.getDocument();
        if (isCacheManifestEnabled()) {
            document.getElementsByTag("html").attr("manifest",
                    getCacheManifestLocation(response));

            // Add the widgetsetUrl parameter to the bootstrap parameters.
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

    private String getCacheManifestLocation(BootstrapPageResponse response) {
        String staticFileLocation = response.getRequest().getService()
                .getStaticFileLocation(response.getRequest());
        String configuredWidgetset = response.getRequest().getService()
                .getConfiguredWidgetset(response.getRequest());
        return staticFileLocation + "/VAADIN/widgetsets/" + configuredWidgetset
                + "/cache.manifest";
    }

    public boolean isCacheManifestEnabled() {
        return cacheManifestEnabled;
    }

    public void setCacheManifestEnabled(boolean cacheManifestEnabled) {
        this.cacheManifestEnabled = cacheManifestEnabled;
    }

}
