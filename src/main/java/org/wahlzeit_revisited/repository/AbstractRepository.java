package org.wahlzeit_revisited.repository;

import org.wahlzeit_revisited.database.DatabaseConnection;
import org.wahlzeit_revisited.database.SessionManager;
import org.wahlzeit_revisited.utils.SysLog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository<T extends Persistent> implements Repository<T> {

    /*
     * template methods
     */

    protected abstract PersistentFactory<T> getFactory();

    protected abstract String getTableName();

    /*
     * Repository contract
     */

    public Optional<T> findById(Long id) throws SQLException {
        assertIsNonNullArgument(id);

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

    /*
     * getters
     */

    protected PreparedStatement getReadingStatement(String query) throws SQLException {
        SysLog.logQuery(query);

        DatabaseConnection dbc = getDatabaseConnection();
        return dbc.getReadingStatement(query);
    }

    protected PreparedStatement getUpdatingStatement(String query) throws SQLException {
        SysLog.logQuery(query);

        DatabaseConnection dbc = getDatabaseConnection();
        return dbc.getUpdatingStatement(query);
    }

    private DatabaseConnection getDatabaseConnection() {
        return SessionManager.getDatabaseConnection();
    }

    /*
     * asserts
     */

    protected void assertNonPersistedObject(Persistent toPersist) {
        if (toPersist.getId() != null) {
            String formatted = String.format("Object '%s' already has an id", toPersist.toString());
            throw new IllegalStateException(formatted);
        }
    }

    protected void assertPersistedObject(Persistent toPersist) {
        if (toPersist.getId() == null) {
            String formatted = String.format("Object '%s' has no id", toPersist.toString());
            throw new IllegalStateException(formatted);
        }
    }

    protected void assertIsNonEmpty(Collection<?> arg) {
        if (arg.isEmpty()) {
            String formatted = "Collection is empty";
            throw new IllegalStateException(formatted);
        }
    }

    protected void assertIsNonNullArgument(Object arg) {
        assertIsNonNullArgument(arg, "persistent");
    }

    protected void assertIsNonNullArgument(Object arg, String label) {
        if (arg == null) {
            throw new IllegalArgumentException(label + " should not be null");
        }
    }
}
