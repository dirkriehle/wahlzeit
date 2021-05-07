/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.services.mailing;

import javax.mail.*;

/**
 * 
 */
public class MailingException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public MailingException(String reason)	{
		super(reason);
	}
	
	/**
	 * 
	 */
	public MailingException(String reason, MessagingException other) {
		super(reason, other);
	}
	
}
