package com.vaadin.addon.touchkit.extensions;

import java.util.Locale;

import com.vaadin.addon.touchkit.gwt.client.vcom.Html5InputSettingsState;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.TextField;

/**
 * A TextField extension to control new input properties introduced in HTML5.
 * Browser support for these may vary, but among modern mobile browsers it is
 * generally quite good.
 * <p>
 * <strong>Note</strong>, that values are passed as is to the browsers related
 * dom element, so be sure to validate possibly user submitted values.
 * <p>
 * References for adjustable properties: <a
 * href="http://www.w3schools.com/html/html5_form_attributes.asp">W3
 * Schools</a>, <a href=
 * "http://developer.apple.com/library/safari/#documentation/AppleApplications/Reference/SafariHTMLRef/Articles/Attributes.html"
 * >Apple</a>.
 * 
 */
@SuppressWarnings("serial")
public class Html5InputSettings extends AbstractExtension {

    public Html5InputSettings(TextField tf) {
        extend(tf);
    }

    public void setAutoComplete(boolean value) {
        setProperty("autocomplete", value ? "on" : "off");
    }

    public boolean getAutoComplete() {
        return !"off".equals(getState().props.get("autocomplete"));
    }

    /**
     * See <a href=
     * "https://developer.apple.com/library/safari/#documentation/AppleApplications/Reference/SafariHTMLRef/Articles/Attributes.html#//apple_ref/html/attribute/autocapitalize"
     * >Apple ios developer docs</a>.
     * 
     * @param value
     */
    public void setAutoCapitalize(String value) {
        getState().props.put("autocapitalize", value);
    }

    public String getAutoCapitalize() {
        return getState().props.get("autocapitalize");
    }

    /**
     * * See <a href=
     * "https://developer.apple.com/library/safari/#documentation/AppleApplications/Reference/SafariHTMLRef/Articles/Attributes.html%23//apple_ref/html/attribute/autocorrect"
     * >Apple ios developer docs</a>.
     * 
     * @param value
     */
    public void setAutoCorrect(boolean value) {
        setProperty("autocorrect", value ? "on" : "off");
    }

    public boolean getAutoCorrect() {
        return !"off".equals(getState().props.get("autocorrect"));
    }

    public void setPlaceholder(String placeholder) {
        setProperty("placeholder", placeholder);
    }

    public String getPlaceholder() {
        return getState().props.get("placeholder");
    }

    public void setMin(Number min) {
        setMin(String.format(Locale.US, "%d", min));
    }

    public void setMin(String min) {
        getState().props.put("min", min);
    }

    public String getMin() {
        return getState().props.get("min");
    }

    public void setMax(Number max) {
        setMax(String.format(Locale.US, "%d", max));
    }

    public void setMax(String max) {
        getState().props.put("max", max);
    }

    public String getMax() {
        return getState().props.get("max");
    }

    public void setStep(Number step) {
        setMax(String.format(Locale.US, "%d", step));
    }

    public void setStep(String step) {
        getState().props.put("step", step);
    }

    public String getStep() {
        return getState().props.get("step");
    }

    /**
     * Sets a generic property of the textfield. Instead of using this method
     * directly, developers should prefer to use typed API instead.
     * 
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        getState().props.put(key, value);
    }

    public String getProperty(String key) {
        return getState().props.get(key);
    }

    @Override
    protected Html5InputSettingsState getState() {
        return (Html5InputSettingsState) super.getState();
    }

}
