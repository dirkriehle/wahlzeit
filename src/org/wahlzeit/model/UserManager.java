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
import java.util.concurrent.locks.*;
import java.sql.*;

import org.wahlzeit.model.ControllerException.ControllerErrorCode;
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
			result = Client.createClient(User.class);
			result.readFrom(rset);
		} else if (rights == AccessRights.MODERATOR) {
			result = Client.createClient(Moderator.class);
			result.readFrom(rset);
		} else if (rights == AccessRights.ADMINISTRATOR) {
			result = Client.createClient(Moderator.class);
			result.readFrom(rset);
		} else {
			SysLog.logInfo("received NONE rights value");
		}

		return result;
	}

	
	/**
	 * problem: two users can pass the "users-exists-checks" and enter this function parallel => critical section
	 * solution: executing query creating users must be atomic
	 */
	Lock userAddMutex = new ReentrantLock();
	
	/**
	 * controller/business function (tier 1)
	 */
	public void addUser(User user) throws ControllerException{
		try{
			assertIsNonNullArgument(user);
			assertIsUnknownUserAsIllegalArgument(user);
		}catch(IllegalArgumentException iaex){
			throw new ControllerException(ControllerErrorCode.USER_ALREADY_EXISTS);
		}

		try {
			int id = user.getId();
			PreparedStatement stmt = null;
			try{
			stmt = getReadingStatement("INSERT INTO users(id) VALUES(?)");
			}catch(SQLException s){
				SysLog.logThrowable(s);
				throw new ControllerException(ControllerErrorCode.UNDEFINED);
			}
			
			userAddMutex.lock();
			
			if (hasUserByName(user.getName())) {
				// fault detected: race-condition!
				userAddMutex.unlock();
				throw new ControllerException(ControllerErrorCode.USER_ALREADY_EXISTS);
			}
			
			createUserObject(user, stmt, id);
			userAddMutex.unlock();
			
		} catch (ReadWriteException rwex) {
			// fault detected!
			// error handling not possible here!
			// failure:
			throw new ControllerException(ControllerErrorCode.UNDEFINED);
			
		}
		
		doAddUser(user);
	}
	
	
	/**
	 * data access function (tier 0)
	 */
	protected void createUserObject(Persistent obj, PreparedStatement stmt, int value) throws ReadWriteException {
		try {
			stmt.setInt(1, value);
		} catch (SQLException e) {
			// fault detected!
			// error handling not possible here!
			// failure:
			SysLog.logThrowable(e);
			throw new ReadWriteException(SQLErrorCodes.invalid_statement);
		}
		
		try{
			stmt.executeUpdate();
			SysLog.logQuery(stmt);
		}catch (SQLException e){
			// fault detected!
			// error handling mechanism: 
			if(!handleExceptionCreatingUserObject(e, stmt)){
				// failure:
				SysLog.logThrowable(e);
				throw new ReadWriteException(SQLErrorCodes.undefined);
			}
		}
	}
	
	/**
	 * returns true if the {@link SQLException} could be handeled
	 */
	private boolean handleExceptionCreatingUserObject(SQLException e, PreparedStatement stmt) {
		if(e.getErrorCode() == SQLErrorCodes.connection_exception.getCode()
				|| e.getErrorCode() == SQLErrorCodes.connection_failure.getCode()){
			// try it again:
			try{
				stmt.executeUpdate();
			}catch (SQLException se){
				return false;
			}
			return true;
		}else{
			// cannot handle other error codes:
			return false;
		}
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
					SysLog.logValueWithInfo("user", user.getName(), "user had already been loaded");
				}
			}
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
		
		SysLog.logInfo("loaded all users");
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
	public void emailWelcomeMessage(UserSession ctx, User user) {
		EmailAddress from = ctx.cfg().getAdministratorEmailAddress();
		EmailAddress to = user.getEmailAddress();

		String emailSubject = ctx.cfg().getWelcomeEmailSubject();
		String emailBody = ctx.cfg().getWelcomeEmailBody() + "\n\n";
		emailBody += ctx.cfg().getWelcomeEmailUserName() + user.getName() + "\n\n"; 
		emailBody += ctx.cfg().getConfirmAccountEmailBody() + "\n\n";
		emailBody += SysConfig.getSiteUrlAsString() + "confirm?code=" + user.getConfirmationCode() + "\n\n";
		emailBody += ctx.cfg().getGeneralEmailRegards() + "\n\n----\n";
		emailBody += ctx.cfg().getGeneralEmailFooter() + "\n\n";

		EmailService emailService = EmailServiceManager.getDefaultService();
		emailService.sendEmailIgnoreException(from, to, ctx.cfg().getAuditEmailAddress(), emailSubject, emailBody);
	}
	
	/**
	 * 
	 */
	public void emailConfirmationRequest(UserSession ctx, User user) {
		EmailAddress from = ctx.cfg().getAdministratorEmailAddress();
		EmailAddress to = user.getEmailAddress();

		String emailSubject = ctx.cfg().getConfirmAccountEmailSubject();
		String emailBody = ctx.cfg().getConfirmAccountEmailBody() + "\n\n";
		emailBody += SysConfig.getSiteUrlAsString() + "confirm?code=" + user.getConfirmationCode() + "\n\n";
		emailBody += ctx.cfg().getGeneralEmailRegards() + "\n\n----\n";
		emailBody += ctx.cfg().getGeneralEmailFooter() + "\n\n";

		EmailService emailService = EmailServiceManager.getDefaultService();
		emailService.sendEmailIgnoreException(from, to, ctx.cfg().getAuditEmailAddress(), emailSubject, emailBody);
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
