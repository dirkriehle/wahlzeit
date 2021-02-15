package org.wahlzeit_revisited.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T extends Persistent> {

    Optional<T> findById(Long id) throws SQLException;

    List<T> findAll() throws SQLException;

    T insert(T toInsert) throws SQLException;

    T update(T toUpdate) throws SQLException;

    T delete(T toDelete) throws SQLException;

}
