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
    @Path("/rand")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getRandomPhoto() throws SQLException {
        PhotoDto responseDto = service.getRandomPhoto();
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(AccessRights.USER_ROLE)
    public Response removePhoto(@PathParam("id") Long photoId) throws SQLException {
        User user = getAuthorizedUser();
        PhotoDto photoDto = service.removePhoto(user, photoId);
        return Response.ok(photoDto).build();
    }

    @GET
    @Path("/{id}/data")
    @Produces("image/*")
    @PermitAll
    public Response getPhotoData(@PathParam("id") Long photoId) throws SQLException {
        byte[] response = service.getPhotoData(photoId);
        return Response.ok(response).build();
    }

    @POST
    @Path("/{id}/praise")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response praiseData(@PathParam("id") Long photoId, Long ranking) throws SQLException {
        PhotoDto photoDto = service.praisePhoto(photoId, ranking);
        return Response.ok(photoDto).build();
    }
}
