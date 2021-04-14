package org.wahlzeit_revisited.filter;

import jakarta.annotation.Priority;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import org.wahlzeit_revisited.dto.ErrorDto;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.auth.PrincipalUser;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.repository.UserRepository;
import org.wahlzeit_revisited.utils.SysLog;

import java.lang.reflect.Method;
import java.security.Principal;
import java.sql.SQLException;
import java.util.*;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String AUTHENTICATION_SCHEME = "Basic ";

    @Context
    public ResourceInfo resourceInfo;
    @Inject
    public UserRepository userRepository;

    /**
     * A Jakarta Filter, this is applied before each request
     * <p>
     * It checks the called resource for the @ROLES_ALLOWED annotation.
     * If this annotation is present, a credential check is performed.
     * A successful check set's the security context,
     * an unsuccessful check makes the request fail and the resource is never called
     * <p>
     * Currently only Basic HTTP authentication is supported. For more information see:
     * https://en.wikipedia.org/wiki/Basic_access_authentication
     *
     * @param requestContext the call context
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method resource = resourceInfo.getResourceMethod();

        if (resource.isAnnotationPresent(DenyAll.class)) {
            ErrorDto errorDto = new ErrorDto("Access blocked for all users");
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity(errorDto).build());
        } else if (resource.isAnnotationPresent(RolesAllowed.class)) {

            String credentials = extractCredentials(requestContext);
            if (credentials == null) {
                ErrorDto errorDto = new ErrorDto("Not allowed, you need to login first");
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity(errorDto).build());
                return;
            }

            // split values from "username:password"
            int splitIndex = credentials.indexOf(':');
            String username = credentials.substring(0, splitIndex);
            String password = credentials.substring(splitIndex + 1);

            // find according user
            Optional<User> userOpt = findUser(username, password);
            if (userOpt.isEmpty()) {
                ErrorDto errorDto = new ErrorDto("Not allowed, invalid credentials");
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity(errorDto).build());
                return;
            }

            // Get roles names
            AccessRights userRights = userOpt.get().getRights();
            RolesAllowed rolesAnnotation = resource.getAnnotation(RolesAllowed.class);
            Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));
            for (String roleName : rolesSet) {
                // Check if user rights match endpoint accessRights
                if (!AccessRights.hasRights(userRights, AccessRights.getFromString(roleName))) {
                    ErrorDto errorDto = new ErrorDto("Not allowed, user has no sufficient rights");
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                            .entity(errorDto).build());
                    return;
                }
            }

            setSecurityContext(userOpt.get(), requestContext);
        }
    }

    /**
     * Extracts the BASIC header value and performs the base64 decoding
     *
     * @param requestContext the current HTTP request
     * @return the encoded and ready to split credentials or null on failure
     */
    private String extractCredentials(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || authorizationHeader.length() <= AUTHENTICATION_SCHEME.length()) {
            return null;
        }

        // decode base64
        String encodedUserNamePwd = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
        String credentials = new String(Base64.getDecoder().decode(encodedUserNamePwd));

        // check valid data
        int splitIndex = credentials.indexOf(':');
        if (splitIndex == -1) {
            return null;
        }
        return credentials;
    }

    /**
     * Convenience method for wrapping up barely possible SQL errors
     *
     * @param email    the users email
     * @param password the users password
     * @return the User that got found, or an empty Optional
     */
    private Optional<User> findUser(String email, String password) {
        try {
            return userRepository.findByNameOrEmailAndPassword(email, password);
        } catch (SQLException sqlException) {
            SysLog.logThrowable(sqlException);
            return Optional.empty();
        }
    }

    /**
     * Sets the security context, which can be accessed via @inject in the called resource
     * Wraps the user object to fulfill the principal interface
     *
     * @param user           the user that got extracted from the headers
     * @param requestContext the call context
     */
    private void setSecurityContext(User user, ContainerRequestContext requestContext) {
        PrincipalUser principalUser = new PrincipalUser(user);
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return principalUser;
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return true;
            }

            @Override
            public String getAuthenticationScheme() {
                return AUTHENTICATION_SCHEME;
            }
        });
    }

}
