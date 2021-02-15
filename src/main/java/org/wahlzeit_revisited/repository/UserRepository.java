package org.wahlzeit_revisited.repository;

import jakarta.inject.Inject;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository extends AbstractRepository<User> {

    @Inject
    public UserFactory factory;

    /*
     * AbstractRepository Contract
     */

    @Override
    protected Optional<User> doFindById(Long id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setLong(1, id);

        User result = null;
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                result = parseAsteriskRow(resultSet);
            }
        }

        return Optional.ofNullable(result);
    }

    @Override
    protected List<User> doFindAll() throws SQLException {
        String query = "SELECT * FROM users";
        PreparedStatement stmt = getReadingStatement(query);

        List<User> result = new ArrayList<>();
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                User user = parseAsteriskRow(resultSet);
                result.add(user);
            }
        }

        return result;
    }

    @Override
    protected User doInsert(User toInsert) throws SQLException {
        String query = "INSERT INTO users (creation_time, name, email_address, password, rights) VALUES(?, ?, ?, ?, ?) RETURNING id";
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setLong(1, toInsert.getCreationTime());
        stmt.setString(2, toInsert.getName());
        stmt.setString(3, toInsert.getEmail());
        stmt.setString(4, toInsert.getPassword());
        stmt.setInt(5, toInsert.getRights().asInt());

        // Extract id from RETURNING statement
        try (ResultSet returningSet = stmt.executeQuery()) {
            returningSet.next();
            Long persistedId = returningSet.getLong(1);
            toInsert.setId(persistedId);
        }

        return toInsert;
    }

    @Override
    protected User doUpdate(User toUpdate) throws SQLException {
        String query = "UPDATE users SET name = ?, password = ?, email_address = ?, rights = ? WHERE id = ?";
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setString(1, toUpdate.getName());
        stmt.setString(2, toUpdate.getPassword());
        stmt.setString(3, toUpdate.getEmail());
        stmt.setInt(4, toUpdate.getRights().asInt());
        stmt.setLong(5, toUpdate.getId());

        stmt.execute();
        return toUpdate;
    }

    @Override
    protected User doDelete(User toDelete) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setLong(1, toDelete.getId());
        stmt.execute();

        return toDelete;
    }

    /*
     * business methods
     */

    public Optional<User> findByEmailPassword(String email, String plainPassword) throws SQLException {
        assertIsNonNullArgument(email);
        assertIsNonNullArgument(plainPassword);

        String query = "SELECT * FROM users WHERE email_address = ? AND password = ?";
        PreparedStatement stmt = getReadingStatement(query);
        stmt.setString(1, email);
        stmt.setString(2, plainPassword);

        User result = null;
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                result = parseAsteriskRow(resultSet);
            }
        }

        return Optional.ofNullable(result);
    }

    /*
     * helper methods
     */

    private User parseAsteriskRow(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong(1);
        Long created = resultSet.getLong(2);
        String name = resultSet.getString(3);
        String emailAddress = resultSet.getString(4);
        String password = resultSet.getString(5);
        AccessRights accessRights = AccessRights.getFromInt(resultSet.getInt(6));

        return factory.createUser(id, created, name, emailAddress, password, accessRights);
    }

}
