package com.vaadin.addon.touchkit.service;

import java.io.Serializable;

public interface ApplicationIcon extends Serializable {
    String getSizes();

    String getHref();

    boolean isPreComposed();
}
