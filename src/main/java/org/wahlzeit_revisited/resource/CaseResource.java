package org.wahlzeit_revisited.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.dto.CaseDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.service.CaseService;

import java.util.List;

/*
 * The bridge between the outer world and cases
 */
@Path("api/case")
public class CaseResource extends AbstractResource {

    @Inject
    CaseService service;

    @GET
    @RolesAllowed(AccessRights.MODERATOR_ROLE)
    public Response getCases() throws Exception {
        List<CaseDto> responseDto = service.getAllCases();
        return Response.ok(responseDto).build();
    }

    @POST
    @RolesAllowed(AccessRights.MODERATOR_ROLE)
    public Response createCase(@QueryParam("photoId") Long photoId, String reason) throws Exception {
        User flagger = getAuthorizedUser();
        CaseDto responseDto = service.createCase(flagger, photoId, reason);
        return Response.ok(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(AccessRights.MODERATOR_ROLE)
    public Response closeCase(@PathParam("id") long caseId) throws Exception {
        CaseDto responseDto = service.closeCase(caseId);
        return Response.ok(responseDto).build();
    }

}
