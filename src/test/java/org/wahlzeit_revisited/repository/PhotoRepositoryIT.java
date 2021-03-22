package org.wahlzeit_revisited.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.model.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PhotoRepositoryIT extends BaseModelTest {

    private PhotoRepository repository;
    private PhotoFactory factory;

    @Before
    public void setupDependencies() {
        repository = new PhotoRepository();
        factory = new PhotoFactory();
        repository.factory = factory;
    }

    @Test
    public void test_insertPhoto() throws SQLException {
        // arrange
        Photo expectedPhoto = factory.createPhoto();

        // act
        repository.insert(expectedPhoto);

        // assert
        Assert.assertNotNull(expectedPhoto.getId());
        Assert.assertNotNull(expectedPhoto.getStatus());
    }

    @Test
    public void test_getPhotoById() throws SQLException {
        // arrange
        Photo expectedPhoto = factory.createPhoto();
        expectedPhoto = repository.insert(expectedPhoto);

        // act
        Optional<Photo> actualPhotoOpt = repository.findById(expectedPhoto.getId());

        // assert
        Assert.assertTrue(actualPhotoOpt.isPresent());
        Assert.assertEquals(expectedPhoto.getId(), actualPhotoOpt.get().getId());
        Assert.assertEquals(expectedPhoto.getCreationTime(), actualPhotoOpt.get().getCreationTime());
        Assert.assertEquals(expectedPhoto.getStatus(), actualPhotoOpt.get().getStatus());
        Assert.assertEquals(expectedPhoto.getWidth(), actualPhotoOpt.get().getWidth());
        Assert.assertEquals(expectedPhoto.getHeight(), actualPhotoOpt.get().getHeight());
    }

    @Test
    public void test_updatePhoto() throws SQLException {
        // arrange
        Photo expectedPhoto = factory.createPhoto();
        expectedPhoto = repository.insert(expectedPhoto);
        expectedPhoto.setStatus(PhotoStatus.DELETED);

        // act
        Photo actualPhoto = repository.update(expectedPhoto);

        // assert
        Optional<Photo> actualDbPhotoOpt = repository.findById(expectedPhoto.getId());
        Assert.assertTrue(actualDbPhotoOpt.isPresent());
        Assert.assertEquals(expectedPhoto.getStatus(), actualPhoto.getStatus());
        Assert.assertEquals(expectedPhoto.getStatus(), actualDbPhotoOpt.get().getStatus());
    }

    @Test
    public void test_deletePhoto() throws SQLException {
        // arrange
        Photo expectedPhoto = factory.createPhoto();
        expectedPhoto = repository.insert(expectedPhoto);

        // act
        Photo actualPhoto = repository.delete(expectedPhoto);

        // assert
        Optional<Photo> actualDbPhoto = repository.findById(expectedPhoto.getId());
        Assert.assertNotNull(actualPhoto);
        Assert.assertTrue(actualDbPhoto.isEmpty());
    }

    @Test
    public void test_getPhotos() throws SQLException {
        // arrange
        Photo expectedPhoto = factory.createPhoto();
        repository.insert(expectedPhoto);

        // act
        List<Photo> actualPhotos = repository.findAll();

        // assert
        Assert.assertNotNull(actualPhotos);
        Assert.assertFalse(actualPhotos.isEmpty());
    }

    @Test
    public void test_getPhotoForUser() throws SQLException {
        // arrange
        UserFactory userFactory = new UserFactory();
        UserRepository userRepository = new UserRepository();
        userRepository.factory = userFactory;

        User user = userFactory.createUser();
        user = userRepository.insert(user);

        Photo expectedPhoto = factory.createPhoto(user.getId());
        expectedPhoto = repository.insert(expectedPhoto);

        // act
        List<Photo> actualPhotos = repository.findForUser(user);

        // assert
        Assert.assertNotNull(actualPhotos);
        Assert.assertEquals(1, actualPhotos.size());
        Assert.assertEquals(expectedPhoto.getId(), actualPhotos.get(0).getId());
    }

}
