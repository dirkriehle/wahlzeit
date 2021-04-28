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
import org.wahlzeit_revisited.service.PhotoCaseService;

import java.sql.SQLException;
import java.util.List;

@Path("api/photo/case")
public class PhotoCaseResource {

    @Inject
    PhotoCaseService service;

    @GET
    @RolesAllowed(AccessRights.MODERATOR_ROLE)
    public Response getFlaggedPhotos() throws Exception {
        List<PhotoDto> photoDto = service.getFlaggedPhotos();
        return Response.ok(photoDto).build();
    }

    @POST
    @Path("{id}")
    @RolesAllowed(AccessRights.MODERATOR_ROLE)
    public Response flagPhoto(@PathParam("id") Long photoId, String status) throws Exception {
        PhotoDto photoDto = service.setPhotoStatus(photoId, status);
        return Response.ok(photoDto).build();
    }

}
