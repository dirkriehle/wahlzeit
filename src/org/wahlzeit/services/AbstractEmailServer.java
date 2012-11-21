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

import javax.mail.*;
import javax.mail.internet.*;

public abstract class AbstractEmailServer implements EmailServer{
	
	/**
	 * 
	 */
	protected static final EmailServer REAL_INSTANCE = new DefaultEmailServer();
	protected static final EmailServer NULL_INSTANCE = new NullEmailServer();

	/**
	 * 
	 */
	protected static EmailServer instance = getInstanceFromMode();
	
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
	 * @methodtype set
	 */
	public static void setInstance(AbstractEmailServer server) {
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
	protected AbstractEmailServer() {
	}
	
	/**
	 * 
	 * @methodproperties convenience
	 */
	public synchronized void sendEmail(EmailAddress from, EmailAddress to, String subject, String body) {
		sendEmail(from, to, EmailAddress.NONE, subject, body);
	}
	
	/**
	 * 
	 * @methodproperties composed
	 */
	public synchronized void sendEmail(EmailAddress from, EmailAddress to, EmailAddress bcc, String subject, String body) {
		try {
			Message msg = createMessage(from, to, bcc, subject, body);
			doSendEmail(msg);
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
		}
	}

	/**
	 * 
	 * @methodtype factory
	 * @methodproperties composed
	 */
	abstract protected Message createMessage(EmailAddress from, EmailAddress to,
			EmailAddress bcc, String subject, String body)
			throws MessagingException, AddressException;

	/**
	 * 
	 * @methodproperties primitive, hook
	 */
	abstract protected void doSendEmail(Message msg) throws Exception;
	
}
