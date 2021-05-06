package org.wahlzeit_revisited.repository;

import jakarta.inject.Inject;
import org.wahlzeit_revisited.model.Photo;
import org.wahlzeit_revisited.model.PhotoFactory;
import org.wahlzeit_revisited.model.PhotoFilter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
 * Repository to query Photo entities
 */
public class PhotoRepository extends AbstractRepository<Photo> {

    @Inject
    public PhotoFactory factory;

    /**
     * Generates a SQL query dynamically by using the filter
     * Initially the query string is generated
     * Finally the filter values are applied to the prepared statement
     *
     * If no filter is provided all Photos are returned
     * @param filter the query constraints
     * @return Set of all Photos matching filter
     * @throws SQLException invalid generated sql
     */
    public List<Photo> findWithFilter(PhotoFilter filter) throws SQLException {
        assertIsNonNullArgument(filter);
        if (!filter.hasFilters()) {
            return findAll();
        }

        Optional<Long> userIdOpt = filter.getUserId();
        Set<String> tags = filter.getTags();
        StringBuilder queryBuffer = new StringBuilder(String.format("SELECT * FROM %s WHERE ", getTableName()));

        // add user search query
        if (userIdOpt.isPresent()) {
            queryBuffer.append(" owner_id = ? ");
            if (filter.filtersByTags()) {
                queryBuffer.append(" AND ");
            }
        }

        // add tag search query
        if (filter.filtersByTags()) {
            for (int i = 0; i < tags.size() - 1; i++) {
                queryBuffer.append("tags LIKE ?");
                queryBuffer.append(" AND ");
            }
            queryBuffer.append("tags LIKE ?");
        }

        // Insert query values to prepared statement
        try (PreparedStatement stmt = getReadingStatement(queryBuffer.toString())) {
            int i = 1;
            // Insert user query values
            if (userIdOpt.isPresent()) {
                stmt.setLong(i++, userIdOpt.get());
            }
            // Insert tag query values
            for (String tag : tags) {
                stmt.setString(i++, "%" + tag + ",%");
            }

            List<Photo> result = executeStatement(stmt);
            return result;
        }
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

    /**
     * Iterates through the rows of the statement
     * Creates a new Photo for every row
     * @param stmt statement to execute
     * @return parsed Photo
     * @throws SQLException invalid stmt
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
