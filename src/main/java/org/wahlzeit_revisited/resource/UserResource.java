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

package org.wahlzeit_revisited.resource;


import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.dto.LoginRequestDto;
import org.wahlzeit_revisited.dto.UserCreationDto;
import org.wahlzeit_revisited.dto.UserDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.service.UserService;

import java.sql.SQLException;
import java.util.List;

/*
 * The bridge between the outer world and users
 */
@Path("api/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource extends AbstractResource {

    @Inject
    public UserService service;

    @GET
    @RolesAllowed(AccessRights.ADMINISTRATOR_ROLE)
    public Response getUsers() throws SQLException {
        List<UserDto> responseDto = service.getUsers();
        return Response.ok(responseDto).build();
    }


    @GET
    @Path("/{id}")
    @RolesAllowed(AccessRights.USER_ROLE)
    public Response getUser(@PathParam("id") Long userId) throws SQLException {
        UserDto responseDto = service.getUser(userId);
        return Response.ok(responseDto).build();
    }

    @POST
    @PermitAll
    public Response createUser(UserCreationDto creationDto) throws SQLException {
        if (creationDto.getName() == null || creationDto.getEmail() == null || creationDto.getPassword() == null) {
            return buildBadRequest();
        }

        UserDto responseDto = service.createUser(creationDto.getName(), creationDto.getEmail(), creationDto.getPassword());
        return Response.ok(responseDto).build();
    }

    @Path("/login")
    @POST
    @PermitAll
    public Response login(LoginRequestDto creationDto) throws SQLException {
        if (creationDto.getEmail() == null || creationDto.getPassword() == null) {
            return buildBadRequest();
        }

        UserDto responseDto = service.login(creationDto.getEmail(), creationDto.getPassword());
        return Response.ok(responseDto).build();

    }

    @DELETE
    @RolesAllowed(AccessRights.USER_ROLE)
    public Response deleteUser() throws SQLException {
        User user = getAuthorizedUser();
        UserDto responseDto = service.deleteUser(user);
        return Response.ok(responseDto).build();
    }

}
