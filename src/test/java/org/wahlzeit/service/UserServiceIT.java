package org.wahlzeit.service;

import jakarta.ws.rs.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.wahlzeit.BaseModelTest;
import org.wahlzeit.api.dto.UserDto;
import org.wahlzeit.model.User;
import org.wahlzeit.model.UserFactory;

import java.sql.SQLException;
import java.util.List;

public class UserServiceIT extends BaseModelTest {


    @Test
    public void test_getUsers() throws SQLException {
        // act
        List<UserDto> userDtos = userService.getUsers();

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
        UserDto actualUserDto = userService.createUser(expectedUsername, expectedEmail, expectedPassword);

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
        UserDto expectedUserDto = userService.createUser(expectedUsername, expectedEmail, expectedPassword);

        // act
        UserDto actualUserDto = userService.getUser(expectedUserDto.getId());

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
        UserDto actualUserDto = userService.deleteUser(deleteUser);

        // assert
        userService.login(deleteUser.getEmail(), deleteUser.getPassword());

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
        UserDto actualUserDto = userService.login(expectedUser.getEmail(), expectedUser.getPassword());

        // assert
        Assert.assertEquals(expectedUser.getId(), actualUserDto.getId());
        Assert.assertEquals(expectedUser.getName(), actualUserDto.getName());
        Assert.assertEquals(expectedUser.getEmail(), actualUserDto.getEmail());
    }

}
