/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 * Copyright (c) 2021 by Aron Metzig
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

package org.wahlzeit.api.filter;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wahlzeit.api.auth.AccessRights;
import org.wahlzeit.api.auth.PrincipalUser;
import org.wahlzeit.api.dto.ErrorDto;
import org.wahlzeit.database.repository.UserRepository;
import org.wahlzeit.model.User;

import java.lang.reflect.Method;
import java.security.Principal;
import java.sql.SQLException;
import java.util.*;

/**
 * Jakarta Filter that handles the mapping from credentials to the user entity
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    protected static final Logger LOG = LogManager.getLogger(AuthenticationFilter.class);

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
     * <p>
     * For a productive project a token based authorization scheme is advised
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
            LOG.error("Cannot find user", sqlException);
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
