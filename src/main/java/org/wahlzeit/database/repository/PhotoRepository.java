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

package org.wahlzeit.database.repository;

import jakarta.inject.Inject;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.PhotoFilter;
import org.wahlzeit.model.PhotoStatus;

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
     * <p>
     * If no filter is provided all Photos are returned
     *
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

    /**
     * Finds a single visible photo by random
     *
     * @return random photo, if exists
     */
    public Photo findRandomVisible() throws SQLException {
        Photo photo = null;
        String query = String.format("SELECT * from %s WHERE status = ? ORDER BY RANDOM() LIMIT 1", getTableName());
        try (PreparedStatement stmt = getReadingStatement(query)) {
            stmt.setInt(1, PhotoStatus.VISIBLE.asInt());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    photo = getFactory().createPersistent(resultSet);
                }
            }
        }
        return photo;
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
     *
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
