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

package org.wahlzeit_revisited.model;


import org.wahlzeit_revisited.config.AbstractConfig;
import org.wahlzeit_revisited.utils.SysConfig;
import org.wahlzeit_revisited.utils.SysLog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * A generic implementation of ModelConfig.
 * Subclasses provide the parameters and language-specific handling of text and data.
 */
public abstract class AbstractModelConfig extends AbstractConfig implements ModelConfig {

    /**
     *
     */
    protected Language language = Language.ENGLISH;
    protected DateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
    protected DecimalFormat praiseFormatter = new DecimalFormat("##.#");

    /**
     *
     */
    protected void initialize(Language myLanguage, DateFormat myDateFormatter, DecimalFormat myPraiseFormatter) {
        language = myLanguage;
        dateFormatter = myDateFormatter;
        praiseFormatter = myPraiseFormatter;

        Path languagePath = new SysConfig()
                .getLanguagePath()
                .resolve(myLanguage.asIsoCode())
                .resolve("ModelConfig.properties");

        try {
            File properties = new File(getClass().getClassLoader().getResource(languagePath.toString()).getFile());
            loadProperties(properties);
        } catch (IOException e) {
            SysLog.logThrowable(e);
        }
    }

    /**
     *
     */
    public Language getLanguage() {
        return language;
    }

    /**
     *
     */
    public String getLanguageCode() {
        return doGetValue("LanguageCode");
    }

    /**
     *
     */
    public EmailAddress getModeratorEmailAddress() {
        return EmailAddress.getFromString(doGetValue("ModeratorEmailAddress"));
    }

    /**
     *
     */
    public EmailAddress getAdministratorEmailAddress() {
        return EmailAddress.getFromString(doGetValue("AdministratorEmailAddress"));
    }

    /**
     *
     */
    public EmailAddress getAuditEmailAddress() {
        return EmailAddress.getFromString(doGetValue("AuditEmailAddress"));
    }

    /**
     *
     */
    public String getContinueWithShowPhoto() {
        return doGetValue("ContinueWithShowPhoto");
    }

    /**
     *
     */
    public String getGeneralEmailRegards() {
        return doGetValue("GeneralEmailRegards");
    }

    /**
     *
     */
    public String getGeneralEmailFooter() {
        return doGetValue("GeneralEmailFooter");
    }


    /**
     *
     */
    public String getNotifyAboutPraiseEmailSubject() {
        return doGetValue("NotifyAboutPraiseEmailSubject");
    }

    /**
     *
     */
    public String getNotifyAboutPraiseEmailBody() {
        return doGetValue("NotifyAboutPraiseEmailBody");
    }

    /**
     *
     */
    public String getNotifyAboutPraiseEmailPostScriptum() {
        return doGetValue("NotifyAboutPraiseEmailPostScriptum");
    }

    /**
     *
     */
    public String asDateString(long millis) {
        return dateFormatter.format(millis);
    }

    /**
     *
     */
    public String asPraiseString(double praise) {
        return praiseFormatter.format(praise);
    }

}
