package org.wahlzeit_revisited.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.wahlzeit_revisited.auth.PrincipalUser;
import org.wahlzeit_revisited.model.User;

public abstract class AbstractResource {

    @Inject
    SecurityContext securityContext;

    protected User getAuthorizedUser() {
        if (securityContext.getUserPrincipal() == null) {
            throw new IllegalStateException("No principal found, is @RolesAllowed added properly to the endpoint");
        }

        PrincipalUser principalUser = (PrincipalUser) securityContext.getUserPrincipal();
        return principalUser.getUser();
    }

    protected Response buildBadRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    protected Response buildServerError() {
        return Response.serverError().build();
    }

}
