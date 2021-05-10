/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 * Copyright (c) 2021 by Aron Metzig
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

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.api.auth.AccessRights;
import org.wahlzeit_revisited.api.dto.PhotoDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.service.PhotoFlagService;

import java.util.List;

/*
 * The bridge between the outer world and photo flagging
 */
@Path("api/photo/flag")
@Produces(MediaType.APPLICATION_JSON)
public class PhotoFlagResource extends AbstractResource {

    @Inject
    PhotoFlagService service;

    @GET
    @RolesAllowed(AccessRights.MODERATOR_ROLE)
    public Response getFlaggedPhotos() throws Exception {
        List<PhotoDto> photoDto = service.getFlaggedPhotos();
        return Response.ok(photoDto).build();
    }

    @GET
    @Path("{id}")
    @RolesAllowed(AccessRights.MODERATOR_ROLE)
    public Response getFlaggedPhoto(@PathParam("id") Long photoId) throws Exception {
        PhotoDto photoDto = service.getFlaggedPhoto(photoId);
        return Response.ok(photoDto).build();
    }

    @GET
    @Path("{id}/data")
    @RolesAllowed(AccessRights.MODERATOR_ROLE)
    public Response getFlaggedPhotoData(@PathParam("id") Long photoId) throws Exception {
        byte[] response = service.getFlaggedPhotoData(photoId);
        return Response.ok(response).build();
    }

    @POST
    @Path("{id}")
    @RolesAllowed(AccessRights.MODERATOR_ROLE)
    public Response flagPhoto(@PathParam("id") Long photoId, String status) throws Exception {
        User user = getAuthorizedUser();
        PhotoDto photoDto = service.setPhotoStatus(user, photoId, status);
        return Response.ok(photoDto).build();
    }

}
