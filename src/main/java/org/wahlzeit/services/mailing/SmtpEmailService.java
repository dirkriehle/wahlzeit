/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
