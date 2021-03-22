package org.wahlzeit_revisited.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.service.PhotoService;
import org.wahlzeit_revisited.utils.SysLog;


@Path("api/photo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PhotoResource extends AbstractResource {

    @Inject
    public PhotoService service;

    @GET
    @RolesAllowed(AccessRights.ADMINISTRATOR_ROLE)
    public Response getPhotos() {
        try {
            return service.getPhotos();
        } catch (Exception e) {
            SysLog.logThrowable(e);
            return buildServerError();
        }
    }

    @POST
    @RolesAllowed(AccessRights.USER_ROLE)
    public Response addPhoto(byte[] photoBlob) {
        User user = getAuthorizedUser();

        try {
            return service.addPhoto(user, photoBlob);
        } catch (Exception e) {
            SysLog.logThrowable(e);
            return buildServerError();
        }
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public Response getPhoto(@PathParam("id") Long photoId) {
        try {
            return service.getPhoto(photoId);
        } catch (Exception e) {
            SysLog.logThrowable(e);
            return buildServerError();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(AccessRights.USER_ROLE)
    public Response removePhoto(@PathParam("id") Long photoId) {
        User user = getAuthorizedUser();

        try {
            return service.removePhoto(user, photoId);
        } catch (Exception e) {
            SysLog.logThrowable(e);
            return buildServerError();
        }
    }

}
