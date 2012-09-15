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

import java.sql.*;
import java.net.*;

import org.wahlzeit.services.*;
import org.wahlzeit.utils.*;

/**
 * A photo represents a user-provided (uploaded) photo.
 * 
 * @author dirkriehle
 *
 */
public class Photo extends DataObject {

	/**
	 * 
	 */
	public static final String IMAGE = "image";
	public static final String THUMB = "thumb";
	public static final String LINK = "link";
	public static final String PRAISE = "praise";
	public static final String NO_VOTES = "noVotes";
	public static final String CAPTION = "caption";
	public static final String DESCRIPTION = "description";
	public static final String KEYWORDS = "keywords";

	public static final String TAGS = "tags";

	public static final String STATUS = "status";
	public static final String IS_INVISIBLE = "isInvisible";
	public static final String UPLOADED_ON = "uploadedOn";
	
	/**
	 * 
	 */
	public static final int MAX_PHOTO_WIDTH = 420;
	public static final int MAX_PHOTO_HEIGHT = 600;
	public static final int MAX_THUMB_PHOTO_WIDTH = 105;
	public static final int MAX_THUMB_PHOTO_HEIGHT = 150;
	
	/**
	 * 
	 */
	protected PhotoId id = null;
	
	/**
	 * 
	 */
	protected int ownerId = 0;
	protected String ownerName;

	/**
	 * 
	 */
	protected boolean ownerNotifyAboutPraise = false;
	protected EmailAddress ownerEmailAddress = EmailAddress.NONE;
	protected Language ownerLanguage = Language.ENGLISH;
	protected URL ownerHomePage;
	
	/**
	 * 
	 */
	protected int width;
	protected int height;
	protected PhotoSize maxPhotoSize = PhotoSize.MEDIUM; // derived
	
	/**
	 * 
	 */
	protected Tags tags = Tags.EMPTY_TAGS;

	/**
	 * 
	 */
	protected PhotoStatus status = PhotoStatus.VISIBLE;
	
	/**
	 * 
	 */
	protected int praiseSum = 10;
	protected int noVotes = 1;
	
	/**
	 * 
	 */
	protected long creationTime = System.currentTimeMillis();
	
	/**
	 * 
	 */
	public Photo() {
		id = PhotoId.getNextId();
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public Photo(PhotoId myId) {
		id = myId;
		
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public Photo(ResultSet rset) throws SQLException {
		readFrom(rset);
	}

	/**
	 * 
	 */
	public String getIdAsString() {
		return String.valueOf(id.asInt());
	}
	
	/**
	 * 
	 */
	public void readFrom(ResultSet rset) throws SQLException {
		id = PhotoId.getId(rset.getInt("id"));

		ownerId = rset.getInt("owner_id");
		ownerName = rset.getString("owner_name");
		
		ownerNotifyAboutPraise = rset.getBoolean("owner_notify_about_praise");
		ownerEmailAddress = EmailAddress.getFromString(rset.getString("owner_email_address"));
		ownerLanguage = Language.getFromInt(rset.getInt("owner_language"));
		ownerHomePage = StringUtil.asUrl(rset.getString("owner_home_page"));

		width = rset.getInt("width");
		height = rset.getInt("height");

		tags = new Tags(rset.getString("tags"));

		status = PhotoStatus.getFromInt(rset.getInt("status"));
		praiseSum = rset.getInt("praise_sum");
		noVotes = rset.getInt("no_votes");

		creationTime = rset.getLong("creation_time");

		maxPhotoSize = PhotoSize.getFromWidthHeight(width, height);
	}
	
	/**
	 * 
	 */
	public void writeOn(ResultSet rset) throws SQLException {
		rset.updateInt("id", id.asInt());
		rset.updateInt("owner_id", ownerId);
		rset.updateString("owner_name", ownerName);
		rset.updateBoolean("owner_notify_about_praise", ownerNotifyAboutPraise);
		rset.updateString("owner_email_address", ownerEmailAddress.asString());
		rset.updateInt("owner_language", ownerLanguage.asInt());
		rset.updateString("owner_home_page", ownerHomePage.toString());
		rset.updateInt("width", width);
		rset.updateInt("height", height);
		rset.updateString("tags", tags.asString());
		rset.updateInt("status", status.asInt());
		rset.updateInt("praise_sum", praiseSum);
		rset.updateInt("no_votes", noVotes);
		rset.updateLong("creation_time", creationTime);		
	}

	/**
	 * 
	 */
	public void writeId(PreparedStatement stmt, int pos) throws SQLException {
		stmt.setInt(pos, id.asInt());
	}
	
	/**
	 * 
	 */
	public PhotoId getId() {
		return id;
	}
	
	/**
	 * 
	 */
	public int getOwnerId() {
		return ownerId;
	}
	
	/**
	 * 
	 */
	public void setOwnerId(int newId) {
		ownerId = newId;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public String getOwnerName() {
		return ownerName;
	}
	
	/**
	 * 
	 */
	public void setOwnerName(String newName) {
		ownerName = newName;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public String getSummary(ModelConfig cfg) {
		return cfg.asPhotoSummary(ownerName);
	}

	/**
	 * 
	 */
	public String getCaption(ModelConfig cfg) {
		return cfg.asPhotoCaption(ownerName, ownerHomePage);
	}

	/**
	 * 
	 */
	public boolean getOwnerNotifyAboutPraise() {
		return ownerNotifyAboutPraise;
	}
	
	/**
	 * 
	 */
	public void setOwnerNotifyAboutPraise(boolean newNotifyAboutPraise) {
		ownerNotifyAboutPraise = newNotifyAboutPraise;
		incWriteCount();
	}

	/**
	 * 
	 */
	public EmailAddress getOwnerEmailAddress() {
		return ownerEmailAddress;
	}
	
	/**
	 * 
	 */
	public void setOwnerEmailAddress(EmailAddress newEmailAddress) {
		ownerEmailAddress = newEmailAddress;
		incWriteCount();
	}

	/**
	 * 
	 */
	public Language getOwnerLanguage() {
		return ownerLanguage;
	}
	
	/**
	 * 
	 */
	public void setOwnerLanguage(Language newLanguage) {
		ownerLanguage = newLanguage;
		incWriteCount();
	}

	/**
	 * 
	 */
	public URL getOwnerHomePage() {
		return ownerHomePage;
	}
	
	/**
	 * 
	 */
	public void setOwnerHomePage(URL newHomePage) {
		ownerHomePage = newHomePage;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public boolean hasSameOwner(Photo photo) {
		return photo.getOwnerEmailAddress().equals(ownerEmailAddress);
	}

	/**
	 * 
	 */
	public boolean isWiderThanHigher() {
		return (height * MAX_PHOTO_WIDTH) < (width * MAX_PHOTO_HEIGHT);
	}
	
	/**
	 * 
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 
	 */
	public int getThumbWidth() {
		return isWiderThanHigher() ? MAX_THUMB_PHOTO_WIDTH : (width * MAX_THUMB_PHOTO_HEIGHT / height);
	}
	
	/**
	 * 
	 */
	public int getThumbHeight() {
		return isWiderThanHigher() ? (height * MAX_THUMB_PHOTO_WIDTH / width) : MAX_THUMB_PHOTO_HEIGHT;
	}
	
	/**
	 * 
	 */
	public void setWidthAndHeight(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;

		maxPhotoSize = PhotoSize.getFromWidthHeight(width, height);

		incWriteCount();
	}
	
	/**
	 * 
	 */
	public boolean hasPhotoSize(PhotoSize size) {
		return maxPhotoSize.asInt() >= size.asInt();
	}
	
	/**
	 * 
	 */
	public PhotoSize getMaxPhotoSize() {
		return maxPhotoSize;
	}
	
	/**
	 * 
	 */
	public double getPraise() {
		return (double) praiseSum / noVotes;
	}
	
	/**
	 * 
	 */
	public String getPraiseAsString(ModelConfig cfg) {
		return cfg.getPraise(getPraise());
	}
	
	/**
	 * 
	 */
	public void addToPraise(int value) {
		praiseSum += value;
		noVotes += 1;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public boolean isVisible() {
		return status.isDisplayable();
	}
	
	/**
	 * 
	 */
	public PhotoStatus getStatus() {
		return status;
	}
	
	/**
	 * 
	 */
	public void setStatus(PhotoStatus newStatus) {
		status = newStatus;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public boolean hasTag(String tag) {
		return tags.hasTag(tag);
	}
	
	/**
	 * 
	 */
	public Tags getTags() {
		return tags;
	}

	/**
	 * 
	 */
	public void setTags(Tags newTags) {
		tags = newTags;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public long getCreationTime() {
		return creationTime;
	}
	
}
