package org.wahlzeit_revisited.repository;

import jakarta.inject.Inject;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository extends AbstractRepository<User> {

    @Inject
    public UserFactory factory;

    /*
     * business methods
     */

    public boolean hasByEmail(String email) throws SQLException {
        assertIsNonNullArgument(email);

        String query = String.format("SELECT * FROM %s WHERE email_address = ?", getTableName());
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setString(1, email);

        try (ResultSet resultSet = stmt.executeQuery()) {
            return resultSet.next();
        }
    }

    public boolean hasByName(String username) throws SQLException {
        assertIsNonNullArgument(username);

        String query = String.format("SELECT * FROM %s WHERE name = ?", getTableName());
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setString(1, username);

        try (ResultSet resultSet = stmt.executeQuery()) {
            return resultSet.next();
        }
    }

    public Optional<User> findByNameOrEmailAndPassword(String identifier, String plainPassword) throws SQLException {
        assertIsNonNullArgument(identifier);
        assertIsNonNullArgument(plainPassword);

        String query = String.format("SELECT * FROM %s WHERE email_address = ? OR name = ? AND password = ?", getTableName());
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setString(1, identifier);
        stmt.setString(2, identifier);
        stmt.setString(3, plainPassword);

        User result = null;
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                result = factory.createPersistent(resultSet);
            }
        }

        return Optional.ofNullable(result);
    }

    /*
     * AbstractRepository Contract
     */

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    protected PersistentFactory<User> getFactory() {
        return factory;
    }

}
