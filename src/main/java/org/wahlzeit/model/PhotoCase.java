/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.sql.*;


/**
 * A photo case is a case where someone flagged a photo as inappropriate.
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
	protected String flagger = "unknown";
	protected FlagReason reason = FlagReason.OTHER;
	protected String explanation = "none";	
	protected long createdOn = System.currentTimeMillis();
	protected boolean wasDecided = false;
	protected long decidedOn = 0;
	
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
		id = new CaseId(rset.getInt("id"));
		photo = PhotoManager.getPhoto(PhotoId.getIdFromInt(rset.getInt("photo")));
		createdOn = rset.getLong("creation_time");
		
		flagger = rset.getString("flagger");
		reason = FlagReason.getFromInt(rset.getInt("reason"));
		explanation = rset.getString("explanation");
		
		wasDecided = rset.getBoolean("was_decided");
		decidedOn = rset.getLong("decision_time");
	}
	
	/**
	 * 
	 */
	public void writeOn(ResultSet rset) throws SQLException {
		rset.updateInt("id", id.asInt());
		rset.updateInt("photo", (photo == null) ? 0 : photo.getId().asInt());
		rset.updateLong("creation_time", createdOn);
		
		rset.updateString("flagger", flagger);
		rset.updateInt("reason", reason.asInt());
		rset.updateString("explanation", explanation);
		
		rset.updateBoolean("was_decided", wasDecided);
		rset.updateLong("decision_time", decidedOn);		
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
		return createdOn;
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
		decidedOn = System.currentTimeMillis();
		incWriteCount();
	}
	
	/**
	 * 
	 */
	public long getDecisionTime() {
		return decidedOn;
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
