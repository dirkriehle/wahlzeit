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

package org.wahlzeit.services;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

/**
 * 
 * @author tilmanbeck
 *
 */
public class DefaultEmailServer extends AbstractEmailServer implements EmailServer{
	
	protected Session session = null;
	
	public DefaultEmailServer() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "localhost");
	    session = Session.getDefaultInstance(properties, null);
	}
	
	/**
	 * 
	 * @methodtype factory
	 * @methodproperties composed
	 */
	protected Message createMessage(EmailAddress from, EmailAddress to,
			EmailAddress bcc, String subject, String body)
			throws MessagingException, AddressException {
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from.asString()));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to.asString()));
		
		if (bcc != EmailAddress.NONE) {
			msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc.asString()));
		}

		msg.setSubject(subject);
		msg.setContent(createMultipart(body));
		return msg;
	}
	/**
	 * 
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
	protected void doSendEmail(Message msg) throws Exception {
		Transport.send(msg);		
	}
	
	
	
}
