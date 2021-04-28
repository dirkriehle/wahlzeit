package org.wahlzeit_revisited.service;

import jakarta.ws.rs.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.auth.AccessRights;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.PhotoFactory;
import org.wahlzeit_revisited.model.PhotoStatus;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;
import org.wahlzeit_revisited.repository.PhotoRepository;
import org.wahlzeit_revisited.repository.UserRepository;
import org.wahlzeit_revisited.utils.SysConfig;

import java.util.List;
import java.util.Set;

public class PhotoCaseServiceIT extends BaseModelTest {

    private PhotoCaseService service;
    private PhotoService photoService;
    private UserRepository userRepository;
    private UserFactory userFactory;

    @Before
    public void setupDependencies() {
        service = new PhotoCaseService();
        service.repository = new PhotoRepository();
        service.repository.factory = new PhotoFactory();
        service.transformer = new Transformer();

        photoService = new PhotoService();
        photoService.factory = new PhotoFactory();
        photoService.repository = new PhotoRepository();
        photoService.repository.factory = new PhotoFactory();
        photoService.transformer = new Transformer();
        photoService.transformer.config = new SysConfig();

        userFactory = new UserFactory();
        userRepository = new UserRepository();
        userRepository.factory = new UserFactory();
    }

    @Test(expected = NotFoundException.class)
    public void test_setFlaggedPhoto() throws Exception {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto photo = photoService.addPhoto(user, buildMockImageBytes(), Set.of());

        // act
        service.setPhotoStatus(photo.getId(), PhotoStatus.FLAGGED.asString());

        // assert
        photoService.getPhoto(photo.getId(), AccessRights.MODERATOR);
        photoService.getPhoto(photo.getId(), AccessRights.USER);
    }

    @Test
    public void test_getFlaggedPhotos() throws Exception {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedPhoto = photoService.addPhoto(user, buildMockImageBytes(), Set.of());
        service.setPhotoStatus(expectedPhoto.getId(), PhotoStatus.FLAGGED.asString());

        // act
        List<PhotoDto> actualPhotos = photoService.getPhotos();

        // assert
        final long expectedPhotoId = expectedPhoto.getId();
        Assert.assertTrue(actualPhotos.stream().anyMatch((actualPhoto) -> actualPhoto.getId().equals(expectedPhotoId)));
    }

}
