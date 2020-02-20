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

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.wahlzeit.services.EmailAddress;

/**
 * 
 */
public class SmtpEmailService extends AbstractEmailService {
	
	/**
	 * 
	 */
	private Session session;
	
	/**
	 * 
	 */
	public SmtpEmailService()	{
		this("localhost", "25", null, null);
	}
	
	/**
	 * 
	 */
	public SmtpEmailService(String host, String port)	{
		this(host, port, null, null);
	}
	
	/**
	 * 
	 */
	public SmtpEmailService(String host, String port, String user, String password)	{
		initialize(host, port, user, password);
	}
	
	/**
	 * 
	 * @methodtype initialization
	 */
	protected void initialize(String host, String port, String user, String password)	{
		Properties props = new Properties();
		
		props.setProperty("mail.smtp.host", host);
		props.setProperty("mail.smtp.port", port);
		
		Authenticator auth = null;
		
		if (user != null && password != null)	{
			auth = new SmtpAuthenticator(user, password);
			props.setProperty("mail.smtp.auth", "true");
		}
		
		session = Session.getDefaultInstance(props, auth);		
	}
	
	/**
	 * 
	 */
	@Override
	protected Message doCreateEmail(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) throws MailingException {
		Message msg = new MimeMessage(session);
		
		try {
			msg.setFrom(from.asInternetAddress());
			msg.addRecipient(Message.RecipientType.TO, to.asInternetAddress());
	
			if (bcc.isValid()) {
				msg.addRecipient(Message.RecipientType.BCC, bcc.asInternetAddress());
			}
	
			msg.setSubject(subject);
			msg.setContent(createMultipart(body));
		} catch (MessagingException ex) {
			throw new MailingException("Creating email failed", ex);
		}
		
		return msg;
	}
	
	/**
	 * 
	 * @methodtype factory
	 * @methodproperties primitive, hook
	 */
	protected Multipart createMultipart(String body) throws MessagingException {
		Multipart mp = new MimeMultipart();
		BodyPart textPart = new MimeBodyPart();
		
		textPart.setText(body); // sets type to "text/plain"
		mp.addBodyPart(textPart);
		
		return mp;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doSendEmail(Message msg) throws MailingException {
		try {
			Transport.send(msg);
		} catch (MessagingException ex) {
			throw new MailingException("Sending email failed", ex);
		}
	}

	/**
	 *
	 */
	protected class SmtpAuthenticator extends Authenticator	{
		
		/**
		 * 
		 */
		private PasswordAuthentication auth;
		
		/**
		 * 
		 */
		public SmtpAuthenticator(String user, String password)	{
			auth = new PasswordAuthentication(user, password);
		}
				
		/**
		 * 
		 */
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return auth;
		}		

	}
	
}
