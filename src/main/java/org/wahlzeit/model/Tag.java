package org.wahlzeit.model;

import com.google.appengine.api.datastore.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import org.wahlzeit.services.DataObject;
import org.wahlzeit.services.ObjectManager;

/**
 * Created by Lukas Hahmann on 01.04.15.
 */
@Entity
public class Tag extends DataObject {

    public static final String TEXT = "text";
    public static final String PHOTO_ID = "photoId";

    @Id
    private Long id;
    @Index
    private String text;
    @Index
    private String photoId;
    @Parent
    Key parent = ObjectManager.applicationRootKey;

    public Tag() {
        // do nothing, necessary for Google Datastore
    }

    public Tag(String text, String photoId) {
        this.text = text;
        this.photoId = photoId;
        incWriteCount();
    }

    public String getText() {
        return text;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String asString() {
        return "PhotoId: " + photoId + ", Tag: " + text;
    }
}
