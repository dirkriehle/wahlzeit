package org.wahlzeit_revisited.service;

import jakarta.ws.rs.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.PhotoFactory;
import org.wahlzeit_revisited.model.PhotoStatus;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;
import org.wahlzeit_revisited.repository.PhotoRepository;
import org.wahlzeit_revisited.repository.UserRepository;

import java.util.List;
import java.util.Set;

public class PhotoFlagServiceIT extends BaseModelTest {

    private PhotoFlagService service;
    private PhotoService photoService;
    private UserRepository userRepository;
    private UserFactory userFactory;

    @Before
    public void setupDependencies() {
        service = new PhotoFlagService();
        service.repository = new PhotoRepository();
        service.repository.factory = new PhotoFactory();
        service.transformer = new Transformer();

        photoService = new PhotoService();
        photoService.factory = new PhotoFactory();
        photoService.repository = new PhotoRepository();
        photoService.repository.factory = new PhotoFactory();
        photoService.transformer = new Transformer();

        userFactory = new UserFactory();
        userRepository = new UserRepository();
        userRepository.factory = new UserFactory();
    }

    @Test(expected = NotFoundException.class)
    public void test_setFlaggedPhotoStillVisible() throws Exception {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedPhoto = photoService.addPhoto(user, buildMockImageBytes(), Set.of());

        // act
        service.setPhotoStatus(user, expectedPhoto.getId(), PhotoStatus.FLAGGED.asString());

        // assert
        PhotoDto actualPhoto = service.getFlaggedPhoto(expectedPhoto.getId());
        Assert.assertEquals(expectedPhoto.getId(), actualPhoto.getId());
        photoService.getPhoto(expectedPhoto.getId());
    }

    @Test
    public void test_getFlaggedPhotos() throws Exception {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedPhoto = photoService.addPhoto(user, buildMockImageBytes(), Set.of());
        service.setPhotoStatus(user, expectedPhoto.getId(), PhotoStatus.FLAGGED.asString());

        // act
        List<PhotoDto> actualPhotos = service.getFlaggedPhotos();

        // assert
        final long expectedPhotoId = expectedPhoto.getId();
        Assert.assertTrue(actualPhotos.stream().anyMatch((actualPhoto) -> actualPhoto.getId().equals(expectedPhotoId)));
    }

}
