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
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.service.PhotoService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/*
 * The bridge between the outer world and photos
 */
@Path("api/photo")
public class PhotoResource extends AbstractResource {

    @Inject
    public PhotoService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getPhotos(@QueryParam("user") Long userId, @QueryParam("tags") Set<String> unescapedTags) throws SQLException {
        unescapedTags = unescapedTags == null ? Set.of() : unescapedTags;
        List<PhotoDto> responseDto = service.getFilteredPhotos(userId, unescapedTags);
        return Response.ok(responseDto).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(AccessRights.USER_ROLE)
    public Response addPhoto(byte[] photoBlob, @QueryParam("tags") Set<String> tags) throws SQLException, IOException {
        if (tags == null) {
            tags = Set.of();
        }
        User user = getAuthorizedUser();
        PhotoDto responseDto = service.addPhoto(user, photoBlob, tags);
        return Response.ok(responseDto).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getPhoto(@PathParam("id") Long photoId) throws SQLException {
        PhotoDto responseDto = service.getPhoto(photoId);
        return Response.ok(responseDto).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed(AccessRights.USER_ROLE)
    public Response removePhoto(@PathParam("id") Long photoId) throws SQLException {
        User user = getAuthorizedUser();
        PhotoDto photoDto = service.removePhoto(user, photoId);
        return Response.ok(photoDto).build();
    }

    @GET
    @Path("/{id}/data")
    @PermitAll
    public Response getPhotoData(@PathParam("id") Long photoId) throws SQLException {
        byte[] response = service.getPhotoData(photoId);
        return Response.ok(response).build();
    }

    @POST
    @Path("/{id}/praise")
    @PermitAll
    public Response praiseData(@PathParam("id") Long photoId, Long ranking) throws SQLException {
        PhotoDto photoDto = service.praisePhoto(photoId, ranking);
        return Response.ok(photoDto).build();
    }
}
