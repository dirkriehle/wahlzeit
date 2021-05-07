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

package org.wahlzeit_revisited.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.dto.UserDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;
import org.wahlzeit_revisited.repository.UserRepository;
import org.wahlzeit_revisited.utils.SysLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Business logic of users
 */
@Singleton
public class UserService {

    @Inject
    public Transformer transformer;
    @Inject
    public UserRepository repository;
    @Inject
    public UserFactory factory;

    /**
     * Get all users
     *
     * @return all users
     * @throws SQLException internal error
     */
    public List<UserDto> getUsers() throws SQLException {
        List<User> userList = repository.findAll();

        List<UserDto> responseDto = new ArrayList<>(userList.size());
        for (User photo : userList) {
            responseDto.add(transformer.transform(photo));
        }
        SysLog.logSysInfo(String.format("Fetched %s users", responseDto.size()));
        return responseDto;
    }

    /**
     * Finds a user by id
     *
     * @param userId the user to find
     * @return found user
     * @throws SQLException internal error
     */
    public UserDto getUser(Long userId) throws SQLException {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("Unknown UserId"));

        SysLog.logSysInfo(String.format("Got user by id: %s ", user.getId()));
        UserDto responseDto = transformer.transform(user);
        return responseDto;
    }

    /**
     * Creates a new User
     * <p>
     * Class is not thread safe, as email might be no longer unique
     * due a race condition
     * <p>
     * One might consider transactions for a productive application
     * One might consider password hashing for a productive application
     *
     * @param username      register username
     * @param email         register email
     * @param plainPassword register password
     * @return created user
     * @throws SQLException internal error
     */
    public synchronized UserDto createUser(String username, String email, String plainPassword) throws SQLException {
        if (repository.hasByName(username) || repository.hasByEmail(email)) {
            throw new WebApplicationException("Email already registered", Response.Status.CONFLICT);
        }

        User createdUser = factory.createUser(username, email, plainPassword, AccessRights.USER);
        createdUser = repository.insert(createdUser);

        SysLog.logSysInfo(String.format("Created user: %s ", createdUser.getId()));
        UserDto responseDto = transformer.transform(createdUser);
        return responseDto;
    }

    /**
     * Returns the userDto for the credentials
     *
     * @param identifier the users email or username
     * @param password   according password
     * @return matching userDto
     * @throws SQLException internal error
     */
    public UserDto login(String identifier, String password) throws SQLException {
        User loginUser = repository.findByNameOrEmailAndPassword(identifier, password)
                .orElseThrow(() -> new NotFoundException("Invalid credentials"));

        SysLog.logSysInfo(String.format("Logged user in: %s ", loginUser.getId()));
        UserDto responseDto = transformer.transform(loginUser);
        return responseDto;
    }

    /**
     * Deletes an user
     *
     * @param user to delete
     * @return deleted user
     * @throws SQLException internal error
     */
    public UserDto deleteUser(User user) throws SQLException {
        User deletedUser = repository.delete(user);

        SysLog.logSysInfo(String.format("Deleted user: %s ", deletedUser.getId()));
        UserDto responseDto = transformer.transform(deletedUser);
        return responseDto;
    }

}
