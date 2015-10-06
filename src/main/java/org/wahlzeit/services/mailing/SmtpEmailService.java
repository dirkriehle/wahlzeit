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

import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.LogBuilder;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 */
public class SmtpEmailService extends AbstractEmailService {

	private static final Logger log = Logger.getLogger(SmtpEmailService.class.getName());

	/**
	 *
	 */
	private Session session;

	/**
	 * Default constructor for GAE. There is usually no need to change those parameter.
	 */
	public SmtpEmailService() {
		this("smtp.google.com", "smtp", null);
	}

	/**
	 *
	 */
	public SmtpEmailService(String host, String protocol, Authenticator authenticator) {
		initialize(host, protocol, authenticator);
	}

	/**
	 * @methodtype initialization
	 */
	protected void initialize(String host, String protocol, Authenticator authenticator) {
		Properties props = new Properties();

		props.put("mail.host", host);
		props.put("mail.transport.protocol", protocol);

		session = Session.getDefaultInstance(props, authenticator);
	}

	/**
	 *
	 */
	@Override
	protected Message doCreateEmail(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body)
			throws MailingException {
		Message msg = new MimeMessage(session);

		try {
			msg.setFrom(from.asInternetAddress());
			msg.addRecipient(Message.RecipientType.TO, to.asInternetAddress());
			if (bcc.isValid()) {
				msg.addRecipient(Message.RecipientType.BCC, bcc.asInternetAddress());
			}
			msg.setSubject(subject);
			msg.setContent(createMultipart(body));
		} catch (MessagingException e) {
			log.warning(LogBuilder.createSystemMessage().addException("failed to create email", e).toString());
			throw new MailingException(e.getMessage());
		}
		return msg;
	}

	/**
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
			log.config(LogBuilder.createSystemMessage().
					addMessage("email send").
					addParameter("subject", msg.getSubject()).toString());
		} catch (MessagingException ex) {
			throw new MailingException("Sending email failed", ex);
		}
	}

}
