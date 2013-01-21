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

import org.wahlzeit.services.PersistentWriter;

/**
 * A photo case is a case where someone flagged a photo as inappropriate.
 * 
 * @author dirkriehle
 *
 */
public class PhotoCase extends Case {
	
	/**
	 * 
	 */
	public static final String FLAGGER = "flagger";
	public static final String REASON = "reason";
	public static final String EXPLANATION = "explanation";
	public static final String CREATED_ON = "createdOn";
	public static final String WAS_DECIDED = "wasDecided";
	public static final String DECIDED_ON = "decidedOn";

	/**
	 * 
	 */
	protected CaseId id = CaseId.NULL_ID; // case id
	protected int applicationId = 0; // application id (unused on Java level)
	protected Photo photo = null; // photo id -> photo
	
	@SqlAnnotation(value="direct")
	protected String flagger = "unknown";
	
	protected FlagReason reason = FlagReason.OTHER;
	
	@SqlAnnotation(value="direct")
	protected String explanation = "none";
	
	@SqlAnnotation(value="direct")
	protected long creationTime = System.currentTimeMillis();
	
	@SqlAnnotation(value="direct")
	protected boolean wasDecided = false;
	
	@SqlAnnotation(value="direct")
	protected long decisionTime = 0;
	
	/**
	 * 
	 */
	public PhotoCase(Photo myPhoto) {
		id = getNextCaseId();
		photo = myPhoto;
		
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public PhotoCase(ResultSet rset) throws SQLException {
		readFrom(rset);
	}
	
	/**
	 * 
	 */
	public String getIdAsString() {
		return String.valueOf(id);
	}
	
	/**
	 * 
	 */
	public void readFrom(ResultSet rset) throws SQLException {
		//wasDecided = rset.getBoolean("was_decided");
		//decisionTime = rset.getLong("decision_time");
		//creationTime = rset.getLong("creation_time");
		//explanation = rset.getString("explanation");
		//flagger = rset.getString("flagger");
		PersistentWriter.readResultSet(this, rset);
		
		// non-generic attributes:
		id = new CaseId(rset.getInt("id"));
		photo = PhotoManager.getPhoto(PhotoId.getId(rset.getInt("photo")));
		reason = FlagReason.getFromInt(rset.getInt("reason"));
	}
	
	/**
	 * 
	 */
	public void writeOn(ResultSet rset) throws SQLException {
		//rset.updateBoolean("was_decided", wasDecided);
		//rset.updateLong("decision_time", decisionTime);
		//rset.updateLong("creation_time", creationTime);
		//rset.updateString("explanation", explanation);
		//rset.updateString("flagger", flagger);
		PersistentWriter.writeResultSet(this, rset);
		
		// non-generic attributes:
		rset.updateInt("id", id.asInt());
		rset.updateInt("photo", (photo == null) ? 0 : photo.getId().asInt());
		rset.updateInt("reason", reason.asInt());				
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
	public CaseId getId() {
		return id;
	}
	
	/**
	 * 
	 */
	public Photo getPhoto() {
		return photo;
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
	public String getFlagger() {
		return flagger;
	}
	
	/**
	 * 
=	 */
	public void setFlagger(String newFlagger) {
		flagger = newFlagger;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public FlagReason getReason() {
		return reason;
	}
	
	/**
	 * 
=	 */
	public void setReason(FlagReason newReason) {
		reason = newReason;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public String getExplanation() {
		return explanation;
	}
	
	/**
	 * 
=	 */
	public void setExplanation(String newExplanation) {
		explanation = newExplanation;
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public boolean wasDecided() {
		return wasDecided;
	}
	
	/**
	 * 
	 */
	public void setDecided() {
		wasDecided = true;
		decisionTime = System.currentTimeMillis();
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public long getDecisionTime() {
		return decisionTime;
	}

	/**
	 * 
	 */
	public String getPhotoOwnerName() {
		return photo.getOwnerName();
	}
	
	/**
	 * 
	 */
	public PhotoStatus getPhotoStatus() {
		return photo.getStatus();
	}
	
}
