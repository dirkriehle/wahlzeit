package org.wahlzeit_revisited.service;

import jakarta.inject.Inject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.api.dto.CaseDto;
import org.wahlzeit_revisited.model.*;
import org.wahlzeit_revisited.api.repository.CaseRepository;
import org.wahlzeit_revisited.api.repository.PhotoRepository;
import org.wahlzeit_revisited.api.repository.UserRepository;

import java.sql.SQLException;

public class CaseServiceIT extends BaseModelTest {

    @Inject
    CaseService service;

    private User user;
    private Photo photo;

    @Before
    public void setupDependencies() throws Exception {
        service = new CaseService();
        service.transformer = new Transformer();
        service.photoRepository = new PhotoRepository();
        service.photoRepository.factory = new PhotoFactory();

        service.factory = new CaseFactory();
        service.repository = new CaseRepository();
        service.repository.factory = new CaseFactory();

        UserFactory userFactory = new UserFactory();
        UserRepository userRepository = new UserRepository();
        userRepository.factory = userFactory;
        user = userFactory.createUser();
        user = userRepository.insert(user);

        PhotoFactory photoFactory = new PhotoFactory();
        PhotoRepository photoRepository = new PhotoRepository();
        photoRepository.factory = photoFactory;
        photo = photoFactory.createPhoto(buildMockImageBytes());
        photo = photoRepository.insert(photo);
    }

    @Test
    public void test_createCase() throws SQLException {
        // act
        CaseDto actualCaseDto = service.createCase(user, photo.getId(), FlagReason.COPYRIGHT.asString());

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
        CaseDto expectedCaseDto = service.createCase(user, photo.getId(), FlagReason.COPYRIGHT.asString());
        System.out.println(expectedCaseDto.getId());

        // act
        CaseDto actualCaseDto = service.closeCase(expectedCaseDto.getId());

        // assert
        Assert.assertNotNull(actualCaseDto);
        Assert.assertTrue(actualCaseDto.isWasDecided());
        Assert.assertEquals(expectedCaseDto.getId(), actualCaseDto.getId());
        Assert.assertEquals(user.getId(), actualCaseDto.getFlaggerId());
        Assert.assertEquals(photo.getId(), actualCaseDto.getPhotoId());
        Assert.assertEquals(FlagReason.COPYRIGHT, actualCaseDto.getReason());
    }

}
