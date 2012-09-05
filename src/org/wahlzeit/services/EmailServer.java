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

import java.io.*;
import java.net.*;
import java.util.*;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.*;

/**
 * The EmailServer service lets clients send emails.
 * 
 * @author driehle
 *
 */
public class EmailServer {

	/**
	 * 
	 */
	protected static final EmailServer REAL_INSTANCE = new EmailServer();
	protected static final EmailServer NULL_INSTANCE = new NullEmailServer();

	/**
	 * 
	 */
	protected static EmailServer instance = getInstanceFromMode();
	
	/**
	 * 
	 */
	protected Session session = null;
	
	/**
	 * 
	 */
	public static EmailServer getInstance() {
		return instance;
	}
	
	/**
	 * 
	 */
	public static EmailServer getInstanceFromMode() {
		if (SysLog.isInProductionMode()) {
			return REAL_INSTANCE;
		} else {
			return NULL_INSTANCE;
		}
	}

	/**
	 * 
	 */
	public static void setInstance(EmailServer server) {
		instance = server;
	}
	
	/**
	 * 
	 */
	public static void setNullInstance() {
		instance = NULL_INSTANCE;
	}
	
	/**
	 * 
	 */
	protected EmailServer() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "localhost");
	    session = Session.getDefaultInstance(properties, null);
	}
	
	/**
	 * 
	 */
	public synchronized void sendEmail(EmailAddress from, EmailAddress to, String subject, String body) {
		sendEmail(from, to, EmailAddress.NONE, subject, body);
	}
	
	/**
	 * 
	 */
	public synchronized void sendEmail(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) {
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from.asString()));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to.asString()));
			
			if (bcc != EmailAddress.NONE) {
				msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc.asString()));
			}

			msg.setSubject(subject);

			Multipart mp = new MimeMultipart();
			BodyPart textPart = new MimeBodyPart();
			textPart.setText(body); // sets type to "text/plain"
			mp.addBodyPart(textPart);
			msg.setContent(mp);

			doSendEmail(msg);
			
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
		}
	}
	
	/**
	 * 
	 */
	protected void doSendEmail(Message msg) throws Exception {
		Transport.send(msg);
	}
	
}
