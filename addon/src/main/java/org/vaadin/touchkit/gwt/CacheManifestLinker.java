package org.vaadin.touchkit.gwt;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.gwt.core.ext.LinkerContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.AbstractLinker;
import com.google.gwt.core.ext.linker.Artifact;
import com.google.gwt.core.ext.linker.ArtifactSet;
import com.google.gwt.core.ext.linker.CompilationResult;
import com.google.gwt.core.ext.linker.ConfigurationProperty;
import com.google.gwt.core.ext.linker.EmittedArtifact;
import com.google.gwt.core.ext.linker.LinkerOrder;
import com.google.gwt.core.ext.linker.SelectionProperty;
import com.google.gwt.core.ext.linker.Shardable;

/**
 * A GWT linker that produces a cache.manifest file describing what to cache in
 * the application cache. Very useful for specifying which resources need to be
 * available when in offline mode.
 */
@LinkerOrder(LinkerOrder.Order.POST)
@Shardable
public class CacheManifestLinker extends AbstractLinker {

    private final HashSet<String> cachedArtifacts = new HashSet<String>();

    private static Set<String> allArtifacts = Collections
            .synchronizedSet(new HashSet<String>());

    private static Map<String, Set<String>> generatedManifestResources = Collections
            .synchronizedMap(new HashMap<String, Set<String>>());

    public CacheManifestLinker() {
        addCachedResource("/");
        addCachedResource("../../../VAADIN/vaadinBootstrap.js");
        // Add the empty fake file we need add to keep Vaadin happy
        addCachedResource("../../../VAADIN/themes/touchkit/styles.css");
    }

    @Override
    public String getDescription() {
        return "Touchkit cache manifest generator";
    }

    @Override
    @SuppressWarnings("rawtypes")
    public ArtifactSet link(TreeLogger logger, LinkerContext context,
            ArtifactSet artifacts, boolean onePermutation)
            throws UnableToCompleteException {

        loadTouchKitWidgetSetResources(context);

        ArtifactSet newArtifacts = new ArtifactSet(artifacts);

        if (onePermutation) {
            Set<String> userAgents = new HashSet<String>();

            for (CompilationResult result : artifacts
                    .find(CompilationResult.class)) {
                SortedSet<SortedMap<SelectionProperty, String>> propertiesMap = result
                        .getPropertyMap();
                for (SortedMap<SelectionProperty, String> sm : propertiesMap) {
                    Set<SelectionProperty> keySet = sm.keySet();
                    for (SelectionProperty selectionProperty : keySet) {
                        if ("user.agent".equals(selectionProperty.getName())) {
                            userAgents.add(sm.get(selectionProperty));
                        }
                    }
                }
            }

            // This happens when we compile the widgetset for one browser only.
            if (userAgents.size() == 0) {
                userAgents.add("safari");
            }

            SortedSet<String> hashSet = new TreeSet<String>();

            for (Artifact artifact : artifacts) {
                if (artifact instanceof EmittedArtifact) {
                    EmittedArtifact ea = (EmittedArtifact) artifact;
                    String pathName = ea.getPartialPath();
                    if (acceptCachedResource(pathName)) {
                        hashSet.add(pathName);
                        allArtifacts.add(pathName);
                    }
                }
            }

            // Create manifest file names based on UA values
            Set<String> manifestNames = new HashSet<String>();
            for (String ua : userAgents) {
                manifestNames.add(ua);
                if ("safari".equals(ua)) {
                    // custom manifest for pre-KitKat Android
                    manifestNames.add("aosp");
                }
            }

            for (String name : manifestNames) {
                Set<String> manifestResources = generatedManifestResources
                        .get(name);
                if (manifestResources == null) {
                    manifestResources = new TreeSet<String>(hashSet);
                    generatedManifestResources.put(name, manifestResources);
                } else {
                    manifestResources.addAll(hashSet);
                }
                manifestResources.addAll(getManifestSpecificResources(name));
            }

        } else {

            for (Artifact artifact : artifacts) {
                if (artifact instanceof EmittedArtifact) {
                    EmittedArtifact ea = (EmittedArtifact) artifact;
                    String pathName = ea.getPartialPath();
                    if (acceptCachedResource(pathName)) {
                        if (!allArtifacts.contains(pathName)) {
                            // common stuff like kickstart script, included
                            // scripts, styles, images etc..
                            cachedArtifacts.add(pathName);
                        }
                    }
                }
            }

            for (Entry<String, Set<String>> e : generatedManifestResources
                    .entrySet()) {
                e.getValue().addAll(cachedArtifacts);
                newArtifacts.add(createCacheManifest(context, logger,
                        e.getValue(), e.getKey()));
            }
        }

        return newArtifacts;
    }

    private Set<String> getManifestSpecificResources(String name) {
        SortedSet<String> resources = new TreeSet<String>();
        String fontAwesome = "../../../VAADIN/themes/base/fonts/fontawesome-webfont";
        String fontExtension = "aosp".equals(name) ? ".ttf" : ".woff";
        resources.add(fontAwesome + fontExtension);
        return resources;
    }

    /**
     * Traverses directories specified in gwt modules to be added to cache
     * manifests. E.g. themes.
     * 
     * @param context
     */
    private void loadTouchKitWidgetSetResources(LinkerContext context) {
        synchronized (cachedArtifacts) {
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
        }
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
                doAdd(file2, string, relativeRoot + "/" + file2.getName());
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

    List<String> acceptedFileExtensions = Arrays.asList(".html", ".js", ".css",
            ".png", ".jpg", ".gif", ".ico", ".woff");

    protected boolean acceptCachedResource(String filename) {
        if (filename.startsWith("compile-report/")) {
            return false;
        }
        for (String acceptedExtension : acceptedFileExtensions) {
            if (filename.endsWith(acceptedExtension)) {
                return true;
            }
        }
        return false;
    }

    protected String getCacheManifestFileName() {
        return "cache.manifest";
    }

    private Artifact<?> createCacheManifest(LinkerContext context,
            TreeLogger logger, Set<String> artifacts, String userAgent)
            throws UnableToCompleteException {

        StringBuilder cm = new StringBuilder();
        cm.append("CACHE MANIFEST\n");
        cm.append("# Build time" + new Date());
        cm.append("\n\nCACHE:\n");

        for (String fn : artifacts) {
            cm.append(fn);
            cm.append("\n");
        }
        cm.append("\nNETWORK:\n");
        cm.append("*\n\n");

        String manifest = cm.toString();
        String manifestName = userAgent + ".manifest";

        return emitString(logger, manifest, manifestName);
    }

}
