package com.vaadin.addon.touchkit.gwt;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

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
 * TODO review and improve this impl
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
        addBaseTheme();
    }

    private void addBaseTheme() {
        // Find resources using org.reflections
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                .filterInputsBy(
                        new FilterBuilder()
                        .include("VAADIN\\.themes\\.base.*"))
                        .setUrls(
                                ClasspathHelper
                                .forPackage("VAADIN.themes.base"))
                                .setScanners(new ResourcesScanner()));

        Set<String> themeFiles = reflections.getResources(Pattern
                .compile("^.*\\.(css|gif|png|ico)$"));

        for (String name : themeFiles) {
            addCachedResource("../../../" + name);
        }
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
            String userAgent = "";
            for (CompilationResult result : artifacts
                    .find(CompilationResult.class)) {
                SortedSet<SortedMap<SelectionProperty, String>> propertiesMap = result
                        .getPropertyMap();
                for (SortedMap<SelectionProperty, String> sm : propertiesMap) {
                    Set<SelectionProperty> keySet = sm.keySet();
                    for (SelectionProperty selectionProperty : keySet) {
                        if ("user.agent".equals(selectionProperty.getName())) {
                            userAgent = sm.get(selectionProperty);
                            break;
                        }
                    }
                    if (userAgent != null) {
                        break;
                    }
                }
                if (userAgent != null) {
                    break;
                }
            }

            if (userAgent == null || userAgent.equals("")) {
                // If only one permutation, expect safari
                // TODO fetch real value
                userAgent = "safari";
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

            generatedManifestResources.put(userAgent, hashSet);
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

            Set<String> keySet = generatedManifestResources.keySet();
            for (String ua : keySet) {
                Set<String> set = generatedManifestResources.get(ua);
                set.addAll(cachedArtifacts);
                newArtifacts.add(createCacheManifest(context, logger, set, ua));
            }

        }

        return newArtifacts;
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

    List<String> acceptedFileExtensions = Arrays.asList(".html", ".js", ".css",
            ".png", ".jpg", ".gif", ".ico");

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
