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

    public Optional<User> findByEmailPassword(String email, String plainPassword) throws SQLException {
        assertIsNonNullArgument(email);
        assertIsNonNullArgument(plainPassword);

        String query = String.format("SELECT * FROM %s WHERE email_address = ? AND password = ?", getTableName());
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setString(1, email);
        stmt.setString(2, plainPassword);

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
