package org.wahlzeit.services.mailing;

import org.wahlzeit.services.SysLog;

/**
 * 
 */
public class EmailServiceManager {

	/**
	 * 
	 */
	public final static EmailService SMTP_EMAIL_SERVICE = new SmtpEmailService();
	public final static EmailService MOCK_EMAIL_SERVICE = new LoggingEmailService(new MockEmailService());
	
	/**
	 * 
	 */
	public static EmailService getDefaultService() {
		if (SysLog.isInProductionMode()) {
			return getSmtpEmailService();
		} else {
			return getMockEmailService();
		}
	}
	
	/**
	 * 
	 */
	public static EmailService getSmtpEmailService() {
		return SMTP_EMAIL_SERVICE;
	}
	
	/**
	 * 
	 */
	public static EmailService getMockEmailService() {
		return MOCK_EMAIL_SERVICE;
	}
	
}
