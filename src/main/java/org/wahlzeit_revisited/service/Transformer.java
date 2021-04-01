package org.wahlzeit_revisited.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.dto.UserDto;
import org.wahlzeit_revisited.model.Photo;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.utils.WahlzeitConfig;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class Transformer {

    @Inject
    public WahlzeitConfig config;

    public UserDto transform(User user) {
        assertIsNotNull(user);
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public PhotoDto transform(Photo photo) {
        assertIsNotNull(photo);
        assertValidPhoto(photo);

        String path = "/api/photo/" + photo.getId() + "/data";
        return new PhotoDto(photo.getId(), photo.getOwnerId(), path, photo.getTags(), photo.getWidth(), photo.getHeight());
    }

    public List<PhotoDto> transformPhotos(List<Photo> photos) {
        return photos.stream().map(this::transform).collect(Collectors.toList());
    }

    /*
     * asserts
     */

    private static void assertIsNotNull(Object obj) {
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
