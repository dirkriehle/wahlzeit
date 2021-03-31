package org.wahlzeit_revisited.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public void test_insertPhoto() throws SQLException, IOException {
        // arrange
        Photo expectedPhoto = factory.createPhoto(buildMockImageBytes());

        // act
        expectedPhoto = repository.insert(expectedPhoto);

        // assert
        Assert.assertNotNull(expectedPhoto.getId());
        Assert.assertNotNull(expectedPhoto.getStatus());
        Assert.assertNull(expectedPhoto.getOwnerId());
    }

    @Test
    public void test_getPhotoById() throws SQLException, IOException {
        // arrange
        byte[] expectedData = buildMockImageBytes(200, 192);
        Photo expectedPhoto = factory.createPhoto(expectedData);
        expectedPhoto = repository.insert(expectedPhoto);

        // act
        Optional<Photo> actualPhotoOpt = repository.findById(expectedPhoto.getId());

        // assert
        Assert.assertTrue(actualPhotoOpt.isPresent());
        Assert.assertEquals(expectedPhoto.getId(), actualPhotoOpt.get().getId());
        Assert.assertEquals(expectedPhoto.getCreationTime(), actualPhotoOpt.get().getCreationTime());
        Assert.assertArrayEquals(expectedData, actualPhotoOpt.get().getData());
        Assert.assertEquals(expectedPhoto.getStatus(), actualPhotoOpt.get().getStatus());
        Assert.assertEquals(expectedPhoto.getWidth(), actualPhotoOpt.get().getWidth());
        Assert.assertEquals(expectedPhoto.getHeight(), actualPhotoOpt.get().getHeight());
    }

    @Test
    public void test_updatePhoto() throws SQLException, IOException {
        // arrange
        Photo expectedPhoto = factory.createPhoto(buildMockImageBytes());
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
    public void test_deletePhoto() throws SQLException, IOException {
        // arrange
        Photo expectedPhoto = factory.createPhoto(buildMockImageBytes());
        expectedPhoto = repository.insert(expectedPhoto);

        // act
        Photo actualPhoto = repository.delete(expectedPhoto);

        // assert
        Optional<Photo> actualDbPhoto = repository.findById(expectedPhoto.getId());
        Assert.assertNotNull(actualPhoto);
        Assert.assertTrue(actualDbPhoto.isEmpty());
    }

    @Test
    public void test_getPhotos() throws SQLException, IOException {
        // arrange
        Photo expectedPhoto = factory.createPhoto(buildMockImageBytes());
        repository.insert(expectedPhoto);

        // act
        List<Photo> actualPhotos = repository.findAll();

        // assert
        Assert.assertNotNull(actualPhotos);
        Assert.assertFalse(actualPhotos.isEmpty());
    }

    @Test
    public void test_getPhotoForUser() throws SQLException, IOException {
        // arrange
        UserFactory userFactory = new UserFactory();
        UserRepository userRepository = new UserRepository();
        userRepository.factory = userFactory;

        User user = userFactory.createUser();
        user = userRepository.insert(user);

        Photo expectedPhoto = factory.createPhoto(user.getId(), buildMockImageBytes(), Set.of());
        expectedPhoto = repository.insert(expectedPhoto);

        // act
        List<Photo> actualPhotos = repository.findForUser(user);

        // assert
        Assert.assertNotNull(actualPhotos);
        Assert.assertEquals(1, actualPhotos.size());
        Assert.assertEquals(user.getId(), actualPhotos.get(0).getOwnerId());
        Assert.assertEquals(expectedPhoto.getId(), actualPhotos.get(0).getId());
    }

    @Test
    public void test_getPhotosForTags() throws SQLException, IOException {
        // arrange
        Set<String> expectedTags = Set.of("captainamerica", "batman", "poisonivy");
        Photo expectedPhoto = factory.createPhoto(buildMockImageBytes(), expectedTags);
        expectedPhoto = repository.insert(expectedPhoto);

        // execute
        List<Photo> allTags = repository.findForTags(expectedTags);
        List<Photo> firstTag = repository.findForTags(Set.of("captainamerica"));
        List<Photo> middleTag = repository.findForTags(Set.of("batman"));
        List<Photo> lastTag = repository.findForTags(Set.of("poisonivy"));

        // assert
        final Long expectedId = expectedPhoto.getId();
        Assert.assertTrue(allTags.stream().anyMatch(p -> p.getId().equals(expectedId)));
        Assert.assertTrue(firstTag.stream().anyMatch(p -> p.getId().equals(expectedId)));
        Assert.assertTrue(middleTag.stream().anyMatch(p -> p.getId().equals(expectedId)));
        Assert.assertTrue(lastTag.stream().anyMatch(p -> p.getId().equals(expectedId)));
    }

    @Test
    public void test_getEmptyTag() throws SQLException, IOException {
        // arrange
        Photo expectedPhoto = factory.createPhoto(buildMockImageBytes(), Set.of());
        expectedPhoto = repository.insert(expectedPhoto);

        // act
        Photo actualPhoto = repository.findById(expectedPhoto.getId()).get();

        // assert
        Assert.assertNotNull(actualPhoto);
        Assert.assertTrue(actualPhoto.getTags().isEmpty());
    }

    @Test
    public void test_getEscapedTags() throws SQLException, IOException {
        // arrange
        Set<String> tags = Set.of("Captain America", "escaped,S3t", "");
        Set<String> expectedTags = Set.of("captainamerica", "escapeds3t");
        Photo expectedPhoto = factory.createPhoto(buildMockImageBytes(), tags);
        expectedPhoto = repository.insert(expectedPhoto);

        // act
        Photo actualPhoto = repository.findById(expectedPhoto.getId()).get();

        // assert
        Assert.assertNotNull(actualPhoto);
        Assert.assertEquals(expectedTags.size(), actualPhoto.getTags().size());
        actualPhoto.getTags().forEach(System.out::println);
        for (String expectedTag : expectedTags) {
            Assert.assertTrue(actualPhoto.getTags().contains(expectedTag));
        }
    }

}
