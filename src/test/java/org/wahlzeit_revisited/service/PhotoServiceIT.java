package org.wahlzeit_revisited.service;

import jakarta.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wahlzeit_revisited.BaseModelTest;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.PhotoFactory;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.model.UserFactory;
import org.wahlzeit_revisited.repository.PhotoRepository;
import org.wahlzeit_revisited.repository.UserRepository;
import org.wahlzeit_revisited.utils.SysConfig;

import java.sql.SQLException;
import java.util.List;

public class PhotoServiceIT extends BaseModelTest {

    private PhotoService service;
    private UserRepository userRepository;
    private UserFactory userFactory;

    @Before
    public void setupDependencies() {
        service = new PhotoService();
        service.factory = new PhotoFactory();
        service.repository = new PhotoRepository();
        service.repository.factory = new PhotoFactory();
        service.transformer = new Transformer();
        service.transformer.config = new SysConfig();

        userFactory = new UserFactory();
        userRepository = new UserRepository();
        userRepository.factory = new UserFactory();
    }

    @Test
    public void test_getPhotos() throws SQLException {
        // execute
        Response response = service.getPhotos();

        // validate
        List<PhotoDto> photoDtos = assertSuccessfulResponse(response);
        Assert.assertNotNull(photoDtos);
    }

    @Test
    public void test_addPhoto() throws SQLException {
        // prepare
        User user = userFactory.createUser();
        user = userRepository.insert(user);

        // execute
        Response response = service.addPhoto(user, new byte[]{});

        // validate
        PhotoDto responseDto = assertSuccessfulResponse(response);
        Assert.assertNotNull(responseDto);
    }

    @Test
    public void test_getPhoto() throws SQLException {
        // prepare
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedDto = assertSuccessfulResponse(service.addPhoto(user, new byte[]{}));

        // execute
        Response response = service.getPhoto(expectedDto.getId());

        // validate
        PhotoDto actualDto = assertSuccessfulResponse(response);
        Assert.assertEquals(expectedDto.getId(), actualDto.getId());
        Assert.assertEquals(expectedDto.getPath(), actualDto.getPath());
        Assert.assertEquals(expectedDto.getWidth(), actualDto.getWidth());
        Assert.assertEquals(expectedDto.getHeight(), actualDto.getHeight());
    }

    @Test
    public void test_getUserPhotos() throws SQLException {
        // prepare
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedDto = assertSuccessfulResponse(service.addPhoto(user, new byte[]{}));

        // execute
        Response response = service.getUserPhotos(user.getId());

        // validate
        List<PhotoDto> responseDto = assertSuccessfulResponse(response);
        Assert.assertEquals(1, responseDto.size());
        Assert.assertEquals(expectedDto.getId(), responseDto.get(0).getId());
        Assert.assertEquals(expectedDto.getPath(), responseDto.get(0).getPath());
        Assert.assertEquals(expectedDto.getWidth(), responseDto.get(0).getWidth());
        Assert.assertEquals(expectedDto.getHeight(), responseDto.get(0).getHeight());
    }

    @Test
    public void test_removePhoto() throws SQLException {
        // prepare
        User user = userFactory.createUser();
        user = userRepository.insert(user);
        PhotoDto expectedDto = assertSuccessfulResponse(service.addPhoto(user, new byte[]{}));

        // execute
        Response response = service.removePhoto(user, expectedDto.getId());

        // validate
        PhotoDto responseDto = assertSuccessfulResponse(response);
        Assert.assertNotEquals(Response.Status.OK.getStatusCode(), service.getPhoto(expectedDto.getId()).getStatus());
        Assert.assertEquals(expectedDto.getId(), responseDto.getId());
        Assert.assertEquals(expectedDto.getPath(), responseDto.getPath());
        Assert.assertEquals(expectedDto.getWidth(), responseDto.getWidth());
        Assert.assertEquals(expectedDto.getHeight(), responseDto.getHeight());
    }

    /*
     * helpers
     */

    @SuppressWarnings("unchecked")
    protected <T> T assertSuccessfulResponse(Response response) {
        if (response.getStatus() < 200 || response.getStatus() > 299) {
            Assert.fail("Invalid Statuscode: " + response.getStatus());
        }
        return (T) response.getEntity();
    }
}
