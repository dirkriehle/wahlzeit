package org.wahlzeit_revisited.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface PersistentFactory<T extends Persistent> {

    T createPersistent(ResultSet resultSet) throws SQLException;

}
