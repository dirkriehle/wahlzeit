package org.wahlzeit_revisited.repository;

import jakarta.inject.Inject;
import org.wahlzeit_revisited.model.Photo;
import org.wahlzeit_revisited.model.PhotoFactory;
import org.wahlzeit_revisited.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        String query = String.format("SELECT * FROM %s WHERE owner_id = ?", getTableName());
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setLong(1, userId);

        List<Photo> result = executeStatement(stmt);
        return result;
    }

    public List<Photo> findForTags(Set<String> tags) throws SQLException {
        assertIsNonNullArgument(tags);
        assertIsNonEmpty(tags);

        StringBuilder queryBufffer = new StringBuilder(String.format("SELECT * FROM %s WHERE ", getTableName()));
        for (int i = 0; i < tags.size() - 1; i++) {
            queryBufffer.append("tags LIKE ?");
            queryBufffer.append(" AND ");
        }
        queryBufffer.append("tags LIKE ?");


        int i = 1;
        PreparedStatement stmt = getReadingStatement(queryBufffer.toString());
        for (String tag : tags) {
            System.out.println("%" + tag + ",%");
            stmt.setString(i++, "%" + tag + ",%");
        }

        List<Photo> result = executeStatement(stmt);
        return result;
    }

    /*
     * AbstractRepository contract
     */

    @Override
    protected String getTableName() {
        return "photos";
    }

    @Override
    protected PersistentFactory<Photo> getFactory() {
        return factory;
    }

    /*
     * Helpers
     */

    private List<Photo> executeStatement(PreparedStatement stmt) throws SQLException {
        List<Photo> result = new ArrayList<>();
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                Photo photo = factory.createPersistent(resultSet);
                result.add(photo);
            }
        }
        return result;
    }
}
