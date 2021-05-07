/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
 
package org.wahlzeit.services.mailing;

import org.wahlzeit.main.ServiceMain;

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
