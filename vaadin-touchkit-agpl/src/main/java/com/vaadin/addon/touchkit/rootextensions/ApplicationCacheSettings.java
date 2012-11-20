package com.vaadin.addon.touchkit.rootextensions;

import org.jsoup.nodes.Document;

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
        }
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
