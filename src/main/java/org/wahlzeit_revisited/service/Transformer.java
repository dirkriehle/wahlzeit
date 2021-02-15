package org.wahlzeit_revisited.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.dto.UserDto;
import org.wahlzeit_revisited.model.Photo;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.utils.WahlzeitConfig;

import java.io.File;

@Singleton
public class Transformer {

    @Inject
    public WahlzeitConfig config;

    public UserDto transform(User user) {
        assertNonNull(user);
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public PhotoDto transform(Photo photo) {
        assertNonNull(photo);
        assertValidPhoto(photo);

        String path = transformToMappedPath(photo);
        return new PhotoDto(photo.getId(), path, photo.getWidth(), photo.getHeight());
    }

    public String transformToPersistPath(Photo photo) {
        assertNonNull(photo);
        assertValidPhoto(photo);

        return config.getPhotosDir().getRootDir() + File.separator + photo.getId() + ".png";
    }

    protected String transformToMappedPath(Photo photo) {
        assertNonNull(photo);
        assertValidPhoto(photo);

        return config.getStaticFileMappingPath() + File.separator + photo.getId() + ".png";
    }


    /*
     * asserts
     */

    private static void assertNonNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Object cannot be null");
        }
    }

    private static void assertValidPhoto(Photo photo) {
        if (photo.getId() == null) {
            throw new IllegalArgumentException("Photo needs an id");
        }
    }

}
