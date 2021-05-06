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


import org.wahlzeit_revisited.repository.Persistent;

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
     *
     */
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

    /**
     *
     */
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

    /**
     *
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     *
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     */
    public Long getPhotoId() {
        return photoId;
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
    public Long getFlaggerId() {
        return flaggerId;
    }

    /**
     * =
     */
    public void setFlaggerId(long flaggerId) {
        this.flaggerId = flaggerId;
    }

    /**
     *
     */
    public FlagReason getReason() {
        return reason;
    }

    /**
     * =
     */
    public void setReason(FlagReason reason) {
        this.reason = reason;
    }

    /**
     *
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * =
     */
    public void setExplanation(String newExplanation) {
        explanation = newExplanation;
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
    }

    /**
     *
     */
    public long getDecisionTime() {
        return decidedOn;
    }
}
