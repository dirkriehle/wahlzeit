package org.wahlzeit_revisited.service;

import jakarta.ws.rs.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.PhotoFactory;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;
import org.wahlzeit_revisited.repository.PhotoRepository;
import org.wahlzeit_revisited.repository.UserRepository;
import org.wahlzeit_revisited.utils.SysConfig;

import java.sql.SQLException;
import java.util.List;

public class PhotoServiceIT extends BaseModelTest {

    private PhotoService service;
    private UserRepository userRepository;
    private UserFactory userFactory;

    @Before
    public void setupDependencies() {
        service = new PhotoService();
        service.factory = new PhotoFactory();
        service.repository = new PhotoRepository();
        service.repository.factory = new PhotoFactory();
        service.transformer = new Transformer();
        service.transformer.config = new SysConfig();

        userFactory = new UserFactory();
        userRepository = new UserRepository();
        userRepository.factory = new UserFactory();
    }

    @Test
    public void test_getPhotos() throws SQLException {
        // act
        List<PhotoDto> photoDtos = service.getPhotos();

        // assert
        Assert.assertNotNull(photoDtos);
    }

    @Test
    public void test_addPhoto() throws SQLException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);

        // act
        PhotoDto responseDto = service.addPhoto(user, new byte[]{});

        // assert
        Assert.assertNotNull(responseDto);
    }

    @Test
    public void test_getPhoto() throws SQLException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedDto = service.addPhoto(user, new byte[]{});

        // act
        PhotoDto actualDto = service.getPhoto(expectedDto.getId());

        // assert
        Assert.assertEquals(user.getId(), actualDto.getUserId());
        Assert.assertEquals(expectedDto.getId(), actualDto.getId());
        Assert.assertEquals(expectedDto.getPath(), actualDto.getPath());
        Assert.assertEquals(expectedDto.getWidth(), actualDto.getWidth());
        Assert.assertEquals(expectedDto.getHeight(), actualDto.getHeight());
    }

    @Test
    public void test_getUserPhotos() throws SQLException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedDto = service.addPhoto(user, new byte[]{});

        // act
        List<PhotoDto> responseDto = service.getUserPhotos(user.getId());

        // assert
        Assert.assertEquals(1, responseDto.size());
        Assert.assertEquals(user.getId(), responseDto.get(0).getUserId());
        Assert.assertEquals(expectedDto.getId(), responseDto.get(0).getId());
        Assert.assertEquals(expectedDto.getPath(), responseDto.get(0).getPath());
        Assert.assertEquals(expectedDto.getWidth(), responseDto.get(0).getWidth());
        Assert.assertEquals(expectedDto.getHeight(), responseDto.get(0).getHeight());
    }

    @Test(expected = NotFoundException.class)
    public void test_removePhoto() throws SQLException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedDto = service.addPhoto(user, new byte[]{});

        // act
        PhotoDto responseDto = service.removePhoto(user, expectedDto.getId());

        // assert
        service.getPhoto(expectedDto.getId());
        Assert.assertEquals(user.getId(), responseDto.getUserId());
        Assert.assertEquals(expectedDto.getId(), responseDto.getId());
        Assert.assertEquals(expectedDto.getPath(), responseDto.getPath());
        Assert.assertEquals(expectedDto.getWidth(), responseDto.getWidth());
        Assert.assertEquals(expectedDto.getHeight(), responseDto.getHeight());
    }

}
