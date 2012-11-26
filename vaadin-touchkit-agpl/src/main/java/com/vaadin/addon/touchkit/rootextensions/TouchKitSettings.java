package com.vaadin.addon.touchkit.rootextensions;

import java.util.Locale;

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

public class TouchKitSettings implements BootstrapListener,
        SessionInitListener, SystemMessagesProvider {

    private ViewPortSettings viewPortSettings;
    private IosWebAppSettings iosWebAppSettings;
    private ApplicationIcons applicationIcons;
    private ApplicationCacheSettings applicationCacheSettings;

    public ViewPortSettings getViewPortSettings() {
        return viewPortSettings;
    }

    public IosWebAppSettings getIosWebAppSettings() {
        return iosWebAppSettings;
    }

    public ApplicationIcons getApplicationIcons() {
        return applicationIcons;
    }

    public TouchKitSettings() {
        this(VaadinService.getCurrent());
    }

    public TouchKitSettings(VaadinService vaadinService) {
        viewPortSettings = new ViewPortSettings();
        iosWebAppSettings = new IosWebAppSettings();
        applicationIcons = new ApplicationIcons();
        applicationCacheSettings = new ApplicationCacheSettings();
        vaadinService.addSessionInitListener(this);

        vaadinService.setSystemMessagesProvider(this);
    }

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP no support for portlets currently
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        getViewPortSettings().modifyBootstrapPage(response);
        getIosWebAppSettings().modifyBootstrapPage(response);
        getApplicationIcons().modifyBootstrapPage(response);
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

    public void setIosWebAppSettings(IosWebAppSettings iosWebAppSettings) {
        this.iosWebAppSettings = iosWebAppSettings;
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
