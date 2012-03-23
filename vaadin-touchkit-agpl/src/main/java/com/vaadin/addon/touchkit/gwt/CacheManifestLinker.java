package com.vaadin.addon.touchkit.gwt;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.gwt.core.ext.LinkerContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.AbstractLinker;
import com.google.gwt.core.ext.linker.Artifact;
import com.google.gwt.core.ext.linker.ArtifactSet;
import com.google.gwt.core.ext.linker.ConfigurationProperty;
import com.google.gwt.core.ext.linker.LinkerOrder;
import com.google.gwt.core.ext.linker.SelectionProperty;
import com.google.gwt.core.ext.linker.SyntheticArtifact;
import com.vaadin.ui.themes.BaseTheme;

/**
 * TODO review and improve this impl
 */
@LinkerOrder(LinkerOrder.Order.POST)
public class CacheManifestLinker extends AbstractLinker {

    private HashSet<String> cachedArtifacts = new HashSet<String>();

    private HashSet<String> onlineArtifacts = new HashSet<String>();

    public CacheManifestLinker() {
        addCachedResource("/");
        addBaseTheme();
    }

    private void addBaseTheme() {
        CodeSource codeSource = BaseTheme.class.getProtectionDomain()
                .getCodeSource();
        if (codeSource != null) {
            URL jar = codeSource.getLocation();
            try {
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                ZipEntry nextEntry = zip.getNextEntry();
                while (nextEntry != null) {
                    String name = nextEntry.getName();
                    if (name.contains("VAADIN/themes/base")) {
                        if (name.endsWith("/")
                                || (name.endsWith(".css") && !name
                                        .endsWith("styles.css"))) {
                            // ingrore "partial css files"
                        } else {
                            addCachedResource("../../../" + name);
                        }
                    }
                    nextEntry = zip.getNextEntry();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getDescription() {
        return "Touchkit cache manifest generator";
    }

    @Override
    public ArtifactSet link(TreeLogger logger, LinkerContext context,
            ArtifactSet artifacts) throws UnableToCompleteException {
        ArtifactSet newArtifacts = new ArtifactSet(artifacts);

        SortedSet<ConfigurationProperty> configurationProperties = context
                .getConfigurationProperties();
        for (ConfigurationProperty configurationProperty : configurationProperties) {
            if (configurationProperty.getName().equals(
                    "touchkit.manifestlinker.additionalCacheRoot")) {
                List<String> values = configurationProperty.getValues();
                for (String root : values) {
                    addResourcesRecursively(root);
                }
                break;
            }
        }

        /*
         * TODO make manifest for all permutations as we widen browsers support.
         * Even IE 10 is supposed to support cache manifests. The servlet then
         * needs to serve the correct one based on useragent.
         */

        for (Artifact<?> artifact : newArtifacts) {
            for (SelectionProperty property : context.getProperties()) {
                if (property.getName().equals("user.agent")) {
                    String value = property.tryGetValue();
                    if (value != null && value.equals("safari")) {
                        addCachedResource(artifact);
                        break;
                    }
                }
            }
        }

        String moduleName = context.getModuleName();

        addCachedResource(moduleName + ".nocache.js");

        StringBuilder cm = new StringBuilder();
        cm.append("CACHE MANIFEST\n");
        cm.append("# Build time" + new Date());
        cm.append("\n\nCACHE:\n");

        for (String fn : cachedArtifacts) {
            cm.append(fn);
            cm.append("\n");
        }
        cm.append("\nNETWORK:\n");
        cm.append("*\n\n");

        SyntheticArtifact cacheManifest = emitString(logger, cm.toString(),
                getCacheManifestFileName());
        newArtifacts.add(cacheManifest);

        return newArtifacts;
    }

    private void addResourcesRecursively(String root) {

        String[] split = root.split(":");

        String sourcePath = split[0];
        String relativeRoot = split[1];

        File file = new File(sourcePath);
        if (file.isDirectory()) {
            String[] list = file.list();
            for (String child : list) {
                doAdd(file, child, relativeRoot);
            }
        }
    }

    private void doAdd(File f, String relativePath, final String relativeRoot) {
        File file2 = new File(f, relativePath);
        if (file2.isDirectory()) {
            String[] list = file2.list();
            for (String string : list) {
                doAdd(file2, relativePath + "/" + string, relativeRoot);
            }
        } else {
            addCachedResource(relativeRoot + "/" + relativePath);
        }

    }

    protected void addCachedResource(Artifact<?> artifact) {
        String string = artifact.toString();
        addCachedResource("" + string);
    }

    protected void addCachedResource(String filename) {
        if (acceptCachedResource(filename)) {
            cachedArtifacts.add(filename);
        }
    }

    List<String> acceptedFileEnds = Arrays.asList(".html", ".js", ".css",
            ".png", ".jpg", ".gif", ".ico");

    protected boolean acceptCachedResource(String filename) {
        if (filename.startsWith("compile-report/")) {
            return false;
        }
        for (String acceptedEnd : acceptedFileEnds) {
            if (filename.endsWith(acceptedEnd)) {
                return true;
            }
        }
        return false;
    }

    protected String getCacheManifestFileName() {
        return "cache.manifest";
    }

}
