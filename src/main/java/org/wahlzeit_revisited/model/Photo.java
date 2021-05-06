package org.wahlzeit_revisited.model;

import org.wahlzeit_revisited.repository.Persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * A photo represents a user-provided (uploaded) photo.
 */
public class Photo implements Persistent {

    private Long id;

    /*
     * coupled classes
     */

    protected PhotoStatus status;
    protected Tags tags;
    protected Long ownerId;

    /*
     * members
     */

    protected int width;
    protected int height;
    protected PhotoSize maxPhotoSize = PhotoSize.MEDIUM; // derived
    protected long creationTime = System.currentTimeMillis();
    protected byte[] data;

    protected int praiseSum = 10;
    protected int noVotes = 1;

    /*
     * constructor
     */

    public Photo(ResultSet resultSet) throws SQLException {
        readFrom(resultSet);
    }

    Photo(PhotoStatus status, byte[] data, Tags tags, int with, int height) {
        this.data = data;
        this.status = status;
        this.tags = tags;
        this.width = with;
        this.height = height;
    }

    Photo(long ownerId, PhotoStatus status, byte[] data, Tags tags, int with, int height) {
        this.data = data;
        this.ownerId = ownerId;
        this.status = status;
        this.tags = tags;
        this.width = with;
        this.height = height;
    }

    /*
     * Persistent contract
     */

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        id = rset.getLong("id");
        ownerId = rset.getLong("owner_id");
        data = rset.getBytes("data");
        tags = new Tags(rset.getString("tags"));
        width = rset.getInt("width");
        height = rset.getInt("height");
        status = PhotoStatus.getFromInt(rset.getInt("status"));
        creationTime = rset.getLong("creation_time");
        maxPhotoSize = PhotoSize.getFromWidthHeight(width, height);
        praiseSum = rset.getInt("praise_sum");
        noVotes = rset.getInt("no_votes");
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateLong("id", id);
        if (ownerId != null) {
            rset.updateLong("owner_id", ownerId);
        }
        rset.updateBytes("data", data);
        rset.updateString("tags", tags.asString());
        rset.updateInt("width", width);
        rset.updateInt("height", height);
        rset.updateInt("status", status.asInt());
        rset.updateLong("creation_time", creationTime);
        rset.updateInt("praise_sum", praiseSum);
        rset.updateInt("no_votes", noVotes);
    }

    public void addToPraise(long ranking) {
        praiseSum += ranking;
        noVotes += 1;
    }

    /*
     * getter/setter
     */

    /**
     * @methodtype get
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     * @methodtype get
     */
    public Set<String> getTags() {
        return tags.getTags();
    }

    /**
     * @methodtype get
     */
    public double getPraise() {
        return (double) praiseSum / noVotes;
    }

    /**
     * @methodtype get
     */
    public int getWidth() {
        return width;
    }

    /**
     * @methodtype get
     */
    public int getHeight() {
        return height;
    }

    /**
     * @methodtype get
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @methodtype boolean-query
     */
    public boolean isVisible() {
        return status.isDisplayable();
    }

    /**
     * @methodtype get
     */
    public PhotoStatus getStatus() {
        return status;
    }

    /**
     * @methodtype set
     */
    public void setStatus(PhotoStatus newStatus) {
        status = newStatus;
    }

    /**
     * @methodtype get
     */
    public long getCreationTime() {
        return creationTime;
    }

    public boolean hasSameOwner(Photo photo) {
        return ownerId.equals(photo.ownerId);
    }

}
