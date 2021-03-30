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

import java.sql.SQLException;
import java.util.List;


@Path("api/photo")
public class PhotoResource extends AbstractResource {

    @Inject
    public PhotoService service;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(AccessRights.ADMINISTRATOR_ROLE)
    public Response getPhotos() throws SQLException {
        List<PhotoDto> responseDto = service.getPhotos();
        return Response.ok(responseDto).build();
    }

    @POST
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(AccessRights.USER_ROLE)
    public Response addPhoto(byte[] photoBlob) throws SQLException {
        User user = getAuthorizedUser();
        PhotoDto responseDto = service.addPhoto(user, photoBlob);
        return Response.ok(responseDto).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @PermitAll
    public Response getPhoto(@PathParam("id") Long photoId) throws SQLException {
        PhotoDto responseDto = service.getPhoto(photoId);
        return Response.ok(responseDto).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed(AccessRights.USER_ROLE)
    public Response removePhoto(@PathParam("id") Long photoId) throws SQLException {
        User user = getAuthorizedUser();
        PhotoDto photoDto = service.removePhoto(user, photoId);
        return Response.ok(photoDto).build();
    }

}
