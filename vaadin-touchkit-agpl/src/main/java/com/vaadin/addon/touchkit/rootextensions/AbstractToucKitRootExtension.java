package com.vaadin.addon.touchkit.rootextensions;

import com.vaadin.addon.touchkit.ui.TouchKitApplication;
import com.vaadin.terminal.AbstractExtension;
import com.vaadin.ui.UI;

public class AbstractToucKitRootExtension extends AbstractExtension implements Cloneable {

    public void extend(UI target) {
        super.extend(target);
    }

    /**
     * Creates a clone of this extension and adds it to given Root. This method
     * is typically called by {@link TouchKitApplication} when it configures new
     * roots with application level settings.
     * 
     * @param root
     */
    public void cloneAndExtend(UI root) {
        if (this.getParent() != null) {
            throw new IllegalStateException(
                    "TouchKit root extensions cannot be cloned once attached.");
        }
        try {
            AbstractToucKitRootExtension clone = (AbstractToucKitRootExtension) this
                    .clone();
            clone.extend(root);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
