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


@Path("api/photo")
public class PhotoResource extends AbstractResource {

    @Inject
    public PhotoService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getPhotos(@QueryParam("tags") Set<String> unescapedTags) throws SQLException {
        List<PhotoDto> responseDto;
        if (unescapedTags == null || unescapedTags.isEmpty()) {
            responseDto = service.getPhotos();
        } else {
            responseDto = service.getTaggedPhotos(unescapedTags);
        }
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

    @GET
    @Path("/{id}/data")
    @PermitAll
    public Response getPhotoData(@PathParam("id") Long id) throws SQLException {
        byte[] response = service.getPhotoData(id);
        return Response.ok(response).build();
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

}
