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



import org.wahlzeit_revisited.database.config.AbstractConfig;
import org.wahlzeit_revisited.database.config.ConfigDir;
import org.wahlzeit_revisited.utils.SysConfig;
import org.wahlzeit_revisited.utils.SysLog;

import java.io.File;
import java.io.IOException;
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
		
		try {
			ConfigDir templatesDir = new SysConfig().getTemplatesDir();
			
			String shortDefaultFileName = myLanguage.asIsoCode() + File.separator + "ModelConfig.AbstractModelConfig";
			if(templatesDir.hasDefaultFile(shortDefaultFileName)) {
				String absoluteDefaultFileName = templatesDir.getAbsoluteDefaultConfigFileName(shortDefaultFileName);
				loadProperties(absoluteDefaultFileName);
			}
			
			String shortCustomFileName = myLanguage.asIsoCode() + File.separator + "CustomModelConfig.properties";
			if(templatesDir.hasCustomFile(shortCustomFileName)) {
				String absoluteCustomFileName = templatesDir.getAbsoluteCustomConfigFileName(shortCustomFileName);
				loadProperties(absoluteCustomFileName);
			}
		} catch (IOException ioex) {
			SysLog.logThrowable(ioex);
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
