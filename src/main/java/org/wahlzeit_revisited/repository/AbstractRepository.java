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

package org.wahlzeit_revisited.repository;

import org.wahlzeit_revisited.database.DatabaseConnection;
import org.wahlzeit_revisited.database.SessionManager;
import org.wahlzeit_revisited.utils.SysLog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * AbstractRepository fulfills big parts of the Repository contract
 * It states generalized SQL queries
 *
 * @param <T> to represent Persistent
 */
public abstract class AbstractRepository<T extends Persistent> implements Repository<T> {

    /*
     * template methods
     */

    /**
     * Returns the factory for the entity
     *
     * @return entity factory
     */
    protected abstract PersistentFactory<T> getFactory();

    /**
     * Returns the entities table name inside the database, eg 'users
     *
     * @return table Name
     */
    protected abstract String getTableName();

    /*
     * Repository contract
     */

    /**
     * Finds the entity by it's database id
     *
     * @param id entities id
     * @return entity if exists
     * @throws SQLException invalid table name
     */
    @Override
    public Optional<T> findById(Long id) throws SQLException {
        if (id == null || id < -1) {
            return Optional.empty();
        }

        String query = String.format("SELECT * FROM %s WHERE id = ?", getTableName());
        try (PreparedStatement stmt = getReadingStatement(query)) {
            stmt.setLong(1, id);

            T persistent = null;
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    persistent = getFactory().createPersistent(resultSet);
                }
            }

            return Optional.ofNullable(persistent);
        }
    }

    /**
     * Finds all entities of the database
     *
     * @return set of all entities
     * @throws SQLException wrong table name
     */
    @Override
    public List<T> findAll() throws SQLException {
        String query = String.format("SELECT * FROM %s", getTableName());
        try (PreparedStatement stmt = getReadingStatement(query)) {

            List<T> result = new ArrayList<>();
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    T persistent = getFactory().createPersistent(resultSet);
                    result.add(persistent);
                }
            }

            return result;
        }
    }

    /**
     * Inserts a not already persisted entity
     * The entities database id is derived from the query result
     *
     * @param toInsert entity to persist
     * @return the persisted entity
     * @throws SQLException wrong table name
     */
    @Override
    public T insert(T toInsert) throws SQLException {
        assertIsNonNullArgument(toInsert);
        assertNonPersistedObject(toInsert);

        String query = String.format("INSERT INTO %s DEFAULT VALUES RETURNING id", getTableName());
        try (PreparedStatement readStmt = getReadingStatement(query)) {

            // Extract id from RETURNING statement
            long persistedId;
            try (ResultSet returningSet = readStmt.executeQuery()) {
                returningSet.next();
                persistedId = returningSet.getLong(1);
                toInsert.setId(persistedId);
            }

            // Get empty entity row
            query = String.format("SELECT * FROM %s WHERE id = ?", getTableName());
            try (PreparedStatement updateStmt = getUpdatingStatement(query)) {
                updateStmt.setLong(1, persistedId);

                // Write internal structure in resultSet and start transaction
                try (ResultSet returningSet = updateStmt.executeQuery()) {
                    returningSet.next();
                    toInsert.writeOn(returningSet);
                    returningSet.updateRow();
                }
            }
        }
        assertPersistedObject(toInsert);
        return toInsert;
    }

    /**
     * Updates a already persisted entity
     *
     * @param toUpdate to entity
     * @return the updated entity
     * @throws SQLException wrong table name
     */
    @Override
    public T update(T toUpdate) throws SQLException {
        assertIsNonNullArgument(toUpdate);
        assertPersistedObject(toUpdate);

        String query = String.format("SELECT * FROM %s WHERE id = ?", getTableName());
        try (PreparedStatement stmt = getUpdatingStatement(query)) {
            stmt.setLong(1, toUpdate.getId());

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    toUpdate.writeOn(resultSet);
                    resultSet.updateRow();
                } else {
                    SysLog.logSysError("Persistent to found: " + toUpdate.getId());
                }
            }
        }
        assertPersistedObject(toUpdate);
        return toUpdate;
    }

    /**
     * Deletes an persisted entity
     *
     * @param toDelete entity to delete
     * @return the deleted entity
     * @throws SQLException wrong table name
     */
    @Override
    public T delete(T toDelete) throws SQLException {
        assertIsNonNullArgument(toDelete);
        assertPersistedObject(toDelete);

        String query = String.format("DELETE FROM %s WHERE id = ?", getTableName());
        try (PreparedStatement stmt = getReadingStatement(query)) {
            stmt.setLong(1, toDelete.getId());
            stmt.execute();

            return toDelete;
        }
    }

    /**
     * @methodtype get
     */
    protected PreparedStatement getReadingStatement(String query) throws SQLException {
        SysLog.logQuery(query);

        DatabaseConnection dbc = getDatabaseConnection();
        return dbc.getReadingStatement(query);
    }

    /**
     * @methodtype get
     */
    protected PreparedStatement getUpdatingStatement(String query) throws SQLException {
        SysLog.logQuery(query);

        DatabaseConnection dbc = getDatabaseConnection();
        return dbc.getUpdatingStatement(query);
    }

    /**
     * @methodtype get
     */
    private DatabaseConnection getDatabaseConnection() {
        return SessionManager.getDatabaseConnection();
    }

    /**
     * @methodtype assert
     */
    protected void assertNonPersistedObject(Persistent toPersist) {
        if (toPersist.getId() != null) {
            String formatted = String.format("Object '%s' already has an id", toPersist);
            throw new IllegalStateException(formatted);
        }
    }

    /**
     * @methodtype assert
     */
    protected void assertPersistedObject(Persistent toPersist) {
        if (toPersist.getId() == null) {
            String formatted = String.format("Object '%s' has no id", toPersist);
            throw new IllegalStateException(formatted);
        }
    }

    /**
     * @methodtype assert
     */
    protected void assertIsNonNullArgument(Object arg) {
        assertIsNonNullArgument(arg, "persistent");
    }

    /**
     * @methodtype assert
     */
    protected void assertIsNonNullArgument(Object arg, String label) {
        if (arg == null) {
            throw new IllegalArgumentException(label + " should not be null");
        }
    }
}
