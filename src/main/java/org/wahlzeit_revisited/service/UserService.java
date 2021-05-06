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

@Singleton
public class UserService {

    @Inject
    public Transformer transformer;
    @Inject
    public UserRepository repository;
    @Inject
    public UserFactory factory;

    /*
     * business methods
     */

    public List<UserDto> getUsers() throws SQLException {
        List<User> userList = repository.findAll();

        List<UserDto> responseDto = new ArrayList<>(userList.size());
        for (User photo : userList) {
            responseDto.add(transformer.transform(photo));
        }
        return responseDto;
    }

    public UserDto getUser(Long userId) throws SQLException {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("Unknown UserId"));

        UserDto responseDto = transformer.transform(user);
        return responseDto;
    }

    public synchronized UserDto createUser(String username, String email, String plainPassword) throws SQLException {
        if (repository.hasByName(username) || repository.hasByEmail(email)) {
            throw new WebApplicationException("Email already registered", Response.Status.CONFLICT);
        }

        User createdUser = factory.createUser(username, email, plainPassword, AccessRights.USER);

        createdUser = repository.insert(createdUser);
        SysLog.logSysInfo(String.format("Created user: %s (%s)  ", createdUser.getEmail(), createdUser.getId()));

        UserDto responseDto = transformer.transform(createdUser);
        return responseDto;
    }

    public UserDto login(String email, String password) throws SQLException {
        User loginUser = repository.findByNameOrEmailAndPassword(email, password)
                .orElseThrow(() -> new NotFoundException("Invalid credentials"));

        UserDto responseDto = transformer.transform(loginUser);
        return responseDto;
    }

    public UserDto deleteUser(User user) throws SQLException {
        User deletedUser = repository.delete(user);
        SysLog.logSysInfo(String.format("Deleted user: %s (%s)  ", deletedUser.getEmail(), deletedUser.getId()));

        UserDto responseDto = transformer.transform(deletedUser);
        return responseDto;
    }

}
