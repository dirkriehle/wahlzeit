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
 
package org.wahlzeit.services.mailing;

import org.wahlzeit.main.ServiceMain;
import org.wahlzeit.services.SysLog;

/**
 * 
 */
public class EmailServiceManager {
	
	/**
	 * 
	 */
	protected static EmailServiceManager instance = null;
	
	/**
	 * 
	 */
	protected static synchronized EmailServiceManager getInstance() {
		if (instance == null) {
			setInstance(new EmailServiceManager());
		}
		return instance;
	}
	
	/**
	 * 
	 */
	protected static void setInstance(EmailServiceManager manager) {
		instance = manager;
	}

	/**
	 * 
	 */
	public static EmailService getDefaultService() {
		return getInstance().doGetDefaultService();
	}
	
	/**
	 * 
	 */
	protected EmailService defaultService = null;
	
	/**
	 * 
	 */
	protected EmailServiceManager() {
		initDefaultService();
	}
	
	/**
	 * 
	 */
	protected void initDefaultService() {
		boolean isInProduction = ServiceMain.getInstance().isInProduction();
		if (isInProduction) {
			defaultService = new SmtpEmailService();
		} else {
			defaultService = new LoggingEmailService(new MockEmailService());
		}
	}
	
	/**
	 * 
	 */
	protected EmailService doGetDefaultService() {
		return defaultService;
	}

}
