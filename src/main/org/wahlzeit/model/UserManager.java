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

package org.wahlzeit.model;

import java.util.*;
import java.sql.*;

import org.wahlzeit.services.*;
import org.wahlzeit.services.mailing.*;

/**
 * The UserManager provides access to and manages Users (including Moderators and Administrators).
 * 
 * @author dirkriehle
 *
 */
public class UserManager extends ObjectManager {

	/**
	 * Reserved names that cannot be registered by regular users
	 * @FIXME Load from file eventually
	 */
	public static List<String> reservedNames = Arrays.asList( 
		"admin",
		"anonymous",
		"flickr"
	);
	
	/**
	 *
	 */
	protected static UserManager instance = new UserManager();

	/**
	 * 
	 */
	public static UserManager getInstance() {
		return instance;
	}
	
	/**
	 * Maps nameAsTag to user of that name (as tag)
	 */
	protected Map<String, User> users = new HashMap<String, User>();
	
	/**
	 * 
	 */
	protected Random codeGenerator = new Random(System.currentTimeMillis());

	/**
	 * 
	 */
	public boolean hasUserByName(String name) {
		assertIsNonNullArgument(name, "user-by-name");
		return hasUserByTag(Tags.asTag(name));
	}
	
	/**
	 * 
	 */
	public boolean hasUserByTag(String tag) {
		assertIsNonNullArgument(tag, "user-by-tag");
		return getUserByTag(tag) != null;
	}
	
	/**
	 * 
	 */
	protected boolean doHasUserByTag(String tag) {
		return doGetUserByTag(tag) != null;
	}
	
	/**
	 * 
	 */
	public User getUserByName(String name) {
		return getUserByTag(Tags.asTag(name));
	}
	
	/**
	 * 
	 */
	public User getUserByTag(String tag) {
		assertIsNonNullArgument(tag, "user-by-tag");

		User result = doGetUserByTag(tag);

		if (result == null) {
			try {
				PreparedStatement stmt = getReadingStatement("SELECT * FROM users WHERE name_as_tag = ?");
				result = (User) readObject(stmt, tag);
			} catch (SQLException sex) {
				SysLog.logThrowable(sex);
			}
			
			if (result != null) {
				doAddUser(result);
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	public boolean isReservedUserName(String userName) {
		return reservedNames.contains(Tags.asTag(userName));
	}
		
	/**
	 * 
	 */
	protected User doGetUserByTag(String tag) {
		return users.get(tag);
	}
	
	/**
	 * 
	 * @methodtype factory
	 */
	protected User createObject(ResultSet rset) throws SQLException {
		User result = null;

		AccessRights rights = AccessRights.getFromInt(rset.getInt("rights"));
		if (rights == AccessRights.USER) {
			result = new User();
			result.readFrom(rset);
		} else if (rights == AccessRights.MODERATOR) {
			result = new Moderator();
			result.readFrom(rset);
		} else if (rights == AccessRights.ADMINISTRATOR) {
			result = new Administrator();
			result.readFrom(rset);
		} else {
			SysLog.logSysInfo("received NONE rights value");
		}

		return result;
	}
	
	/**
	 * 
	 */
	public void addUser(User user) {
		assertIsNonNullArgument(user);
		assertIsUnknownUserAsIllegalArgument(user);

		try {
			int id = user.getId();
			PreparedStatement stmt = getReadingStatement("INSERT INTO users(id) VALUES(?)");
			createObject(user, stmt, id);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
		
		doAddUser(user);		
	}
	
	/**
	 * 
	 */
	protected void doAddUser(User user) {
		users.put(user.getNameAsTag(), user);
	}
	
	/**
	 * 
	 */
	public void deleteUser(User user) {
		assertIsNonNullArgument(user);
		doDeleteUser(user);

		try {
			PreparedStatement stmt = getReadingStatement("DELETE FROM users WHERE id = ?");
			deleteObject(user, stmt);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
		
		assertIsUnknownUserAsIllegalState(user);
	}
	
	/**
	 * 
	 */
	protected void doDeleteUser(User user) {
		users.remove(user.getNameAsTag());
	}
	
	/**
	 * 
	 */
	public void loadUsers(Collection<User> result) {
		try {
			PreparedStatement stmt = getReadingStatement("SELECT * FROM users");
			readObjects(result, stmt);
			for (Iterator<User> i = result.iterator(); i.hasNext(); ) {
				User user = i.next();
				if (!doHasUserByTag(user.getNameAsTag())) {
					doAddUser(user);
				} else {
					SysLog.logSysInfo("user", user.getName(), "user had already been loaded");
				}
			}
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
		
		SysLog.logSysInfo("loaded all users");
	}
	
	/**
	 * 
	 */
	public long createConfirmationCode() {
		return Math.abs(codeGenerator.nextLong() / 2);
	}
	
	/**
	 * 
	 */
	public void emailWelcomeMessage(UserSession us, User user) {
		EmailAddress from = us.cfg().getAdministratorEmailAddress();
		EmailAddress to = user.getEmailAddress();

		String emailSubject = us.cfg().getWelcomeEmailSubject();
		String emailBody = us.cfg().getWelcomeEmailBody() + "\n\n";
		emailBody += us.cfg().getWelcomeEmailUserName() + user.getName() + "\n\n"; 
		emailBody += us.cfg().getConfirmAccountEmailBody() + "\n\n";
		emailBody += user.getSiteUrlAsString() + "confirm?code=" + user.getConfirmationCode() + "\n\n"; // @TODO Application
		emailBody += us.cfg().getGeneralEmailRegards() + "\n\n----\n";
		emailBody += us.cfg().getGeneralEmailFooter() + "\n\n";

		EmailService emailService = EmailServiceManager.getDefaultService();
		emailService.sendEmailIgnoreException(from, to, us.cfg().getAuditEmailAddress(), emailSubject, emailBody);
	}
	
	/**
	 * 
	 */
	public void emailConfirmationRequest(UserSession us, User user) {
		EmailAddress from = us.cfg().getAdministratorEmailAddress();
		EmailAddress to = user.getEmailAddress();

		String emailSubject = us.cfg().getConfirmAccountEmailSubject();
		String emailBody = us.cfg().getConfirmAccountEmailBody() + "\n\n";
		emailBody += user.getSiteUrlAsString() + "confirm?code=" + user.getConfirmationCode() + "\n\n"; // @TODO Application
		emailBody += us.cfg().getGeneralEmailRegards() + "\n\n----\n";
		emailBody += us.cfg().getGeneralEmailFooter() + "\n\n";

		EmailService emailService = EmailServiceManager.getDefaultService();
		emailService.sendEmailIgnoreException(from, to, us.cfg().getAuditEmailAddress(), emailSubject, emailBody);
	}
	
	/**
	 * 
	 */
	public void saveUser(User user) {
		try {
			PreparedStatement stmt = getUpdatingStatement("SELECT * FROM users WHERE id = ?");
			updateObject(user, stmt);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}
	
	/**
	 * 
	 */
	public void removeUser(User user) {
		saveUser(user);
		users.remove(user.getNameAsTag());
	}
	
	/**
	 * 
	 */
	public void saveUsers() {
		try {
			PreparedStatement stmt = getUpdatingStatement("SELECT * FROM users WHERE id = ?");
			updateObjects(users.values(), stmt);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}
	
	/**
	 * 
	 */
	public User getUserByEmailAddress(String emailAddress) {
		return getUserByEmailAddress(EmailAddress.getFromString(emailAddress));
	}

	/**
	 * 
	 */
	public User getUserByEmailAddress(EmailAddress emailAddress) {
		User result = null;
		try {
			PreparedStatement stmt = getReadingStatement("SELECT * FROM users WHERE email_address = ?");
			result = (User) readObject(stmt, emailAddress.asString());
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
		
		if (result != null) {
			User current = doGetUserByTag(result.getNameAsTag());
			if (current == null) {
				doAddUser(result);
			} else {
				result = current;
			}
		}

		return result;
	}
	
	/**
	 * 
	 * @methodtype assertion
	 */
	protected void assertIsUnknownUserAsIllegalArgument(User user) {
		if (hasUserByTag(user.getNameAsTag())) {
			throw new IllegalArgumentException(user.getName() + "is already known");
		}
	}
	
	/**
	 * 
	 * @methodtype assertion
	 */
	protected void assertIsUnknownUserAsIllegalState(User user) {
		if (hasUserByTag(user.getNameAsTag())) {
			throw new IllegalStateException(user.getName() + "should not be known");
		}
	}
	
}
