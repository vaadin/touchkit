package com.vaadin.addon.touchkit.rootextensions;

import java.util.LinkedList;

import com.vaadin.addon.touchkit.service.ApplicationIcon;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;

public class ApplicationIcons extends AbstractTouchKitRootExtension implements BootstrapListener {
    
    private LinkedList<ApplicationIcon> applicationIcon = new LinkedList<ApplicationIcon>();
    
    /**
     * Sets the webpage icon for this web app. This icon may be used by the
     * client OS in case user bookmarks the web page containing this window.
     * 
     * @param url
     * @see #addApplicationIcon(int, int, String)
     */
    public void addApplicationIcon(final String url) {
        applicationIcon.add(new ApplicationIcon() {

            public String getSizes() {
                return null;
            }

            public String getHref() {
                return url;
            }

            public boolean isPreComposed() {
                return false;
            }

        });
    }

    /**
     * Sets the application icon for this web app. This icon may be used by the
     * client OS in case user bookmarks the web page containing this window.
     * <p>
     * 
     * See {@link http://developer.apple.com/library/safari/} for more details.
     * 
     * @param width
     * @param height
     * @param url
     */
    public void addApplicationIcon(final int width, final int height,
            final String url, final boolean preComposed) {
        applicationIcon.add(new ApplicationIcon() {

            public String getSizes() {
                return width + "x" + height;
            }

            public String getHref() {
                return url;
            }

            public boolean isPreComposed() {
                return preComposed;
            }
        });
    }


    /**
     * Gets the {@link ApplicationIcon}s that have previously been added to this
     * window with {@link #addApplicationIcon(String)} or
     * {@link #addApplicationIcon(int, int, String)}.
     * 
     * @return
     */
    public ApplicationIcon[] getApplicationIcons() {
        return applicationIcon.toArray(new ApplicationIcon[applicationIcon
                .size()]);
    }


    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        // FIXME change this part to use variables in this object
        
    }

    
    
}
