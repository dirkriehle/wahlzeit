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
