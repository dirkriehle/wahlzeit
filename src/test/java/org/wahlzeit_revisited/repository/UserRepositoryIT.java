package org.wahlzeit_revisited.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepositoryIT extends BaseModelTest {

    private static final String NAME = "Default Username";
    private static final String PWD = "SimplePassword";

    private UserRepository repository;
    private UserFactory factory;

    @Before
    public void setupDependencies() {
        factory = new UserFactory();
        repository = new UserRepository();
        repository.factory = factory;
    }

    @Test
    public void test_insertUser() throws SQLException {
        // arrange
        User expectedUser = factory.createUser(NAME, buildUniqueEmail("insert"), PWD, AccessRights.USER);

        // act
        User actualUser = repository.insert(expectedUser);

        // assert
        Assert.assertNotNull(actualUser.getId());
        Assert.assertEquals(expectedUser.getName(), actualUser.getName());
        Assert.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assert.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    public void test_getUserById() throws SQLException {
        // arrange
        User expectedUser = factory.createUser(NAME, buildUniqueEmail("getId"), PWD, AccessRights.USER);
        expectedUser = repository.insert(expectedUser);

        // act
        Optional<User> actualUserOpt = repository.findById(expectedUser.getId());

        // assert
        Assert.assertTrue(actualUserOpt.isPresent());
        Assert.assertEquals(expectedUser.getId(), actualUserOpt.get().getId());
        Assert.assertEquals(expectedUser.getCreationTime(), actualUserOpt.get().getCreationTime());
        Assert.assertEquals(expectedUser.getName(), actualUserOpt.get().getName());
        Assert.assertEquals(expectedUser.getPassword(), actualUserOpt.get().getPassword());
        Assert.assertEquals(expectedUser.getRights(), actualUserOpt.get().getRights());
    }

    @Test
    public void test_updateUser() throws SQLException {
        // arrange
        User expectedUser = factory.createUser(NAME, buildUniqueEmail("update"), PWD, AccessRights.USER);
        repository.insert(expectedUser);
        expectedUser.setName("Other Name");

        // act
        User actualUser = repository.update(expectedUser);

        // assert
        Optional<User> actualDbUserOpt = repository.findById(actualUser.getId());
        Assert.assertTrue(actualDbUserOpt.isPresent());
        Assert.assertEquals(expectedUser.getName(), actualUser.getName());
        Assert.assertEquals(expectedUser.getName(), actualDbUserOpt.get().getName());
    }

    @Test
    public void test_deleteUser() throws SQLException {
        // arrange
        User expectedUser = factory.createUser(NAME, buildUniqueEmail("del"), PWD, AccessRights.USER);
        expectedUser = repository.insert(expectedUser);

        // act
        User actualUser = repository.delete(expectedUser);

        // assert
        Optional<User> actualDbUser = repository.findById(expectedUser.getId());
        Assert.assertNotNull(actualUser);
        Assert.assertTrue(actualDbUser.isEmpty());
    }

    @Test
    public void test_getUsers() throws SQLException {
        // arrange
        User expectedUser = factory.createUser(NAME, buildUniqueEmail("get"), PWD, AccessRights.USER);
        repository.insert(expectedUser);

        // act
        List<User> actualUsers = repository.findAll();

        // assert
        Assert.assertNotNull(actualUsers);
        Assert.assertFalse(actualUsers.isEmpty());
    }

    @Test
    public void test_getUserForAuth() throws SQLException {
        // arrange
        String expectedEmail = buildUniqueEmail("loginTest@fau.de");
        String expectedPassword = "StrongPassword123";
        User expectedUser = factory.createUser(NAME, expectedEmail, expectedPassword, AccessRights.USER);
        expectedUser = repository.insert(expectedUser);

        // act
        Optional<User> actualUserOpt = repository.findByEmailPassword(expectedEmail, expectedPassword);

        // assert
        Assert.assertTrue(actualUserOpt.isPresent());
        Assert.assertEquals(expectedUser.getId(), actualUserOpt.get().getId());
    }

}
