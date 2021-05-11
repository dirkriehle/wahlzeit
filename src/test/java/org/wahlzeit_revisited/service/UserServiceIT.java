package org.wahlzeit_revisited.service;

import jakarta.ws.rs.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.api.dto.UserDto;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;
import org.wahlzeit_revisited.database.repository.UserRepository;

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

        userRepository = new UserRepository();
        userRepository.factory = new UserFactory();
    }

    @Test
    public void test_getUsers() throws SQLException {
        // act
        List<UserDto> userDtos = service.getUsers();

        // assert
        Assert.assertNotNull(userDtos);
    }

    @Test
    public void test_createUser() throws SQLException {
        // arrange
        String expectedUsername = buildUniqueName("TestUser");
        String expectedEmail = buildUniqueEmail("create");
        String expectedPassword = "TestPassword123";

        // act
        UserDto actualUserDto = service.createUser(expectedUsername, expectedEmail, expectedPassword);

        // assert
        Assert.assertEquals(expectedEmail, actualUserDto.getEmail());
        Assert.assertEquals(expectedUsername, actualUserDto.getName());
    }

    @Test
    public void test_getUser() throws SQLException {
        // arrange
        String expectedUsername = buildUniqueName("TestUser");
        String expectedEmail = buildUniqueEmail("create");
        String expectedPassword = "TestPassword123";
        UserDto expectedUserDto = service.createUser(expectedUsername, expectedEmail, expectedPassword);

        // act
        UserDto actualUserDto = service.getUser(expectedUserDto.getId());

        // assert
        Assert.assertEquals(expectedUserDto.getId(), actualUserDto.getId());
        Assert.assertEquals(expectedEmail, actualUserDto.getEmail());
        Assert.assertEquals(expectedUsername, actualUserDto.getName());
    }

    @Test(expected = NotFoundException.class)
    public void test_deleteUser() throws SQLException {
        // arrange
        User deleteUser = new UserFactory().createUser();
        deleteUser = userRepository.insert(deleteUser);

        // act
        UserDto actualUserDto = service.deleteUser(deleteUser);

        // assert
        service.login(deleteUser.getEmail(), deleteUser.getPassword());

        Assert.assertEquals(deleteUser.getId(), actualUserDto.getId());
        Assert.assertEquals(deleteUser.getName(), actualUserDto.getName());
        Assert.assertEquals(deleteUser.getEmail(), actualUserDto.getEmail());
    }

    @Test
    public void test_login() throws SQLException {
        // arrange
        User expectedUser = new UserFactory().createUser();
        expectedUser = userRepository.insert(expectedUser);

        // act
        UserDto actualUserDto = service.login(expectedUser.getEmail(), expectedUser.getPassword());

        // assert
        Assert.assertEquals(expectedUser.getId(), actualUserDto.getId());
        Assert.assertEquals(expectedUser.getName(), actualUserDto.getName());
        Assert.assertEquals(expectedUser.getEmail(), actualUserDto.getEmail());
    }

}
