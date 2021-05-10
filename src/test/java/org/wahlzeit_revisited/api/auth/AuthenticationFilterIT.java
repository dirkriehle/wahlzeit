package org.wahlzeit_revisited.api.auth;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.api.filter.AuthenticationFilter;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;
import org.wahlzeit_revisited.api.repository.UserRepository;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.sql.SQLException;
import java.util.*;

public class AuthenticationFilterIT extends BaseModelTest {

    private AuthenticationFilter filter;
    private UserRepository userRepository;
    private UserFactory userFactory;

    @Before
    public void setupDependencies() {
        userFactory = new UserFactory();
        userRepository = new UserRepository();
        userRepository.factory = userFactory;
        filter = new AuthenticationFilter();
        filter.userRepository = userRepository;
    }

    @Test
    @PermitAll
    public void test_PermitAll() throws NoSuchMethodException {
        // arrange
        Method mockMethod = AuthenticationFilterIT.class.getMethod("test_PermitAll");
        filter.resourceInfo = new MockResourceInfo(mockMethod);
        MockRequest mockRequest = new MockRequest();

        // act
        filter.filter(mockRequest);

        // assert
        Assert.assertNull(mockRequest.getAbortResponse());
        Assert.assertNull(mockRequest.getSecurityContext());
    }

    @Test
    @DenyAll
    public void test_DenyAll() throws NoSuchMethodException {
        // arrange
        Method mockMethod = AuthenticationFilterIT.class.getMethod("test_DenyAll");
        filter.resourceInfo = new MockResourceInfo(mockMethod);
        MockRequest mockRequest = new MockRequest();

        // act
        filter.filter(mockRequest);

        // assert
        Assert.assertNotNull(mockRequest.getAbortResponse());
        Assert.assertNull(mockRequest.getSecurityContext());
    }

    @Test
    @RolesAllowed(AccessRights.USER_ROLE)
    public void test_SufficientRights() throws NoSuchMethodException, SQLException {
        // arrange
        Method mockMethod = AuthenticationFilterIT.class.getMethod("test_SufficientRights");
        filter.resourceInfo = new MockResourceInfo(mockMethod);

        // create new unique User
        String name = buildUniqueName("sufficient");
        String email = buildUniqueEmail("sufficient");
        String password = "SimplePasswort1234";
        User authUser = userFactory.createUser(name, email, password, AccessRights.USER);
        userRepository.insert(authUser);

        // add header according to the BASIC auth standard
        String credentials = email + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        Map<String, String> authHeader = Map.of(HttpHeaders.AUTHORIZATION, "Bearer " + encodedCredentials);
        MockRequest mockRequest = new MockRequest(authHeader);

        // act
        filter.filter(mockRequest);

        // assert
        Assert.assertNull(mockRequest.getAbortResponse());
        Assert.assertNotNull(mockRequest.getSecurityContext());
    }

    @Test
    @RolesAllowed(AccessRights.ADMINISTRATOR_ROLE)
    public void test_InsufficientRights() throws NoSuchMethodException, SQLException {
        // arrange
        Method mockMethod = AuthenticationFilterIT.class.getMethod("test_InsufficientRights");
        filter.resourceInfo = new MockResourceInfo(mockMethod);

        // create new unique User
        String name = buildUniqueName("insufficient");
        String email = buildUniqueEmail("insufficient");
        String password = "SimplePasswort1234";
        User authUser = userFactory.createUser(name, email, password, AccessRights.USER);
        userRepository.insert(authUser);

        // add header according to the BASIC auth standard
        String credentials = email + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        Map<String, String> authHeader = Map.of(HttpHeaders.AUTHORIZATION, "Bearer " + encodedCredentials);
        MockRequest mockRequest = new MockRequest(authHeader);

        // act
        filter.filter(mockRequest);

        // assert
        Assert.assertNotNull(mockRequest.getAbortResponse());
        Assert.assertNull(mockRequest.getSecurityContext());
    }

    @Test
    @RolesAllowed(AccessRights.GUEST_ROLE)
    public void test_SuperiorRightsFilter() throws NoSuchMethodException, SQLException {
        // arrange
        Method mockMethod = AuthenticationFilterIT.class.getMethod("test_SuperiorRightsFilter");
        filter.resourceInfo = new MockResourceInfo(mockMethod);

        // create new unique User
        String name = buildUniqueName("superior");
        String email = buildUniqueEmail("superior");
        String password = "SimplePasswort1234";
        User authUser = userFactory.createUser(name, email, password, AccessRights.USER);
        userRepository.insert(authUser);

        // add header according to the BASIC auth standard
        String credentials = email + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        Map<String, String> authHeader = Map.of(HttpHeaders.AUTHORIZATION, "Bearer " + encodedCredentials);
        MockRequest mockRequest = new MockRequest(authHeader);

        // act
        filter.filter(mockRequest);

        // assert
        Assert.assertNull(mockRequest.getAbortResponse());
        Assert.assertNotNull(mockRequest.getSecurityContext());
    }


    /*
     * Mock classes
     */

    private static class MockResourceInfo implements ResourceInfo {

        private final Method mockMethod;

        private MockResourceInfo(Method mockMethod) {
            this.mockMethod = mockMethod;
        }

        @Override
        public Method getResourceMethod() {
            return mockMethod;
        }

        @Override
        public Class<?> getResourceClass() {
            throw new RuntimeException("Not implemented");
        }
    }

    private static class MockRequest implements ContainerRequestContext {

        private Response abortResponse;
        private SecurityContext securityContext;
        private final Map<String, String> mockHeaders;

        MockRequest() {
            mockHeaders = new HashMap<>();
        }

        MockRequest(Map<String, String> mockHeaders) {
            this.mockHeaders = mockHeaders;
        }

        @Override
        public void abortWith(Response response) {
            abortResponse = response;
        }

        public Response getAbortResponse() {
            return abortResponse;
        }

        @Override
        public SecurityContext getSecurityContext() {
            return securityContext;
        }

        @Override
        public void setSecurityContext(SecurityContext context) {
            securityContext = context;
        }

        @Override
        public String getHeaderString(String name) {
            return mockHeaders.get(name);
        }

        @Override
        public Object getProperty(String name) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public Collection<String> getPropertyNames() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public void setProperty(String name, Object object) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public void removeProperty(String name) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public UriInfo getUriInfo() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public void setRequestUri(URI requestUri) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public void setRequestUri(URI baseUri, URI requestUri) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public Request getRequest() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public String getMethod() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public void setMethod(String method) {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public MultivaluedMap<String, String> getHeaders() {
            throw new RuntimeException("Not implemented");
        }


        @Override
        public Date getDate() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public Locale getLanguage() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public int getLength() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public MediaType getMediaType() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public List<MediaType> getAcceptableMediaTypes() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public List<Locale> getAcceptableLanguages() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public Map<String, Cookie> getCookies() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public boolean hasEntity() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public InputStream getEntityStream() {
            throw new RuntimeException("Not implemented");
        }

        @Override
        public void setEntityStream(InputStream input) {
            throw new RuntimeException("Not implemented");
        }


    }

}
