package com.vaadin.addon.touchkit.settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.vaadin.addon.touchkit.annotations.CacheManifestEnabled;
import com.vaadin.addon.touchkit.annotations.OfflineModeEnabled;
import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.service.ApplicationIcon;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;

/**
 * TouchKitSettings is a collection of tools that help modify various touch
 * device related settings on the html page. The class must be bound to
 * VaadinService and configured in an early phase to be functional.
 * <p>
 * This class should be instantiated by and used through the servlet class,
 * which is {@link TouchKitServlet} by default.
 */
@SuppressWarnings("serial")
public class TouchKitSettings implements BootstrapListener,
        SessionInitListener, SystemMessagesProvider {

    private List<String> stronglyCachedResources;

    /**
     * Interface to select different settings for different kind of devices
     * based on e.g. HTTP header inspection.
     */
    interface SettingSelector<T> extends Serializable {
        /**
         * @param response
         * @return used settings or null if settings shouldn't be used for this
         *         request.
         */
        T select(BootstrapPageResponse response);
    }

    private ViewPortSettings defaultViewPortSettings;
    private ArrayList<SettingSelector<ViewPortSettings>> selectorBasedViewPortSettings = new ArrayList<TouchKitSettings.SettingSelector<ViewPortSettings>>();
    private WebAppSettings webAppSettings;
    private ApplicationIcons applicationIcons;
    private ApplicationCacheSettings applicationCacheSettings;

    /**
     * Creates and binds a TouchKitSettings instance to the
     * {@link VaadinService} instance, which is automatically fetched from a
     * thread local.
     */
    public TouchKitSettings() {
        this(VaadinService.getCurrent());
    }

    /**
     * Creates a new instance of TouchKitSettings and binds it to the given
     * {@link VaadinService}.
     *
     * @param vaadinService
     *            the vaadin service to which the new instance should be bound.
     */
    public TouchKitSettings(VaadinService vaadinService) {
        setViewPortSettings(new ViewPortSettings());

        /*
         * WP8 needs device-width and maximum scale to work like ios (with
         * default settings). Without these custom values pixel size is huge in
         * landscape mode. With these values iphone5 goes to letter box when on
         * home screen.
         *
         * @viewport css rule will replace meta tags in the future and there is
         * a prefixed version in MobileIE10, but it is buggy (landscape mode
         * don't change "device-width").
         */
        final ViewPortSettings ieViewPortSettings = new ViewPortSettings();
        ieViewPortSettings.setViewPortWidth("device-width");
        ieViewPortSettings.setViewPortMaximumScale(1f);

        addViewPortSettings(new SettingSelector<ViewPortSettings>() {

            @Override
            public ViewPortSettings select(BootstrapPageResponse response) {
                String ua = response.getRequest().getHeader("User-Agent");
                if (ua != null && ua.contains("IEMobile")) {
                    return ieViewPortSettings;
                }
                return null;
            }

        });
        setWebAppSettings(new WebAppSettings());
        setApplicationIcons(new ApplicationIcons());
        setApplicationCacheSettings(new ApplicationCacheSettings());
        vaadinService.addSessionInitListener(this);
        vaadinService.setSystemMessagesProvider(this);
    }

    /**
     * Selects viewport settings to be used for given bootstrap
     * request/response.
     *
     * @param response
     * @return The {@link ViewPortSettings}
     */
    private ViewPortSettings selectViewPortSettings(
            BootstrapPageResponse response) {
        for (SettingSelector<ViewPortSettings> s : selectorBasedViewPortSettings) {
            ViewPortSettings selected = s.select(response);
            if (selected != null) {
                return selected;
            }
        }
        return defaultViewPortSettings;
    }

    /**
     * @return The {@link ViewPortSettings} used by default.
     */
    public ViewPortSettings getViewPortSettings() {
        return defaultViewPortSettings;
    }

    /**
     * @return The {@link WebAppSettings}
     */
    public WebAppSettings getWebAppSettings() {
        return webAppSettings;
    }

    /**
     * @return The {@link ApplicationIcons}
     */
    public ApplicationIcons getApplicationIcons() {
        return applicationIcons;
    }

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP no support for portlets currently
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {

        ViewPortSettings viewPortSettings2 = selectViewPortSettings(response);
        if (viewPortSettings2 != null) {
            viewPortSettings2.modifyBootstrapPage(response);
        }
        if (getWebAppSettings() != null) {
            getWebAppSettings().modifyBootstrapPage(response);
        }
        if (getApplicationIcons() != null) {
            getApplicationIcons().modifyBootstrapPage(response);
        }
        if (getApplicationCacheSettings() != null) {
            OfflineModeEnabled offline = null;
            CacheManifestEnabled manifest = null;

            Class<?> clazz = response.getUiClass();
            if (clazz != null) {
                offline = clazz.getAnnotation(OfflineModeEnabled.class);
                manifest = clazz.getAnnotation(CacheManifestEnabled.class);
            }
            if (response.getSession().getService() instanceof VaadinServletService) {
                clazz = ((VaadinServletService) response.getSession()
                        .getService()).getServlet().getClass();
                if (offline == null) {
                    offline = clazz.getAnnotation(OfflineModeEnabled.class);
                }
                if (manifest == null) {
                    manifest = clazz.getAnnotation(CacheManifestEnabled.class);
                }
            }

            getApplicationCacheSettings().setCacheManifestEnabled(
                    manifest == null || manifest.value());
            getApplicationCacheSettings().setOfflineModeEnabled(
                    offline == null || offline.value());
            getApplicationCacheSettings().modifyBootstrapPage(response);
        }

        if (supportsGooglePWA()) {

            String contextPath = response.getRequest().getContextPath();

            Document document = response.getDocument();
            // manifest.json
            // <link rel="manifest" href="/manifest.json">
            Element element = document.createElement("link");
            element.attr("rel", "manifest");
            element.attr("href", contextPath + "/manifest.json");
            document.getElementsByTag("head").get(0).appendChild(element);
            // This meta tag is for some weird reason needed for 100% google PWA ;-)
            element = document.createElement("meta");
            element.attr("name", "theme-color");
            element.attr("content", getWebAppSettings().getTheme_color());
            document.getElementsByTag("head").get(0).appendChild(element);

            if (stronglyCachedResources == null) {
                // TODO make this somehow more stable and cleaner
                String text = document.body().toString().replaceAll("\n", "");

                Pattern p = Pattern.compile(".*widgetset\": \"([^\"]+)\"", Pattern.DOTALL);
                Matcher matcher = p.matcher(text);
                boolean find = matcher.find();
                String wsname = matcher.group(1);

                InputStream resourceAsStream = getClass().getResourceAsStream("/VAADIN/widgetsets/" + wsname + "/" + "safari.manifest");

                // Might be null in case of for example fallback UI
                if (resourceAsStream != null) {
                    List<String> resources = new ArrayList<String>();

                    try {
                        final URI base = new URI(contextPath + "/VAADIN/widgetsets/" + wsname + "/");
                        // the safari permutation is used by most mobile web apps
                        // Read the cached files from the GWT generated manifest file for service workers as well
                        List<String> readLines = IOUtils.readLines(resourceAsStream);
                        boolean cachedFilesStarted = false;
                        for (String readLine : readLines) {
                            if (readLine.startsWith("CACHE:")) {
                                cachedFilesStarted = true;
                                resources.add(contextPath + "/");
                                continue;
                            }
                            if (readLine.startsWith("NETWORK:")) {
                                cachedFilesStarted = false;
                                continue;
                            }
                            if (cachedFilesStarted) {
                                readLine = readLine.trim();
                                if (!readLine.isEmpty()) {
                                	readLine = readLine.replace("\\", "/");
                                    URI resolved = base.resolve(readLine);
                                    resources.add(resolved.toString());
                                }
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(TouchKitSettings.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(TouchKitSettings.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.stronglyCachedResources = resources;
                }

            }
            if (stronglyCachedResources != null && !stronglyCachedResources.isEmpty()) {
                Element serviceworkerregistration = document.createElement("script");
                serviceworkerregistration.attr("type", "text/javascript");
                serviceworkerregistration.appendText("if ('serviceWorker' in navigator) {\n"
                        + "  navigator.serviceWorker.register('"+contextPath+"/service-worker.js', { scope: '"+contextPath+"/' }).then(function(reg) {\n"
                        + "\n"
                        + "    if(reg.installing) {\n"
                        + "      console.log('Service worker installing');\n"
                        + "    } else if(reg.waiting) {\n"
                        + "      console.log('Service worker installed');\n"
                        + "    } else if(reg.active) {\n"
                        + "      console.log('Service worker active');\n"
                        + "    }\n"
                        + "\n"
                        + "  }).catch(function(error) {\n"
                        + "    console.log('Registration failed with ' + error);\n"
                        + "  });\n"
                        + "}");
                document.body().appendChild(serviceworkerregistration);
            }

        }        
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSession().addBootstrapListener(this);
        event.getSession().addRequestHandler(new RequestHandler() {
            @Override
            public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException {
                final String pathInfo = request.getPathInfo();
                String contextPath = request.getContextPath();
                if (pathInfo.endsWith("manifest.json")) {
                    // TODO write manifest.json using Jackson or similar
                    PrintWriter writer = response.getWriter();
					writer.append("{\n"
                            + "  \"short_name\": \"" + getWebAppSettings().getApplicationShortName() + "\",\n"
                            + "  \"name\": \"" + getWebAppSettings().getApplicationName() + "\",\n"
                            + "  \"display\": \"" + getWebAppSettings().getDisplay() + "\",\n"
                            + "  \"start_url\": \"" + contextPath + getWebAppSettings().getStart_url() + "\",\n"
                            + "  \"background_color\": \"" + getWebAppSettings().getBackground_color() + "\",\n"
                            + "  \"theme_color\": \"" + getWebAppSettings().getTheme_color() + "\",\n"
                            + "  \"icons\": [\n");

                    final ApplicationIcon[] icons = getApplicationIcons().getApplicationIcons();
                    for (int i = 0; i < icons.length; i++) {
                        ApplicationIcon icon = icons[i];
                        if (i > 0) {
                            writer.println(",");
                        }
                        writer.print("    {\n");
                        writer.print("\"src\": \"" + icon.getHref() + "\",");
                        writer.print("\"sizes\": \"" + icon.getSizes() + "\"");
                        writer.print("    }\n");
                    }
                    writer.print("    ]\n");
                    writer.append("}");

                    return true;
                } else if (pathInfo.endsWith("service-worker.js")) {
                    response.setContentType("text/javascript");
                    response.setCacheTime(-1);
                    PrintWriter writer = response.getWriter();
                    writer.write("self.addEventListener('install', e => {\n"
                            + "  e.waitUntil(\n"
                            + "    caches.open(\"tkcache\").then(c => {\n"
                            + "      return c.addAll([\n");

                    // TODO consider using https://github.com/GoogleChromeLabs/sw-precache
                    if (stronglyCachedResources != null) {
                        for (String stronglyCachedResource : stronglyCachedResources) {
                            writer.write("'");
                            writer.write(stronglyCachedResource);
                            writer.write("',\n");
                        }
                    } else {
                        Logger.getLogger(TouchKitSettings.class.getName()).log(Level.SEVERE, "strongly cached resources could not be found");
                    }

                    writer.write(""
                            + "      ]).then(() => self.skipWaiting());\n"
                            + "    })\n"
                            + "  );\n"
                            + "});\n"
                            + "\n"
                            + "self.addEventListener('fetch', e => {\n"
                            + "  e.respondWith(\n"
                            + "    caches.open(\"tkcache\").then(c => {\n"
                            + "      return c.match(e.request).then(res => {\n"
                            + "        return res || fetch(e.request)\n"
                            + "      });\n"
                            + "    })\n"
                            + "  );\n"
                            + "});");
                    return true;
                }

                return false;
            }

        }
        );
    }

    /**
     * @return The {@link ApplicationCacheSettings}
     */
    public ApplicationCacheSettings getApplicationCacheSettings() {
        return applicationCacheSettings;
    }

    /**
     * Sets the {@link ApplicationCacheSettings} instance to use.
     *
     * @param applicationCacheSettings
     *            the {@link ApplicationCacheSettings} instance to use.
     */
    public void setApplicationCacheSettings(
            ApplicationCacheSettings applicationCacheSettings) {
        this.applicationCacheSettings = applicationCacheSettings;
    }

    /**
     * Sets the {@link ApplicationIcons} instance to use.
     *
     * @param applicationIcons
     *            the {@link ApplicationIcons} instance to use.
     */
    public void setApplicationIcons(ApplicationIcons applicationIcons) {
        this.applicationIcons = applicationIcons;
    }

    /**
     * Sets the {@link ViewPortSettings} instance to use by default.
     *
     * @param viewPortSettings
     *            the {@link ViewPortSettings} instance to use.
     * @see #addViewPortSettings(SettingSelector)
     */
    public void setViewPortSettings(ViewPortSettings viewPortSettings) {
        defaultViewPortSettings = viewPortSettings;
    }

    /**
     * Adds a request dependent view port settings. If selector returns
     * settings, it will override default view port setting.
     *
     * @param viewPortSettingSelector
     */
    public void addViewPortSettings(
            SettingSelector<ViewPortSettings> viewPortSettingSelector) {
        selectorBasedViewPortSettings.add(viewPortSettingSelector);
    }

    /**
     * Sets the {@link WebAppSettings} instance to use.
     *
     * @param iosWebAppSettings
     *            the {@link WebAppSettings} instance to use.
     */
    public void setWebAppSettings(WebAppSettings iosWebAppSettings) {
        webAppSettings = iosWebAppSettings;
    }

    @Override
    public SystemMessages getSystemMessages(
            SystemMessagesInfo systemMessagesInfo) {
        CustomizedSystemMessages customizedSystemMessages = new CustomizedSystemMessages();
        customizedSystemMessages
                .setCommunicationErrorNotificationEnabled(false);
        customizedSystemMessages.setSessionExpiredNotificationEnabled(false);
        return customizedSystemMessages;
    }

    /**
     * @return true if Google style service worker + json manifest style PWA 
     * should be used instead of original iOS style home screen web app thingies.
     */
    public static boolean supportsGooglePWA() {
        try {
            VaadinRequest currentRequest = VaadinServletService.getCurrentRequest();
            String useragentheader = currentRequest.getHeader("User-Agent").toLowerCase();
            // Simply expect all chromes to support
            if (useragentheader.contains("chrome") || useragentheader.contains("firefox")) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("Detecting sw support failed!");
        }
        return false;
    }
}
