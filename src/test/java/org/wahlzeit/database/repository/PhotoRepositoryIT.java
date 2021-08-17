package org.wahlzeit.database.repository;

import org.junit.Assert;
import org.junit.Test;
import org.wahlzeit.BaseModelTest;
import org.wahlzeit.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PhotoRepositoryIT extends BaseModelTest {

    @Test
    public void test_insertPhoto() throws SQLException, IOException {
        // arrange
        Photo expectedPhoto = photoFactory.createPhoto(buildMockImageBytes());

        // act
        expectedPhoto = photoRepository.insert(expectedPhoto);

        // assert
        Assert.assertNotNull(expectedPhoto.getId());
        Assert.assertNotNull(expectedPhoto.getStatus());
        Assert.assertNull(expectedPhoto.getOwnerId());
    }

    @Test
    public void test_getPhotoById() throws SQLException, IOException {
        // arrange
        byte[] expectedData = buildMockImageBytes(200, 192);
        Photo expectedPhoto = photoFactory.createPhoto(expectedData);
        expectedPhoto = photoRepository.insert(expectedPhoto);

        // act
        Optional<Photo> actualPhotoOpt = photoRepository.findById(expectedPhoto.getId());

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
        Photo expectedPhoto = photoFactory.createPhoto(buildMockImageBytes());
        expectedPhoto = photoRepository.insert(expectedPhoto);
        expectedPhoto.setStatus(PhotoStatus.DELETED);

        // act
        Photo actualPhoto = photoRepository.update(expectedPhoto);

        // assert
        Optional<Photo> actualDbPhotoOpt = photoRepository.findById(expectedPhoto.getId());
        Assert.assertTrue(actualDbPhotoOpt.isPresent());
        Assert.assertEquals(expectedPhoto.getStatus(), actualPhoto.getStatus());
        Assert.assertEquals(expectedPhoto.getStatus(), actualDbPhotoOpt.get().getStatus());
    }

    @Test
    public void test_deletePhoto() throws SQLException, IOException {
        // arrange
        Photo expectedPhoto = photoFactory.createPhoto(buildMockImageBytes());
        expectedPhoto = photoRepository.insert(expectedPhoto);

        // act
        Photo actualPhoto = photoRepository.delete(expectedPhoto);

        // assert
        Optional<Photo> actualDbPhoto = photoRepository.findById(expectedPhoto.getId());
        Assert.assertNotNull(actualPhoto);
        Assert.assertTrue(actualDbPhoto.isEmpty());
    }

    @Test
    public void test_getPhotos() throws SQLException, IOException {
        // arrange
        Photo expectedPhoto = photoFactory.createPhoto(buildMockImageBytes());
        photoRepository.insert(expectedPhoto);

        // act
        List<Photo> actualPhotos = photoRepository.findAll();

        // assert
        Assert.assertNotNull(actualPhotos);
        Assert.assertFalse(actualPhotos.isEmpty());
    }

    @Test
    public void test_getPhotoForUser() throws SQLException, IOException {
        // arrange
        User user = createUser();
        Photo expectedPhoto = photoFactory.createPhoto(user, buildMockImageBytes(), new Tags());
        expectedPhoto = photoRepository.insert(expectedPhoto);

        // act
        List<Photo> actualPhotos = photoRepository.findWithFilter(new PhotoFilter(user, new Tags()));

        // assert
        Assert.assertNotNull(actualPhotos);
        Assert.assertEquals(1, actualPhotos.size());
        Assert.assertEquals(user.getId(), actualPhotos.get(0).getOwnerId());
        Assert.assertEquals(expectedPhoto.getId(), actualPhotos.get(0).getId());
    }

    @Test
    public void test_getPhotosForNoUserButTags() throws SQLException, IOException {
        // arrange
        Tags expectedTags = new Tags(Set.of("captainamerica", "batman", "poisonivy"));
        Tags firstTags = new Tags(Set.of("captainamerica"));
        Tags middleTags = new Tags(Set.of("batman"));
        Tags lastTags = new Tags(Set.of("poisonivy"));
        Photo expectedPhoto = photoFactory.createPhoto(buildMockImageBytes(), expectedTags);
        expectedPhoto = photoRepository.insert(expectedPhoto);

        // execute
        List<Photo> allTags = photoRepository.findWithFilter(new PhotoFilter(null, expectedTags));
        List<Photo> firstTag = photoRepository.findWithFilter(new PhotoFilter(null, firstTags));
        List<Photo> middleTag = photoRepository.findWithFilter(new PhotoFilter(null, middleTags));
        List<Photo> lastTag = photoRepository.findWithFilter(new PhotoFilter(null, lastTags));

        // assert
        final Long expectedId = expectedPhoto.getId();
        Assert.assertTrue(allTags.stream().anyMatch(p -> p.getId().equals(expectedId)));
        Assert.assertTrue(firstTag.stream().anyMatch(p -> p.getId().equals(expectedId)));
        Assert.assertTrue(middleTag.stream().anyMatch(p -> p.getId().equals(expectedId)));
        Assert.assertTrue(lastTag.stream().anyMatch(p -> p.getId().equals(expectedId)));
    }

    @Test
    public void test_getPhotosForUserButNoTags() throws SQLException, IOException {
        User user = createUser();
        Photo expectedPhoto = photoFactory.createPhoto(user, buildMockImageBytes(), new Tags());
        expectedPhoto = photoRepository.insert(expectedPhoto);

        // act
        List<Photo> actualPhotos = photoRepository.findWithFilter(new PhotoFilter(user, new Tags()));

        // assert
        Assert.assertEquals(1, actualPhotos.size());
        Assert.assertEquals(expectedPhoto.getId(), actualPhotos.get(0).getId());
    }

    @Test
    public void test_getPhotosForUserAndTags() throws SQLException, IOException {
        // arrange
        Tags tags = new Tags(Set.of("batman"));
        User user = createUser();
        Photo expectedPhoto = photoFactory.createPhoto(user, buildMockImageBytes(), tags);
        expectedPhoto = photoRepository.insert(expectedPhoto);

        // act
        List<Photo> actualPhotos = photoRepository.findWithFilter(new PhotoFilter(user, tags));

        // assert
        Assert.assertEquals(1, actualPhotos.size());
        Assert.assertEquals(expectedPhoto.getId(), actualPhotos.get(0).getId());
    }

    @Test
    public void test_getEmptyTag() throws SQLException, IOException {
        // arrange
        Photo expectedPhoto = photoFactory.createPhoto(buildMockImageBytes(), new Tags());
        expectedPhoto = photoRepository.insert(expectedPhoto);

        // act
        Photo actualPhoto = photoRepository.findById(expectedPhoto.getId()).get();

        // assert
        Assert.assertNotNull(actualPhoto);
        Assert.assertTrue(actualPhoto.getTags().isEmpty());
    }

    @Test
    public void test_getEscapedTags() throws SQLException, IOException {
        // arrange
        Tags tags = new Tags(Set.of("Captain America", "escaped,S3t", ""));
        Tags expectedTags = new Tags(Set.of("captainamerica", "escapeds3t"));
        Photo expectedPhoto = photoFactory.createPhoto(buildMockImageBytes(), tags);
        expectedPhoto = photoRepository.insert(expectedPhoto);

        // act
        Photo actualPhoto = photoRepository.findById(expectedPhoto.getId()).get();

        // assert
        Assert.assertNotNull(actualPhoto);
        Assert.assertEquals(expectedTags.getSize(), actualPhoto.getTags().size());
        actualPhoto.getTags().forEach(System.out::println);
        for (String expectedTag : expectedTags.getTags()) {
            Assert.assertTrue(actualPhoto.getTags().contains(expectedTag));
        }
    }

    /*
     * Helpers
     */

    private User createUser() throws SQLException {
        UserFactory userFactory = new UserFactory();
        UserRepository userRepository = new UserRepository();
        userRepository.factory = userFactory;

        User user = userFactory.createUser();
        return userRepository.insert(user);
    }

}
