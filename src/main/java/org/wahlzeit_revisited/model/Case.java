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

package org.wahlzeit_revisited.model;


import org.wahlzeit_revisited.database.repository.Persistent;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * A photo case is a case where someone flagged a photo as inappropriate.
 */
public class Case implements Persistent {

    /**
     *
     */
    protected Long id;
    protected Long photoId; // photo id -> photo
    protected Long flaggerId;
    protected FlagReason reason;
    protected String explanation = "none";
    protected long createdOn = System.currentTimeMillis();
    protected boolean wasDecided = false;
    protected long decidedOn = 0;

    /**
     *
     */
    public Case(long flaggerId, long photoId, FlagReason flagReason) {
        this.flaggerId = flaggerId;
        this.photoId = photoId;
        this.reason = flagReason;
    }

    /**
     *
     */
    public Case(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    /**
     * Persistent contract
     */

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        id = rset.getLong("id");
        photoId = rset.getLong("photo_id");
        createdOn = rset.getLong("creation_time");

        flaggerId = rset.getLong("flagger_id");
        reason = FlagReason.getFromInt(rset.getInt("reason"));
        explanation = rset.getString("explanation");

        wasDecided = rset.getBoolean("was_decided");
        decidedOn = rset.getLong("decision_time");
    }


    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateLong("id", id);
        rset.updateLong("photo_id", photoId);
        rset.updateLong("creation_time", createdOn);

        rset.updateLong("flagger_id", flaggerId);
        rset.updateInt("reason", reason.asInt());
        rset.updateString("explanation", explanation);

        rset.updateBoolean("was_decided", wasDecided);
        rset.updateLong("decision_time", decidedOn);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @methodtype get
     */
    public Long getPhotoId() {
        return photoId;
    }

    /**
     * @methodtype get
     */
    public long getCreationTime() {
        return createdOn;
    }

    /**
     * @methodtype get
     */
    public Long getFlaggerId() {
        return flaggerId;
    }

    /**
     * @methodtype set
     */
    public void setFlaggerId(long flaggerId) {
        this.flaggerId = flaggerId;
    }

    /**
     * @methodtype get
     */
    public FlagReason getReason() {
        return reason;
    }

    /**
     * @methodtype set
     */
    public void setReason(FlagReason reason) {
        this.reason = reason;
    }

    /**
     * @methodtype get
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * @methodtype set
     */
    public void setExplanation(String newExplanation) {
        explanation = newExplanation;
    }

    /**
     * @methodtype boolean-query
     */
    public boolean wasDecided() {
        return wasDecided;
    }

    /**
     * @methodtype set
     */
    public void setDecided() {
        wasDecided = true;
        decidedOn = System.currentTimeMillis();
    }

    /**
     * @methodtype get
     */
    public long getDecisionTime() {
        return decidedOn;
    }
}
