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

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.wahlzeit_revisited.auth.PrincipalUser;
import org.wahlzeit_revisited.dto.ErrorDto;
import org.wahlzeit_revisited.model.User;

/**
 * A Resource is the bridge between the outer world and this application
 * If an endpoint is annotated with @RolesAllowed only registered users
 * can access this endpoint and the caller can get extracted
 */
public abstract class AbstractResource {

    @Inject
    SecurityContext securityContext;

    /**
     * Returns the caller or throws an internal exception
     * @return the caller from the current call context
     */
    protected User getAuthorizedUser() {
        if (securityContext.getUserPrincipal() == null) {
            throw new IllegalStateException("No principal found, is @RolesAllowed added properly to the endpoint");
        }

        PrincipalUser principalUser = (PrincipalUser) securityContext.getUserPrincipal();
        return principalUser.getUser();
    }

    /**
     * @methodtype convenience
     */
    protected Response buildBadRequest() {
        ErrorDto errorDto = new ErrorDto("You may missed an api field");
        return Response.status(Response.Status.BAD_REQUEST).entity(errorDto).build();
    }

}
