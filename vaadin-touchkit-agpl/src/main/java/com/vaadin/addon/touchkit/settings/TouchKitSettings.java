package com.vaadin.addon.touchkit.settings;

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

/**
 * TouchKitSettings is a collection of tools that help modify various touch
 * device related settings on the html page. The class must be bound to
 * VaadinService and configured in an early phase to be functional.
 * <p>
 * This class should be instantiated by and used through the servlet class,
 * which is {@link TouchKitServlet} by default.
 */
public class TouchKitSettings implements BootstrapListener,
        SessionInitListener, SystemMessagesProvider {

    private ViewPortSettings viewPortSettings;
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
        setWebAppSettings(new WebAppSettings());
        setApplicationIcons(new ApplicationIcons());
        setApplicationCacheSettings(new ApplicationCacheSettings());
        vaadinService.addSessionInitListener(this);

        vaadinService.setSystemMessagesProvider(this);
    }

    /**
     * @return The {@link ViewPortSettings}
     */
    public ViewPortSettings getViewPortSettings() {
        return viewPortSettings;
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
        if (getViewPortSettings() != null)
            getViewPortSettings().modifyBootstrapPage(response);
        if (getWebAppSettings() != null)
            getWebAppSettings().modifyBootstrapPage(response);
        if (getApplicationIcons() != null)
            getApplicationIcons().modifyBootstrapPage(response);
        if (getApplicationCacheSettings() != null)
            getApplicationCacheSettings().modifyBootstrapPage(response);
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
     * Sets the {@link ViewPortSettings} instance to use.
     * 
     * @param viewPortSettings
     *            the {@link ViewPortSettings} instance to use.
     */
    public void setViewPortSettings(ViewPortSettings viewPortSettings) {
        this.viewPortSettings = viewPortSettings;
    }

    /**
     * Sets the {@link WebAppSettings} instance to use.
     * 
     * @param iosWebAppSettings
     *            the {@link WebAppSettings} instance to use.
     */
    public void setWebAppSettings(WebAppSettings iosWebAppSettings) {
        this.webAppSettings = iosWebAppSettings;
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
