package org.wahlzeit_revisited.dto;

import org.wahlzeit_revisited.model.FlagReason;

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

    public Long getId() {
        return id;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public Long getFlaggerId() {
        return flaggerId;
    }

    public FlagReason getReason() {
        return reason;
    }

    public String getExplanation() {
        return explanation;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public Boolean isWasDecided() {
        return wasDecided;
    }

    public Long getDecidedOn() {
        return decidedOn;
    }


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

        public CaseDto build() {
            return new CaseDto(id, photoId, flaggerId, reason, explanation, createdOn, wasDecided, decidedOn);
        }
    }
}
