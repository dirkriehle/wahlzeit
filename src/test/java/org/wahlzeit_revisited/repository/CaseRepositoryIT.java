package org.wahlzeit_revisited.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.model.*;


public class CaseRepositoryIT extends BaseModelTest {

    UserFactory userFactory;
    UserRepository userRepository;
    PhotoFactory photoFactory;
    PhotoRepository photoRepository;

    CaseFactory factory;
    CaseRepository repository;

    @Before
    public void setupDependencies() {
        userFactory = new UserFactory();
        userRepository = new UserRepository();
        userRepository.factory = new UserFactory();
        photoFactory = new PhotoFactory();
        photoRepository = new PhotoRepository();

        factory = new CaseFactory();
        repository = new CaseRepository();
        repository.factory = new CaseFactory();
    }

    @Test
    public void insertCase() throws Exception {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        Photo photo = photoFactory.createPhoto(buildMockImageBytes());
        photo = photoRepository.insert(photo);

        // act
        Case photoCase = factory.createPhotoCase(user, photo, FlagReason.COPYRIGHT);
        photoCase = repository.insert(photoCase);

        // assert
        Assert.assertNotNull(photoCase);
        Assert.assertNotNull(photoCase.getId());
        Assert.assertFalse(photoCase.wasDecided());
        Assert.assertEquals(user.getId(), photoCase.getFlaggerId());
        Assert.assertEquals(photo.getId(), photoCase.getPhotoId());
        Assert.assertEquals(FlagReason.COPYRIGHT, photoCase.getReason());
    }


}
