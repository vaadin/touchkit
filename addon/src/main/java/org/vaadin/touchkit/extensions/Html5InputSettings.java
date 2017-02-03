package org.vaadin.touchkit.extensions;

import java.util.Locale;

import org.vaadin.touchkit.gwt.client.vcom.Html5InputSettingsState;

import com.vaadin.server.AbstractExtension;
import com.vaadin.v7.ui.TextField;

/**
 * A TextField extension to control the new input properties that were
 * introduced in HTML5. Browser support for these properties may vary, but among
 * modern mobile browsers it is generally quite good.
 * <p>
 * <strong>Note</strong> that values are passed as-is to the related DOM
 * element, so be sure to validate any user submitted values.
 * <p>
 * See the following links for references for the supported properties: <a
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
     * "https://developer.apple.com/library/safari/documentation/AppleApplications/Reference/SafariHTMLRef/Articles/Attributes.html#//apple_ref/html/attribute/autocapitalize"
     * >Apple ios developer docs</a>.
     * 
     * @param value
     *            The value for the "autocapitalize" property.
     */
    public void setAutoCapitalize(String value) {
        getState().props.put("autocapitalize", value);
    }

    /**
     * @return The value for the "autocapitalize" property or null if none set
     *         through {@link #setAutoCapitalize(String)}
     */
    public String getAutoCapitalize() {
        return getState().props.get("autocapitalize");
    }

    /**
     * See <a href=
     * "https://developer.apple.com/library/safari/#documentation/AppleApplications/Reference/SafariHTMLRef/Articles/Attributes.html%23//apple_ref/html/attribute/autocorrect"
     * >Apple ios developer docs</a>.
     * 
     * @param value
     *            true to turn auto correct on and false to turn it off for the
     *            input field.
     */
    public void setAutoCorrect(boolean value) {
        setProperty("autocorrect", value ? "on" : "off");
    }

    /**
     * @return true if auto correct is on, false if off or not set.
     */
    public boolean getAutoCorrect() {
        return !"off".equals(getState().props.get("autocorrect"));
    }

    /**
     * Specifies the placeholder text displayed in light grey when the search
     * input field is not currently in use.
     * 
     * @param placeholder
     *            The placeholder text.
     */
    public void setPlaceholder(String placeholder) {
        setProperty("placeholder", placeholder);
    }

    /**
     * @return The placeholder text or null if none set using
     *         {@link #setPlaceholder(String)}
     */
    public String getPlaceholder() {
        return getState().props.get("placeholder");
    }

    /**
     * Specifies the minimum value or input length for the field.
     * 
     * @param min
     *            The minimum value or input length for the field.
     */
    public void setMin(Number min) {
        setMin(String.format(Locale.US, "%d", min));
    }

    /**
     * Specifies the minimum value or input length for the field.
     * 
     * @param min
     *            The minimum value or input length for the field.
     */
    public void setMin(String min) {
        getState().props.put("min", min);
    }

    /**
     * @return The minimum value or input length of the field or null if none
     *         set.
     */
    public String getMin() {
        return getState().props.get("min");
    }

    /**
     * Specifies the maximum value or input length for the field in characters.
     * 
     * @param max
     *            The maximum value or input length of the field in characters.
     */
    public void setMax(Number max) {
        setMax(String.format(Locale.US, "%d", max));
    }

    /**
     * Specifies the maximum value or input length for the field in characters.
     * 
     * @param max
     *            The maximum value or input length of the field in characters.
     */
    public void setMax(String max) {
        getState().props.put("max", max);
    }

    /**
     * @return the maximum value or input length of the field
     */
    public String getMax() {
        return getState().props.get("max");
    }

    /**
     * Set the step value for a number field
     * 
     * @param step
     *            the delta for each step.
     */
    public void setStep(Number step) {
        setMax(String.format(Locale.US, "%d", step));
    }

    /**
     * Set the step value for a number field
     * 
     * @param step
     *            the delta for each step.
     */
    public void setStep(String step) {
        getState().props.put("step", step);
    }

    /**
     * @return The delta for each step in a number field, or null if none set.
     */
    public String getStep() {
        return getState().props.get("step");
    }

    /**
     * Sets a generic property of the textfield. Instead of using this method
     * directly. Developers should prefer to use typed API instead.
     * 
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        getState().props.put(key, value);
    }

    /**
     * Returns the property identified by the provided key.
     * 
     * @param key
     *            The key of the property to return
     * @return The value of the property identified by the key argument.
     */
    public String getProperty(String key) {
        return getState().props.get(key);
    }

    @Override
    protected Html5InputSettingsState getState() {
        return (Html5InputSettingsState) super.getState();
    }

}
