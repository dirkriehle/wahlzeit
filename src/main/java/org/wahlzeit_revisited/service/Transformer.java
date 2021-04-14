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
        return new UserDto.Builder()
                .withId(user.getId())
                .withName(user.getName())
                .withEmail(user.getEmail())
                .build();
    }

    public PhotoDto transform(Photo photo) {
        assertIsNotNull(photo);
        assertValidPhoto(photo);

        String path = "/api/photo/" + photo.getId() + "/data";
        return new PhotoDto.Builder()
                .withId(photo.getId())
                .withUserId(photo.getOwnerId())
                .withPath(path)
                .withTags(photo.getTags())
                .withWith(photo.getWidth())
                .withHeight(photo.getHeight())
                .withPraise(photo.getPraise())
                .build();
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
