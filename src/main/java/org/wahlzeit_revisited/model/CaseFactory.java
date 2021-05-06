package org.wahlzeit_revisited.model;

import org.wahlzeit_revisited.repository.PersistentFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * CaseFactory creates a new Case, or instantiates a entity from a resultSet
 */
public class CaseFactory implements PersistentFactory<Case> {

    @Override
    public Case createPersistent(ResultSet resultSet) throws SQLException {
        return new Case(resultSet);
    }

    public Case createPhotoCase(User flagger, Photo photo, FlagReason flagReason) {
        long flaggerId = flagger.getId();
        long photoId = photo.getId();
        return new Case(flaggerId, photoId, flagReason);
    }

}
