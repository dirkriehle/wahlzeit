package org.wahlzeit.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.wahlzeit.services.DataObject;

/**
 * Class that combines all global wahlzeit variables that need to be stored
 * to the datastore when the system is restarted.
 *
 * Created by Lukas Hahmann on 01.04.15.
 */
@Entity
public class Globals extends DataObject {

    public static final String ID = "id";
    public static final Long DEAULT_ID = 1L;

    @Id private Long id;

    private int lastPhotoId;
    private Long lastUserId;
    private int lastSessionId;
    private int lastCaseId;

    public Globals() {
        id = DEAULT_ID;
        incWriteCount();
    }

    public Long getLastUserId() {
        return lastUserId;
    }

    public int getLastPhotoId() {
        return lastPhotoId;
    }

    public int getLastCaseId() {
        return lastCaseId;
    }

    public int getLastSessionId() {
        return lastSessionId;
    }

    public void setLastPhotoId(int lastPhotoId) {
        this.lastPhotoId = lastPhotoId;
        incWriteCount();
    }

    public void setLastUserId(Long lastUserId) {
        this.lastUserId = lastUserId;
        incWriteCount();
    }

    public void setLastCaseId(int lastCaseId) {
        this.lastCaseId = lastCaseId;
        incWriteCount();
    }

    public void setLastSessionId(int lastSessionId) {
        this.lastSessionId = lastSessionId;
        incWriteCount();
    }

    public String asString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Globals with ID ").append(id);
        builder.append(" and the following parameters: ");
        builder.append("last user ID: ").append(lastUserId);
        builder.append(", last case ID: ").append(lastCaseId);
        builder.append(", last photo ID: ").append(lastPhotoId);
        builder.append(", and last session ID: ").append(lastSessionId);
        return builder.toString();
    }
}
