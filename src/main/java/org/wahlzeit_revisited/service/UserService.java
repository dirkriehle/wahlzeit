package org.wahlzeit_revisited.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.wahlzeit.services.SysLog;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.dto.UserDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;
import org.wahlzeit_revisited.repository.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Response getUsers() throws SQLException {
        List<User> userList = repository.findAll();

        List<UserDto> responseDto = new ArrayList<>(userList.size());
        for (User photo : userList) {
            responseDto.add(transformer.transform(photo));
        }
        return Response.ok(responseDto).build();
    }

    public Response createUser(String username, String email, String plainPassword) throws SQLException {
        User createdUser = factory.createUser(username, email, plainPassword, AccessRights.USER);

        createdUser = repository.insert(createdUser);
        SysLog.logSysInfo(String.format("Created user: %s (%s)  ", createdUser.getEmail(), createdUser.getId()));

        UserDto responseDto = transformer.transform(createdUser);
        return Response.ok(responseDto).build();
    }

    public Response login(String email, String password) throws SQLException {
        Optional<User> loginUserOpt = repository.findByEmailPassword(email, password);
        if (loginUserOpt.isEmpty()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        UserDto responseDto = transformer.transform(loginUserOpt.get());
        return Response.ok(responseDto).build();
    }

    public Response deleteUser(User user) throws SQLException {
        User deletedUser = repository.delete(user);
        SysLog.logSysInfo(String.format("Deleted user: %s (%s)  ", deletedUser.getEmail(), deletedUser.getId()));

        UserDto responseDto = transformer.transform(deletedUser);
        return Response.ok(responseDto).build();
    }

}
