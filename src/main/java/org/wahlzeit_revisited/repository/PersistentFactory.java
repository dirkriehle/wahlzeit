package org.wahlzeit_revisited.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface PersistentFactory<T extends Persistent> {

    /**
     * Creates an persistent entity from a db query
     * @param resultSet database row
     * @return initialized entity
     * @throws SQLException invalid SQL statement
     */
    T createPersistent(ResultSet resultSet) throws SQLException;

}
