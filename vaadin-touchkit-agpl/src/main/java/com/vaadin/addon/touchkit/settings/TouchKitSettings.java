package com.vaadin.addon.touchkit.settings;

import java.io.Serializable;
import java.util.ArrayList;

import com.vaadin.addon.touchkit.annotations.CacheManifestEnabled;
import com.vaadin.addon.touchkit.annotations.OfflineModeEnabled;
import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletService;

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
                    manifest == null || manifest.value());
            getApplicationCacheSettings().modifyBootstrapPage(response);
        }
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSession().addBootstrapListener(this);
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
}
