package org.wahlzeit_revisited.service;

import jakarta.ws.rs.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.api.dto.PhotoDto;
import org.wahlzeit_revisited.model.PhotoFactory;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;
import org.wahlzeit_revisited.repository.PhotoRepository;
import org.wahlzeit_revisited.repository.UserRepository;
import org.wahlzeit_revisited.utils.SysConfig;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class PhotoServiceIT extends BaseModelTest {

    private PhotoService service;
    private UserRepository userRepository;
    private UserFactory userFactory;

    @Before
    public void setupDependencies() {
        service = new PhotoService();
        service.config = new SysConfig();
        service.factory = new PhotoFactory();
        service.repository = new PhotoRepository();
        service.repository.factory = new PhotoFactory();
        service.transformer = new Transformer();
        service.userRepository = new UserRepository();
        service.userRepository.factory = new UserFactory();

        userFactory = new UserFactory();
        userRepository = new UserRepository();
        userRepository.factory = new UserFactory();
    }

    @Test
    public void test_getRandomPhoto() throws SQLException, IOException {
        // arrange
        service.setupInitialPhotos();

        // act
        PhotoDto actualPhoto = service.getRandomPhoto();

        // assert - doesn't throw an exception
        Assert.assertNotNull(actualPhoto);
        Assert.assertNotNull(actualPhoto.getPath());
    }

    @Test
    public void test_getPhotos() throws SQLException {
        // act
        List<PhotoDto> photoDtos = service.getPhotos();

        // assert
        Assert.assertNotNull(photoDtos);
    }

    @Test
    public void test_addPhoto() throws SQLException, IOException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        Set<String> expectedTags = Set.of("tag1", "tag2");

        // act
        PhotoDto responseDto = service.addPhoto(user, buildMockImageBytes(), expectedTags);

        // assert
        Assert.assertNotNull(responseDto);
        Assert.assertEquals(expectedTags.size(), responseDto.getTags().size());
        for (String tag : expectedTags) {
            Assert.assertTrue(responseDto.getTags().contains(tag));
        }
    }

    @Test
    public void test_getPhotosNoUserByTags() throws SQLException, IOException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        Set<String> expectedTags = Set.of("tag1", "tag2");
        PhotoDto expectedPhoto = service.addPhoto(user, buildMockImageBytes(), expectedTags);

        // act
        List<PhotoDto> responseDto = service.getFilteredPhotos(null, Set.of("tag1", "tAg 2"));

        // assert
        final Long expectedPhotoId = expectedPhoto.getId();
        PhotoDto actualPhoto = responseDto.stream().filter((p) -> p.getId().equals(expectedPhotoId)).findFirst().get();
        Assert.assertEquals(user.getId(), actualPhoto.getUserId());
        Assert.assertEquals(expectedTags.size(), actualPhoto.getTags().size());
        for (String expectedTag : expectedTags) {
            Assert.assertTrue(actualPhoto.getTags().contains(expectedTag));
        }
    }

    @Test
    public void test_getPhotosWithUserByTags() throws SQLException, IOException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        Set<String> expectedTags = Set.of("tag1", "tag2");
        PhotoDto expectedPhoto = service.addPhoto(user, buildMockImageBytes(), expectedTags);

        // act
        List<PhotoDto> responseDto = service.getFilteredPhotos(user.getId(), Set.of("tag1", "tAg 2"));

        // assert
        Assert.assertEquals(1, responseDto.size());
        Assert.assertEquals(expectedPhoto.getId(), responseDto.get(0).getId());
    }

    @Test
    public void test_getPhoto() throws SQLException, IOException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedDto = service.addPhoto(user, buildMockImageBytes(), Set.of());

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
    public void test_getUserPhotos() throws SQLException, IOException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedDto = service.addPhoto(user, buildMockImageBytes(), Set.of());

        // act
        List<PhotoDto> responseDto = service.getFilteredPhotos(user.getId(), Set.of());

        // assert
        Assert.assertEquals(1, responseDto.size());
        Assert.assertEquals(user.getId(), responseDto.get(0).getUserId());
        Assert.assertEquals(expectedDto.getId(), responseDto.get(0).getId());
        Assert.assertEquals(expectedDto.getPath(), responseDto.get(0).getPath());
        Assert.assertEquals(expectedDto.getWidth(), responseDto.get(0).getWidth());
        Assert.assertEquals(expectedDto.getHeight(), responseDto.get(0).getHeight());
    }

    @Test(expected = NotFoundException.class)
    public void test_removePhoto() throws SQLException, IOException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedDto = service.addPhoto(user, buildMockImageBytes(), Set.of());

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

    @Test
    public void test_praisePhoto() throws SQLException, IOException {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedDto = service.addPhoto(user, buildMockImageBytes(), Set.of());

        // act
        PhotoDto responseDto = service.praisePhoto(expectedDto.getId(), 1);

        // assert
        Assert.assertTrue(responseDto.getPraise() < 6);
        Assert.assertTrue(responseDto.getPraise() > 4);
    }
}
