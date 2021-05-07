/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com, Aron Metzig
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

import jakarta.inject.Inject;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/*
 * Repository to query User entities
 */
public class UserRepository extends AbstractRepository<User> {

    @Inject
    public UserFactory factory;

    /**
     * Checks if a user with email exists
     * @param email the email to heck
     * @return according user exists
     * @throws SQLException wrong table name
     */
    public boolean hasByEmail(String email) throws SQLException {
        assertIsNonNullArgument(email);

        String query = String.format("SELECT * FROM %s WHERE email_address = ?", getTableName());

        try (PreparedStatement stmt = getReadingStatement(query)) {
            stmt.setString(1, email);

            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * Checks if a user with username exists
     * @param username the username to heck
     * @return according user exists
     * @throws SQLException wrong table name
     */
    public boolean hasByName(String username) throws SQLException {
        assertIsNonNullArgument(username);

        String query = String.format("SELECT * FROM %s WHERE name = ?", getTableName());
        try (PreparedStatement stmt = getReadingStatement(query)) {
            stmt.setString(1, username);

            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * Returns a user for username|email & password combination
     * @param identifier username or email
     * @param plainPassword the plain not hashed password
     * @return The according user
     * @throws SQLException wrong table name
     */
    public Optional<User> findByNameOrEmailAndPassword(String identifier, String plainPassword) throws SQLException {
        assertIsNonNullArgument(identifier);
        assertIsNonNullArgument(plainPassword);

        // One might hash the password in a productive system
        String query = String.format("SELECT * FROM %s WHERE email_address = ? OR name = ? AND password = ?", getTableName());
        try (PreparedStatement stmt = getReadingStatement(query)) {
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
