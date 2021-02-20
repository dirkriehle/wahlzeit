package org.wahlzeit_revisited.repository;

import jakarta.inject.Inject;
import org.wahlzeit_revisited.model.Photo;
import org.wahlzeit_revisited.model.PhotoFactory;
import org.wahlzeit_revisited.model.PhotoStatus;
import org.wahlzeit_revisited.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhotoRepository extends AbstractRepository<Photo> {

    @Inject
    public PhotoFactory factory;

    /*
     * business methods
     */

    public List<Photo> findForUser(User user) throws SQLException {
        assertPersistedObject(user);
        return findForUser(user.getId());
    }

    public List<Photo> findForUser(Long userId) throws SQLException {
        assertIsNonNullArgument(userId, "User");

        String query = "SELECT * FROM photos WHERE owner_id = ?";
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setLong(1, userId);

        List<Photo> result = new ArrayList<>();
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                Photo photo = parseAsteriskRow(resultSet);
                result.add(photo);
            }
        }

        return result;
    }

    /*
     * AbstractRepository contract
     */

    @Override
    protected Optional<Photo> doFindById(Long id) throws SQLException {
        String query = "SELECT * FROM photos WHERE id = ?";
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setLong(1, id);

        Photo photo = null;
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                photo = parseAsteriskRow(resultSet);
            }
        }

        return Optional.ofNullable(photo);
    }

    @Override
    protected List<Photo> doFindAll() throws SQLException {
        String query = "SELECT * FROM photos";
        PreparedStatement stmt = getReadingStatement(query);

        List<Photo> result = new ArrayList<>();
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                Photo photo = parseAsteriskRow(resultSet);
                result.add(photo);
            }
        }

        return result;
    }

    @Override
    protected Photo doInsert(Photo toInsert) throws SQLException {
        String query = "INSERT INTO photos (creation_time, owner_id, status, width, height) VALUES(?, ?, ?, ?, ?) RETURNING id";
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setLong(1, toInsert.getCreationTime());
        stmt.setLong(2, toInsert.getOwnerId() == null ? 0 : toInsert.getOwnerId()); // admin is owner
        stmt.setInt(3, toInsert.getStatus().asInt());
        stmt.setInt(4, toInsert.getWidth());
        stmt.setInt(5, toInsert.getHeight());

        // Extract id from RETURNING statement
        try (ResultSet returningSet = stmt.executeQuery()) {
            returningSet.next();
            Long persistedId = returningSet.getLong(1);
            toInsert.setId(persistedId);
        }

        return toInsert;
    }

    @Override
    protected Photo doUpdate(Photo toUpdate) throws SQLException {
        String query = "UPDATE photos SET owner_id = ?, status = ?, width = ?, height = ? WHERE id = ?";
        PreparedStatement stmt = getUpdatingStatement(query);
        stmt.setLong(1, toUpdate.getOwnerId() == null ? 0 : toUpdate.getOwnerId()); // admin is owner
        stmt.setInt(2, toUpdate.getStatus().asInt());
        stmt.setInt(3, toUpdate.getWidth());
        stmt.setInt(4, toUpdate.getHeight());
        stmt.setLong(5, toUpdate.getId());

        stmt.execute();
        return toUpdate;
    }

    @Override
    protected Photo doDelete(Photo toDelete) throws SQLException {
        String query = "DELETE FROM photos WHERE id = ?";
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setLong(1, toDelete.getId());
        stmt.execute();

        return toDelete;
    }

    /*
     * helpers methods
     */

    protected Photo parseAsteriskRow(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(1);
        long created = resultSet.getLong(2);
        long ownerId = resultSet.getLong(3);
        PhotoStatus status = PhotoStatus.getFromInt(resultSet.getInt(4));
        int width = resultSet.getInt(5);
        int height = resultSet.getInt(6);

        return factory.createPhoto(id, created, ownerId, status, width, height);
    }
}
