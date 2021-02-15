package org.wahlzeit_revisited.resource;


import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wahlzeit.services.SysLog;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.dto.LoginRequestDto;
import org.wahlzeit_revisited.dto.UserCreationDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.service.UserService;

@Path("api/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource extends AbstractResource {

    @Inject
    public UserService service;

    @GET
    @RolesAllowed(AccessRights.ADMINISTRATOR_ROLE)
    public Response getUsers() {
        try {
            return service.getUsers();
        } catch (Exception e) {
            SysLog.logThrowable(e);
            return buildServerError();
        }
    }

    @POST
    @PermitAll
    public Response createUser(UserCreationDto creationDto) {
        if (creationDto.getUsername() == null || creationDto.getEmail() == null || creationDto.getPassword() == null) {
            return buildBadRequest();
        }

        try {
            return service.createUser(creationDto.getUsername(), creationDto.getEmail(), creationDto.getPassword());
        } catch (Exception e) {
            SysLog.logThrowable(e);
            return buildServerError();
        }
    }

    @Path("/login")
    @POST
    @PermitAll
    public Response login(LoginRequestDto creationDto) {
        if (creationDto.getEmail() == null || creationDto.getPassword() == null) {
            return buildBadRequest();
        }

        try {
            return service.login(creationDto.getEmail(), creationDto.getPassword());
        } catch (Exception e) {
            SysLog.logThrowable(e);
            return buildServerError();
        }
    }

    @DELETE
    @RolesAllowed(AccessRights.USER_ROLE)
    public Response deleteUser() {
        User user = getAuthorizedUser();

        try {
            return service.deleteUser(user);
        } catch (Exception e) {
            SysLog.logThrowable(e);
            return buildServerError();
        }
    }

}
