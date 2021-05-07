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
