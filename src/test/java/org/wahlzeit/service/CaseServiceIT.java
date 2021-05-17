package org.wahlzeit.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit.BaseModelTest;
import org.wahlzeit.api.dto.CaseDto;
import org.wahlzeit.model.FlagReason;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.User;

import java.sql.SQLException;

public class CaseServiceIT extends BaseModelTest {

    private User user;
    private Photo photo;

    @Before
    public void setupDependencies() throws Exception {
        user = userFactory.createUser();
        user = userRepository.insert(user);
        photo = photoFactory.createPhoto(buildMockImageBytes());
        photo = photoRepository.insert(photo);
    }

    @Test
    public void test_createCase() throws SQLException {
        // act
        CaseDto actualCaseDto = caseService.createCase(user, photo.getId(), FlagReason.COPYRIGHT.asString());

        // assert
        Assert.assertNotNull(actualCaseDto);
        Assert.assertNotNull(actualCaseDto.getId());
        Assert.assertFalse(actualCaseDto.isWasDecided());
        Assert.assertEquals(user.getId(), actualCaseDto.getFlaggerId());
        Assert.assertEquals(photo.getId(), actualCaseDto.getPhotoId());
        Assert.assertEquals(FlagReason.COPYRIGHT, actualCaseDto.getReason());
    }

    @Test
    public void test_closeCase() throws SQLException {
        // arrange
        CaseDto expectedCaseDto = caseService.createCase(user, photo.getId(), FlagReason.COPYRIGHT.asString());
        System.out.println(expectedCaseDto.getId());

        // act
        CaseDto actualCaseDto = caseService.closeCase(expectedCaseDto.getId());

        // assert
        Assert.assertNotNull(actualCaseDto);
        Assert.assertTrue(actualCaseDto.isWasDecided());
        Assert.assertEquals(expectedCaseDto.getId(), actualCaseDto.getId());
        Assert.assertEquals(user.getId(), actualCaseDto.getFlaggerId());
        Assert.assertEquals(photo.getId(), actualCaseDto.getPhotoId());
        Assert.assertEquals(FlagReason.COPYRIGHT, actualCaseDto.getReason());
    }

}
