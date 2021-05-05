package org.wahlzeit_revisited.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.dto.CaseDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.service.CaseService;

import java.util.List;

@Path("api/case")
public class CaseResource extends AbstractResource {

    @Inject
    CaseService service;

    @GET
    public Response getCases() throws Exception {
        List<CaseDto> responseDto = service.getCases();
        return Response.ok(responseDto).build();
    }

    @POST
    public Response createCase(@QueryParam("photoId") Long photoId, String reason) throws Exception {
        User flagger = getAuthorizedUser();
        CaseDto responseDto = service.createCase(flagger, photoId, reason);
        return Response.ok(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
    public Response closeCase(@PathParam("id") long caseId) throws Exception {
        CaseDto responseDto = service.closeCase(caseId);
        return Response.ok(responseDto).build();
    }

}
