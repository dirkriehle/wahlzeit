/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 * Copyright (c) 2021 by Aron Metzig
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

package org.wahlzeit_revisited.dto;

import org.wahlzeit_revisited.model.FlagReason;

/**
 * CaseDto represents the Case-entity for the outer world
 */
public class CaseDto {

    protected long id;
    protected long photoId;
    protected long flaggerId;
    protected FlagReason reason;
    protected String explanation;
    protected long createdOn;
    protected boolean wasDecided;
    protected long decidedOn;

    public CaseDto(long id, long photoId, long flaggerId, FlagReason reason, String explanation, long createdOn, boolean wasDecided, long decidedOn) {
        this.id = id;
        this.photoId = photoId;
        this.flaggerId = flaggerId;
        this.reason = reason;
        this.explanation = explanation;
        this.createdOn = createdOn;
        this.wasDecided = wasDecided;
        this.decidedOn = decidedOn;
    }

    /**
     * @methodtype get
     */
    public Long getId() {
        return id;
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
    public Long getFlaggerId() {
        return flaggerId;
    }

    /**
     * @methodtype get
     */
    public FlagReason getReason() {
        return reason;
    }

    /**
     * @methodtype get
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * @methodtype get
     */
    public Long getCreatedOn() {
        return createdOn;
    }

    /**
     * @methodtype boolean-query
     */
    public Boolean isWasDecided() {
        return wasDecided;
    }

    /**
     * @methodtype get
     */
    public Long getDecidedOn() {
        return decidedOn;
    }

    /*
     * Builder
     */

    public static class Builder {
        protected long id;
        protected long photoId;
        protected long flaggerId;
        protected FlagReason reason;
        protected String explanation;
        protected long createdOn;
        protected boolean wasDecided;
        protected long decidedOn;

        public Builder() {
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withPhotoId(long photoId) {
            this.photoId = photoId;
            return this;
        }

        public Builder withFlaggerId(long flaggerId) {
            this.flaggerId = flaggerId;
            return this;
        }

        public Builder withReason(FlagReason reason) {
            this.reason = reason;
            return this;
        }

        public Builder withExplanation(String explanation) {
            this.explanation = explanation;
            return this;
        }

        public Builder withCreatedOn(long createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder withWasDecided(boolean wasDecided) {
            this.wasDecided = wasDecided;
            return this;
        }

        public Builder withDecidedOn(long decidedOn) {
            this.decidedOn = decidedOn;
            return this;
        }

        /**
         * @methodtype transform
         */
        public CaseDto build() {
            return new CaseDto(id, photoId, flaggerId, reason, explanation, createdOn, wasDecided, decidedOn);
        }
    }
}
