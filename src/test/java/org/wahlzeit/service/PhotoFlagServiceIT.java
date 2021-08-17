package org.wahlzeit.service;

import jakarta.ws.rs.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.wahlzeit.BaseModelTest;
import org.wahlzeit.api.dto.PhotoDto;
import org.wahlzeit.model.PhotoStatus;
import org.wahlzeit.model.User;

import java.util.List;
import java.util.Set;

public class PhotoFlagServiceIT extends BaseModelTest {


    @Test(expected = NotFoundException.class)
    public void test_setFlaggedPhotoStillVisible() throws Exception {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedPhoto = photoService.addPhoto(user, buildMockImageBytes(), Set.of());

        // act
        photoFlagService.setPhotoStatus(user, expectedPhoto.getId(), PhotoStatus.FLAGGED.asString());

        // assert
        PhotoDto actualPhoto = photoFlagService.getFlaggedPhoto(expectedPhoto.getId());
        Assert.assertEquals(expectedPhoto.getId(), actualPhoto.getId());
        photoService.getPhoto(expectedPhoto.getId());
    }

    @Test
    public void test_getFlaggedPhotos() throws Exception {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedPhoto = photoService.addPhoto(user, buildMockImageBytes(), Set.of());
        photoFlagService.setPhotoStatus(user, expectedPhoto.getId(), PhotoStatus.FLAGGED.asString());

        // act
        List<PhotoDto> actualPhotos = photoFlagService.getFlaggedPhotos();

        // assert
        final long expectedPhotoId = expectedPhoto.getId();
        Assert.assertTrue(actualPhotos.stream().anyMatch((actualPhoto) -> actualPhoto.getId().equals(expectedPhotoId)));
    }

}
