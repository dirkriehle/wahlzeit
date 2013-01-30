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
import java.lang.reflect.Field;
import java.net.*;
import java.sql.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;
import org.wahlzeit.model.SqlAnnotation;

/**
 * A User is a client that is logged-in, that is, has registered with the system.
 * A user has a fair amount of information associated with it, most notably his/her photos.
 * Also, his/her contact information and whether the account has been confirmed.
 * Users can have a home page which may be elsewhere on the net.
 * 
 * @author dirkriehle
 *
 */
public class User extends ClientRole implements Persistent {
	
	/**
	 * 
	 */
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String PASSWORD = "password";
	public static final String PASSWORD_AGAIN = "passwordAgain";
	public static final String EMAIL_ADDRESS = "emailAddress";
	public static final String TERMS = "termsAndConditions";

	/**
	 * 
	 */
	public static final String STATUS = "status";
	public static final String RIGHTS = "rights";
	public static final String GENDER = "gender";
	public static final String LANGUAGE = "language";
	public static final String NOTIFY_ABOUT_PRAISE = "notifyAboutPraise";
	public static final String HOME_PAGE = "homePage";
	public static final String MEMBER_SINCE = "memberSince";
	public static final String NO_PHOTOS = "noPhotos";
		
	/**
	 * 0 is never returned, first value is 1
	 */
	protected static int lastUserId = 0;
	
	/**
	 * 
	 */
	public static int getLastUserId() {
		return lastUserId;
	}
	
	/**
	 * 
	 */
	public static synchronized void setLastUserId(int newId) {
		lastUserId = newId;
	}
	
	/**
	 * 
	 */
	public static synchronized int getNextUserId() {
		return ++lastUserId;
	}

	/**
	 * 
	 */
	protected transient int writeCount = 0;
	
	/**
	 * 
	 */
	@SqlAnnotation(value="direct")
	protected int id;
	
	/**
	 * 
	 */
	@SqlAnnotation(value="direct")
	protected String name;
	
	/**
	 * 
	 */
	@SqlAnnotation(value="direct")
	protected String nameAsTag;
	
	/**
	 * 
	 */
	@SqlAnnotation(value="direct")
	protected String password;
	
	/**
	 * "rights" needs special treatment
	 */
	
	/**
	 * needs special treatment
	 */
	@SqlAnnotation(value="indirect")
	protected Language language = Language.ENGLISH;
	
	/**
	 * 
	 */
	@SqlAnnotation(value="direct")
	protected boolean notifyAboutPraise = true;
	
	/**
	 * needs special treatment
	 */
	@SqlAnnotation(value="indirect")
	protected URL homePage = StringUtil.asUrl(SysConfig.getSiteUrlAsString());
	
	/**
	 * needs special treatment
	 */
	@SqlAnnotation(value="indirect")
	protected Gender gender = Gender.UNDEFINED;
	
	/**
	 * needs special treatment
	 */
	@SqlAnnotation(value="indirect")
	protected UserStatus status = UserStatus.CREATED;
	
	/**
	 * 
	 */
	@SqlAnnotation(value="direct")
	protected long confirmationCode = 0; // 0 means doesn't need confirmation

	/**
	 * needs special treatment
	 */
	@SqlAnnotation(value="indirect")
	protected Photo userPhoto = null;
	
	protected Set<Photo> photos = new HashSet<Photo>();
	
	/**
	 *
	 */
	@SqlAnnotation(value="direct")
	protected long creationTime = System.currentTimeMillis();
	
	/**
	 * 
	 */
	public void initialize(String myName, String myPassword, String myEmailAddress, long vc){
		initialize(AccessRights.USER, EmailAddress.getFromString(myEmailAddress), myName, myPassword, vc);
	}
		
	/**
	 * 
	 */
	protected User(ClientCore core) {
		super(core);
		core.setRights(AccessRights.USER);
	}

	/**
	 * @methodtype initialization
	 */
	protected void initialize(AccessRights r, EmailAddress ea, String n, String p, long vc) {
		super.initialize(r, ea);
		
		id = getNextUserId();
		
		name = n;
		nameAsTag = Tags.asTag(name);
		
		password = p;
		confirmationCode = vc;
		
		homePage = getDefaultHomePage();

		incWriteCount();
	}
	
	/**
	 * @methodtype get
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 */
	public boolean isDirty() {
		return writeCount != 0;
	}
	
	/**
	 * 
	 */
	public final void incWriteCount() {
		writeCount++;
	}
	
	/**
	 * 
	 */
	public void resetWriteCount() {
		writeCount = 0;
	}
	
	/**
	 * 
	 */
	public String getIdAsString() {
		return String.valueOf(id);
	}
	
	/**
	 * 
	 * @methodtype command
	 */
	public void readFrom(ResultSet rset) throws SQLException {
		//id = rset.getInt("id");
		//name = rset.getString("name");
		//nameAsTag = rset.getString("name_as_tag");
		//password = rset.getString("password");
		//notifyAboutPraise = rset.getBoolean("notify_about_praise");
		//confirmationCode = rset.getLong("confirmation_code");
		//creationTime = rset.getLong("creation_time");
		PersistentWriter.readResultSet(this, rset);
		
		// unable to handle the following attributes in a *generic way*:
		homePage = StringUtil.asUrlOrDefault(rset.getString("home_page"), getDefaultHomePage());
		gender = Gender.getFromInt(rset.getInt("gender"));
		status = UserStatus.getFromInt(rset.getInt("status"));
		language = Language.getFromInt(rset.getInt("language"));
		
		AccessRights rights = AccessRights.getFromInt(rset.getInt("rights"));
		setRights(rights);
		EmailAddress emailAddress = EmailAddress.getFromString(rset.getString("email_address"));
		setEmailAddress(emailAddress);		
		
		photos = PhotoManager.getInstance().findPhotosByOwner(name);
		userPhoto = PhotoManager.getPhoto(PhotoId.getId(rset.getInt("photo")));
	}
	
	/**
	 * 
	 */
	public void writeOn(ResultSet rset) throws SQLException {
		//rset.updateInt("id", id);
		//rset.updateString("name", name);
		//rset.updateString("name_as_tag", nameAsTag);
		//rset.updateString("password", password);
		//rset.updateBoolean("notify_about_praise", notifyAboutPraise);
		//rset.updateLong("confirmation_code", confirmationCode);
		//rset.updateLong("creation_time", creationTime);
		PersistentWriter.writeResultSet(this, rset);
		
		// non-generic attributes:
		rset.updateString("home_page", homePage.toString());
		rset.updateInt("gender", gender.asInt());
		rset.updateInt("status", status.asInt());
		rset.updateInt("language", language.asInt());
		
		rset.updateInt("rights", getRights().asInt());
		rset.updateString("email_address", (getEmailAddress() == null) ? "" : getEmailAddress().asString());
		
		rset.updateInt("photo", (userPhoto == null) ? 0 : userPhoto.getId().asInt());
	}

	/**
	 * 
	 */
	public void writeId(PreparedStatement stmt, int pos) throws SQLException {
		stmt.setInt(pos, id);
	}
	
	/**
	 * 
	 */
	public void setEmailAddress(EmailAddress myEmailAddress) {
		super.setEmailAddress(myEmailAddress);
		incWriteCount();
		
		for (Iterator<Photo> i = photos.iterator(); i.hasNext(); ) {
			Photo photo = i.next();
			photo.setOwnerEmailAddress(getEmailAddress());
		}
	}
	
	/**
	 * 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 */
	public String getNameAsTag() {
		return nameAsTag;
	}

	/**
	 * 
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * 
	 */
	public void setPassword(String newPassword) {
		password = newPassword;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public boolean hasPassword(String otherPassword) {
		return password.equals(otherPassword);
	}
	
	/**
	 * 
	 */
	public Language getLanguage() {
		return language;
	}
	
	/**
	 * 
	 */
	public void setLanguage(Language newLanguage) {
		language = newLanguage;
		incWriteCount();
		
		for (Iterator<Photo> i = photos.iterator(); i.hasNext(); ) {
			Photo photo = i.next();
			photo.setOwnerLanguage(language);
		}
	}
	
	/**
	 * 
	 */
	public long getConfirmationCode() {
		return confirmationCode;
	}
	
	/**
	 * 
	 */
	public boolean needsConfirmation() {
		return confirmationCode != 0;
	}
	
	/**
	 * 
	 */
	public boolean getNotifyAboutPraise() {
		return notifyAboutPraise;
	}
	
	/**
	 * 
	 */
	public void setNotifyAboutPraise(boolean notify) {
		notifyAboutPraise = notify;
		incWriteCount();

		for (Iterator<Photo> i = photos.iterator(); i.hasNext(); ) {
			Photo photo = i.next();
			photo.setOwnerNotifyAboutPraise(notifyAboutPraise);
		}
	}
	
	/**
	 * 
	 */
	public URL getHomePage() {
		return homePage;
	}
	
	/**
	 * 
	 */
	public void setHomePage(URL newHomePage) {
		homePage = newHomePage;
		incWriteCount();
		
		for (Iterator<Photo> i = photos.iterator(); i.hasNext(); ) {
			Photo photo = i.next();
			photo.setOwnerHomePage(homePage);
		}
	}
	
	/**
	 * 
	 */
	public URL getDefaultHomePage() {
		return StringUtil.asUrl(SysConfig.getSiteUrlAsString() + "filter?userName=" + name);
	}
	
	/**
	 * 
	 */
	public Gender getGender() {
		return gender;
	}
	
	/**
	 * 
	 */
	public void setGender(Gender newGender) {
		gender = newGender;
		incWriteCount();
	}

	/**
	 * 
	 */
	public UserStatus getStatus() {
		return status;
	}
	
	/**
	 * 
	 * @methodtype set
	 */
	public void setStatus(UserStatus newStatus) {
		status = newStatus;
		incWriteCount();
	}

	/**
	 * 
	 */
	public boolean isConfirmed() {
		return getStatus().isConfirmed();
	}
	
	/**
	 * 
	 */
	public void setConfirmed() {
		setStatus(status.asConfirmed());
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public boolean hasUserPhoto() {
		return userPhoto != null;
	}
	
	/**
	 * 
	 */
	public Photo getUserPhoto() {
		return userPhoto;
	}
			
	/**
	 * 
	 */
	public void setUserPhoto(Photo newPhoto) {
		userPhoto = newPhoto;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public long getCreationTime() {
		return creationTime;
	}
	
	/**
	 * 
	 */
	public void addPhoto(Photo newPhoto) {
		photos.add(newPhoto);
		incWriteCount();

		newPhoto.setOwnerId(id);
		newPhoto.setOwnerName(name);
		newPhoto.setOwnerNotifyAboutPraise(notifyAboutPraise);
		newPhoto.setOwnerEmailAddress(getEmailAddress());
		newPhoto.setOwnerLanguage(language);
		newPhoto.setOwnerHomePage(homePage);
	}
	
	/**
	 * 
	 */
	public void removePhoto(Photo notMyPhoto) {
		photos.remove(notMyPhoto);
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public int getNoPhotos() {
		return photos.size();
	}
	
	/**
	 * 
	 */
	public Photo[] getPhotos() {
		return getPhotosReverseOrderedByPraise();
	}
	
	/**
	 * 
	 */
	public Photo[] getPhotosReverseOrderedByPraise() {
		Photo[] result = photos.toArray(new Photo[0]);
		Arrays.sort(result, getPhotoByPraiseReverseComparator());
		return result;
	}
	
	/**
	 * 
	 */
	public static Comparator<Photo> getPhotoByPraiseReverseComparator() {
		return new Comparator<Photo>() {
			public int compare(Photo p1, Photo p2) {
				double sc1 = p1.getPraise();
				double sc2 = p2.getPraise();
				if (sc1 == sc2) {
					String id1 = p1.getId().asString();
					String id2 = p2.getId().asString();
					return id1.compareTo(id2);
				} else if (sc1 < sc2) {
					return 1;
				} else {
					return -1;
				}
			}
		};
	}

	
	
	/**
	 * 
	 */
	public boolean setAttributeValue(String attributeName, Object value) {
		Field f = null;
		try {
			// f = this.getClass().getField(attributeName);
			f = this.getClass().getDeclaredField(attributeName);
			f.setAccessible(true); // required if field is not normally accessible
		} catch (SecurityException e1) {
			SysLog.logError(e1.getMessage());
			return false;
		} catch (NoSuchFieldException e1) {
			SysLog.logError(e1.getMessage());
			return false;
		}

		try {
			f.set(this, value);
		} catch (Exception e) {
			SysLog.logError(e.getMessage());
			return false;
		}

		return true;
	}
	
	/**
	 * 
	 */
	public Object getAttributeValue(String attributeName){
		Field f = null;
		Object res = null;
		try {
			f = this.getClass().getDeclaredField(attributeName);
			f.setAccessible(true);
		} catch (Exception e) {
			SysLog.logError(e.getMessage());
			return null;
		}
		
		try {
			res = f.get(this);
		} catch (Exception e) {
			SysLog.logError(e.getMessage());
			return null;
		}		
		return res;
	}
	
}
