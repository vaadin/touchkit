package com.vaadin.addon.touchkit.rootextensions;

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
 * TouchKit settings is a collection of tools that help modifying various touch
 * device related configurations on the html page. The class must be bound to
 * VaadinService and configured early phase to be functional.
 * <p>
 * Most often an instance of this class is accessed via {@link TouchKitServlet}.
 */
public class TouchKitSettings implements BootstrapListener,
        SessionInitListener, SystemMessagesProvider {

    private ViewPortSettings viewPortSettings;
    private WebAppSettings webAppSettings;
    private ApplicationIcons applicationIcons;
    private ApplicationCacheSettings applicationCacheSettings;

    /**
     * Creates and bounds TouchKit settings to {@link VaadinService} instance
     * looked up automatically from a thread local.
     */
    public TouchKitSettings() {
        this(VaadinService.getCurrent());
    }

    /**
     * Creates a new instance of TouchKitSettings and bounds it to given
     * {@link VaadinService}.
     * 
     * @param vaadinService
     *            the vaadin service into the new instance should be bound to.
     */
    public TouchKitSettings(VaadinService vaadinService) {
        viewPortSettings = new ViewPortSettings();
        webAppSettings = new WebAppSettings();
        applicationIcons = new ApplicationIcons();
        applicationCacheSettings = new ApplicationCacheSettings();
        vaadinService.addSessionInitListener(this);

        vaadinService.setSystemMessagesProvider(this);
    }

    public ViewPortSettings getViewPortSettings() {
        return viewPortSettings;
    }

    public WebAppSettings getWebAppSettings() {
        return webAppSettings;
    }

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

    public ApplicationCacheSettings getApplicationCacheSettings() {
        return applicationCacheSettings;
    }

    public void setApplicationCacheSettings(
            ApplicationCacheSettings applicationCacheSettings) {
        this.applicationCacheSettings = applicationCacheSettings;
    }

    public void setApplicationIcons(ApplicationIcons applicationIcons) {
        this.applicationIcons = applicationIcons;
    }

    public void setViewPortSettings(ViewPortSettings viewPortSettings) {
        this.viewPortSettings = viewPortSettings;
    }

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
