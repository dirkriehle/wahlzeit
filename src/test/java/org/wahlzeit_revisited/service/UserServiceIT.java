package org.wahlzeit_revisited.service;

import jakarta.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.dto.UserDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;
import org.wahlzeit_revisited.repository.UserRepository;
import org.wahlzeit_revisited.utils.SysConfig;

import java.sql.SQLException;
import java.util.List;

public class UserServiceIT extends BaseModelTest {

    private UserService service;
    private UserRepository userRepository;

    @Before
    public void setupDependencies() {
        service = new UserService();
        service.factory = new UserFactory();
        service.repository = new UserRepository();
        service.repository.factory = new UserFactory();
        service.transformer = new Transformer();
        service.transformer.config = new SysConfig();

        userRepository = new UserRepository();
        userRepository.factory = new UserFactory();
    }

    @Test
    public void test_getUsers() throws SQLException {
        // execute
        Response response = service.getUsers();

        // validate
        List<UserDto> userDtos = assertSuccessfulResponse(response);
        Assert.assertNotNull(userDtos);
    }

    @Test
    public void test_createUser() throws SQLException {
        // prepare
        String expectedUsername = "TestUser";
        String expectedEmail = buildUniqueEmail("create");
        String expectedPassword = "TestPassword123";

        // execute
        Response response = service.createUser(expectedUsername, expectedEmail, expectedPassword);

        // validate
        UserDto actualUserDto = assertSuccessfulResponse(response);
        Assert.assertEquals(expectedEmail, actualUserDto.getEmail());
        Assert.assertEquals(expectedUsername, actualUserDto.getName());
        Assert.assertEquals(expectedPassword, actualUserDto.getPassword());
    }

    @Test
    public void test_deleteUser() throws SQLException {
        // prepare
        User deleteUser = new UserFactory().createUser();
        deleteUser = userRepository.insert(deleteUser);

        // execute
        Response response = service.deleteUser(deleteUser);

        // validate
        UserDto actualUserDto = assertSuccessfulResponse(response);
        Response loginResponse = service.login(deleteUser.getEmail(), deleteUser.getPassword());
        Assert.assertNotEquals(Response.Status.OK.getStatusCode(), loginResponse.getStatus());
        Assert.assertEquals(deleteUser.getId(), actualUserDto.getId());
        Assert.assertEquals(deleteUser.getName(), actualUserDto.getName());
        Assert.assertEquals(deleteUser.getEmail(), actualUserDto.getEmail());
        Assert.assertEquals(deleteUser.getPassword(), actualUserDto.getPassword());
    }

    @Test
    public void test_login() throws SQLException {
        // prepare
        User expectedUser = new UserFactory().createUser();
        expectedUser = userRepository.insert(expectedUser);

        // execute
        Response response = service.login(expectedUser.getEmail(), expectedUser.getPassword());

        // validate
        UserDto actualUserDto = assertSuccessfulResponse(response);
        Assert.assertEquals(expectedUser.getId(), actualUserDto.getId());
        Assert.assertEquals(expectedUser.getName(), actualUserDto.getName());
        Assert.assertEquals(expectedUser.getEmail(), actualUserDto.getEmail());
        Assert.assertEquals(expectedUser.getPassword(), actualUserDto.getPassword());
    }

    /*
     * helpers
     */

    @SuppressWarnings("unchecked")
    protected <T> T assertSuccessfulResponse(Response response) {
        if (response.getStatus() < 200 || response.getStatus() > 299) {
            Assert.fail("Invalid Statuscode: " + response.getStatus());
        }
        return (T) response.getEntity();
    }
}
