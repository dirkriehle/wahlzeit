package org.wahlzeit_revisited.repository;

import org.wahlzeit.services.SysLog;
import org.wahlzeit_revisited.database.DatabaseConnection;
import org.wahlzeit_revisited.database.SessionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository<T extends Persistent> implements Repository<T> {

    /*
     * template methods
     */

    protected abstract Optional<T> doFindById(Long id) throws SQLException;

    protected abstract List<T> doFindAll() throws SQLException;

    protected abstract T doInsert(T toInsert) throws SQLException;

    protected abstract T doUpdate(T toUpdate) throws SQLException;

    protected abstract T doDelete(T toDelete) throws SQLException;

    /*
     * Repository contract
     */

    public Optional<T> findById(Long id) throws SQLException {
        assertIsNonNullArgument(id);

        return doFindById(id);
    }

    public List<T> findAll() throws SQLException {
        return doFindAll();
    }

    public T insert(T toInsert) throws SQLException {
        assertIsNonNullArgument(toInsert);
        assertNonPersistedObject(toInsert);

        T result = doInsert(toInsert);

        assertPersistedObject(result);
        return result;
    }

    public T update(T toUpdate) throws SQLException {
        assertIsNonNullArgument(toUpdate);
        assertPersistedObject(toUpdate);

        T result = doUpdate(toUpdate);

        assertPersistedObject(result);
        return result;
    }

    public T delete(T toDelete) throws SQLException {
        assertIsNonNullArgument(toDelete);
        assertPersistedObject(toDelete);

        T result = doDelete(toDelete);

        assertIsNonNullArgument(result);
        return result;
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

    protected void assertIsNonNullArgument(Object arg) {
        assertIsNonNullArgument(arg, "persistent");
    }

    protected void assertIsNonNullArgument(Object arg, String label) {
        if (arg == null) {
            throw new IllegalArgumentException(label + " should not be null");
        }
    }
}
