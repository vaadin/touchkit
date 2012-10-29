package com.vaadin.addon.touchkit.itest;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.ui.TouchComboBox;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;

public class TouchComboBoxTest extends AbstractTouchKitIntegrationTest {
    private static final String[] iso3166 = new String[] { "Afghanistan", "AF",
            "Öland Islands", "AX", "Albania", "AL", "Algeria", "DZ",
            "American Samoa", "AS", "Andorra", "AD", "Angola", "AO",
            "Anguilla", "AI", "Antarctica", "AQ", "Antigua And Barbuda", "AG",
            "Argentina", "AR", "Armenia", "AM", "Aruba", "AW", "Australia",
            "AU", "Austria", "AT", "Azerbaijan", "AZ", "Bahamas", "BS",
            "Bahrain", "BH", "Bangladesh", "BD", "Barbados", "BB", "Belarus",
            "BY", "Belgium", "BE", "Belize", "BZ", "Benin", "BJ", "Bermuda",
            "BM", "Bhutan", "BT", "Bolivia", "BO", "Bosnia And Herzegovina",
            "BA", "Botswana", "BW", "Bouvet Island", "BV", "Brazil", "BR",
            "British Indian Ocean Territory", "IO", "Brunei Darussalam", "BN",
            "Bulgaria", "BG", "Burkina Faso", "BF", "Burundi", "BI",
            "Cambodia", "KH", "Cameroon", "CM", "Canada", "CA", "Cape Verde",
            "CV", "Cayman Islands", "KY", "Central African Republic", "CF",
            "Chad", "TD", "Chile", "CL", "China", "CN", "Christmas Island",
            "CX", "Cocos (keeling) Islands", "CC", "Colombia", "CO", "Comoros",
            "KM", "Congo", "CG", "Congo, The Democratic Republic Of The", "CD",
            "Cook Islands", "CK", "Costa Rica", "CR", "Cîte D'ivoire", "CI",
            "Croatia", "HR", "Cuba", "CU", "Cyprus", "CY", "Czech Republic",
            "CZ", "Denmark", "DK", "Djibouti", "DJ", "Dominica", "DM",
            "Dominican Republic", "DO", "Ecuador", "EC", "Egypt", "EG",
            "El Salvador", "SV", "Equatorial Guinea", "GQ", "Eritrea", "ER",
            "Estonia", "EE", "Ethiopia", "ET", "Falkland Islands (malvinas)",
            "FK", "Faroe Islands", "FO", "Fiji", "FJ", "Finland", "FI",
            "France", "FR", "French Guiana", "GF", "French Polynesia", "PF",
            "French Southern Territories", "TF", "Gabon", "GA", "Gambia", "GM",
            "Georgia", "GE", "Germany", "DE", "Ghana", "GH", "Gibraltar", "GI",
            "Greece", "GR", "Greenland", "GL", "Grenada", "GD", "Guadeloupe",
            "GP", "Guam", "GU", "Guatemala", "GT", "Guernsey", "GG", "Guinea",
            "GN", "Guinea-bissau", "GW", "Guyana", "GY", "Haiti", "HT",
            "Heard Island And Mcdonald Islands", "HM",
            "Holy See (vatican City State)", "VA", "Honduras", "HN",
            "Hong Kong", "HK", "Hungary", "HU", "Iceland", "IS", "India", "IN",
            "Indonesia", "ID", "Iran, Islamic Republic Of", "IR", "Iraq", "IQ",
            "Ireland", "IE", "Isle Of Man", "IM", "Israel", "IL", "Italy",
            "IT", "Jamaica", "JM", "Japan", "JP", "Jersey", "JE", "Jordan",
            "JO", "Kazakhstan", "KZ", "Kenya", "KE", "Kiribati", "KI",
            "Korea, Democratic People's Republic Of", "KP",
            "Korea, Republic Of", "KR", "Kuwait", "KW", "Kyrgyzstan", "KG",
            "Lao People's Democratic Republic", "LA", "Latvia", "LV",
            "Lebanon", "LB", "Lesotho", "LS", "Liberia", "LR",
            "Libyan Arab Jamahiriya", "LY", "Liechtenstein", "LI", "Lithuania",
            "LT", "Luxembourg", "LU", "Macao", "MO",
            "Macedonia, The Former Yugoslav Republic Of", "MK", "Madagascar",
            "MG", "Malawi", "MW", "Malaysia", "MY", "Maldives", "MV", "Mali",
            "ML", "Malta", "MT", "Marshall Islands", "MH", "Martinique", "MQ",
            "Mauritania", "MR", "Mauritius", "MU", "Mayotte", "YT", "Mexico",
            "MX", "Micronesia, Federated States Of", "FM",
            "Moldova, Republic Of", "MD", "Monaco", "MC", "Mongolia", "MN",
            "Montenegro", "ME", "Montserrat", "MS", "Morocco", "MA",
            "Mozambique", "MZ", "Myanmar", "MM", "Namibia", "NA", "Nauru",
            "NR", "Nepal", "NP", "Netherlands", "NL", "Netherlands Antilles",
            "AN", "New Caledonia", "NC", "New Zealand", "NZ", "Nicaragua",
            "NI", "Niger", "NE", "Nigeria", "NG", "Niue", "NU",
            "Norfolk Island", "NF", "Northern Mariana Islands", "MP", "Norway",
            "NO", "Oman", "OM", "Pakistan", "PK", "Palau", "PW",
            "Palestinian Territory, Occupied", "PS", "Panama", "PA",
            "Papua New Guinea", "PG", "Paraguay", "PY", "Peru", "PE",
            "Philippines", "PH", "Pitcairn", "PN", "Poland", "PL", "Portugal",
            "PT", "Puerto Rico", "PR", "Qatar", "QA", "Reunion", "RE",
            "Romania", "RO", "Russian Federation", "RU", "Rwanda", "RW",
            "Saint Barthâlemy", "BL", "Saint Helena", "SH",
            "Saint Kitts And Nevis", "KN", "Saint Lucia", "LC", "Saint Martin",
            "MF", "Saint Pierre And Miquelon", "PM",
            "Saint Vincent And The Grenadines", "VC", "Samoa", "WS",
            "San Marino", "SM", "Sao Tome And Principe", "ST", "Saudi Arabia",
            "SA", "Senegal", "SN", "Serbia", "RS", "Seychelles", "SC",
            "Sierra Leone", "SL", "Singapore", "SG", "Slovakia", "SK",
            "Slovenia", "SI", "Solomon Islands", "SB", "Somalia", "SO",
            "South Africa", "ZA",
            "South Georgia And The South Sandwich Islands", "GS", "Spain",
            "ES", "Sri Lanka", "LK", "Sudan", "SD", "Suriname", "SR",
            "Svalbard And Jan Mayen", "SJ", "Swaziland", "SZ", "Sweden", "SE",
            "Switzerland", "CH", "Syrian Arab Republic", "SY",
            "Taiwan, Province Of China", "TW", "Tajikistan", "TJ",
            "Tanzania, United Republic Of", "TZ", "Thailand", "TH",
            "Timor-leste", "TL", "Togo", "TG", "Tokelau", "TK", "Tonga", "TO",
            "Trinidad And Tobago", "TT", "Tunisia", "TN", "Turkey", "TR",
            "Turkmenistan", "TM", "Turks And Caicos Islands", "TC", "Tuvalu",
            "TV", "Uganda", "UG", "Ukraine", "UA", "United Arab Emirates",
            "AE", "United Kingdom", "GB", "United States", "US",
            "United States Minor Outlying Islands", "UM", "Uruguay", "UY",
            "Uzbekistan", "UZ", "Vanuatu", "VU", "Venezuela", "VE", "Viet Nam",
            "VN", "Virgin Islands, British", "VG", "Virgin Islands, U.s.",
            "VI", "Wallis And Futuna", "WF", "Western Sahara", "EH", "Yemen",
            "YE", "Zambia", "ZM", "Zimbabwe", "ZW" };
    public static final Object iso3166_PROPERTY_NAME = "name";
    public static final Object iso3166_PROPERTY_SHORT = "short";

    // public static final Object iso3166_PROPERTY_FLAG = "flag";

    public TouchComboBoxTest() {
        setDescription("This is a test for TouchComboBox");

        TouchComboBox defaultComboBox = new TouchComboBox(
                "Normal comboBox with items", getISO3166Container());
        defaultComboBox.setWidth("15em");
        defaultComboBox.setItemCaptionPropertyId(iso3166_PROPERTY_NAME);
        defaultComboBox
                .setItemCaptionMode(TouchComboBox.ItemCaptionMode.PROPERTY);

        TouchComboBox withNullSelectIdComboBox = new TouchComboBox(
                "ComboBox with items, NullSelectItem 'Finland'",
                getISO3166Container());
        withNullSelectIdComboBox.setWidth("15em");
        withNullSelectIdComboBox.setNullSelectionItemId("FI");
        withNullSelectIdComboBox
                .setItemCaptionPropertyId(iso3166_PROPERTY_NAME);
        withNullSelectIdComboBox
                .setItemCaptionMode(TouchComboBox.ItemCaptionMode.PROPERTY);

        TouchComboBox noNullComboBox = new TouchComboBox(
                "ComboBox with items, null selection not allowed",
                getISO3166Container());
        noNullComboBox.setWidth("15em");
        noNullComboBox.setNullSelectionAllowed(false);
        noNullComboBox.setItemCaptionPropertyId(iso3166_PROPERTY_NAME);
        noNullComboBox
                .setItemCaptionMode(TouchComboBox.ItemCaptionMode.PROPERTY);

        TouchComboBox noNullStartsWithComboBox = new TouchComboBox(
                "ComboBox with items, no null selection and StartsWith filtering",
                getISO3166Container());
        noNullStartsWithComboBox.setWidth("15em");
        noNullStartsWithComboBox.setNullSelectionAllowed(false);
        noNullStartsWithComboBox
                .setItemCaptionPropertyId(iso3166_PROPERTY_NAME);
        noNullStartsWithComboBox
                .setItemCaptionMode(TouchComboBox.ItemCaptionMode.PROPERTY);
        noNullStartsWithComboBox.setFilteringMode(FilteringMode.STARTSWITH);

        addComponent(defaultComboBox);
        addComponent(withNullSelectIdComboBox);
        addComponent(noNullComboBox);
        addComponent(noNullStartsWithComboBox);
    }

    public IndexedContainer getISO3166Container() {
        IndexedContainer c = new IndexedContainer();
        fillIso3166Container(c);
        return c;
    }

    private void fillIso3166Container(IndexedContainer container) {
        container.addContainerProperty(iso3166_PROPERTY_NAME, String.class,
                null);
        container.addContainerProperty(iso3166_PROPERTY_SHORT, String.class,
                null);
        // container.addContainerProperty(iso3166_PROPERTY_FLAG, Resource.class,
        // null);
        for (int i = 0; i < iso3166.length; i++) {
            String name = iso3166[i++];
            String id = iso3166[i];
            Item item = container.addItem(id);
            item.getItemProperty(iso3166_PROPERTY_NAME).setValue(name);
            item.getItemProperty(iso3166_PROPERTY_SHORT).setValue(id);
            // item.getItemProperty(iso3166_PROPERTY_FLAG).setValue(
            // new ThemeResource("../sampler/flags/" + id.toLowerCase()
            // + ".gif"));
        }
        container.sort(new Object[] { iso3166_PROPERTY_NAME },
                new boolean[] { true });
    }
}
