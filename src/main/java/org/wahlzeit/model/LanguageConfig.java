/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 *
 * This file is part of the Wahlzeit photo rating application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.wahlzeit.model;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * LanguageConfigs provides a simple access points to different language-based configurations. For every available
 * language, there is a configuration, which captures local specific stuff.
 */
public class LanguageConfig {

    /**
     *
     */
    protected static final Map<Locale, LanguageConfig> CONFIGURATIONS = new HashMap<>();

    private final ResourceBundle resourceBundle;

    /**
     * Private constructor due flyweight pattern
     */
    private LanguageConfig(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     *
     */
    public static LanguageConfig get(Locale language) {
        if (!CONFIGURATIONS.containsKey(language)) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("lang/i18n", language);
            LanguageConfig config = new LanguageConfig(resourceBundle);

            CONFIGURATIONS.put(language, config);
        }
        return CONFIGURATIONS.get(language);
    }

    // System config
    public EmailAddress getAdministratorEmailAddress() {
        String mail = resourceBundle.getString("AdministratorEmailAddress");
        return new EmailAddress(mail);
    }

    // General email
    public String getGeneralEmailRegards() {
        return resourceBundle.getString("GeneralEmailRegards");
    }

    public String getGeneralEmailFooter() {
        return resourceBundle.getString("GeneralEmailFooter");
    }

    // Notify about praise email
    public String getNotifyAboutPraiseEmailSubject() {
        return resourceBundle.getString("NotifyAboutPraiseEmailSubject");
    }

    public String getNotifyAboutPraiseEmailBody() {
        return resourceBundle.getString("NotifyAboutPraiseEmailBody");
    }

    public String getNotifyAboutPraiseEmailPostScriptum() {
        return resourceBundle.getString("NotifyAboutPraiseEmailPostScriptum");
    }

}
