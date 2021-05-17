package org.wahlzeit.database.repository;

import org.junit.Assert;
import org.junit.Test;
import org.wahlzeit.BaseModelTest;
import org.wahlzeit.model.Case;
import org.wahlzeit.model.FlagReason;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.User;


public class CaseRepositoryIT extends BaseModelTest {


    @Test
    public void insertCase() throws Exception {
        // arrange
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        Photo photo = photoFactory.createPhoto(buildMockImageBytes());
        photo = photoRepository.insert(photo);

        // act
        Case photoCase = caseFactory.createPhotoCase(user, photo, FlagReason.COPYRIGHT);
        photoCase = caseRepository.insert(photoCase);

        // assert
        Assert.assertNotNull(photoCase);
        Assert.assertNotNull(photoCase.getId());
        Assert.assertFalse(photoCase.wasDecided());
        Assert.assertEquals(user.getId(), photoCase.getFlaggerId());
        Assert.assertEquals(photo.getId(), photoCase.getPhotoId());
        Assert.assertEquals(FlagReason.COPYRIGHT, photoCase.getReason());
    }


}
