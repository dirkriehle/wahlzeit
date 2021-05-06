package org.wahlzeit_revisited.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.service.PhotoFlagService;

import java.util.List;

/*
 * The bridge between the outer world and photo flagging
 */
@Path("api/photo/flag")
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
