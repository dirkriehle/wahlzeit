package org.wahlzeit_revisited.service;

import jakarta.inject.Singleton;
import org.wahlzeit_revisited.dto.CaseDto;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.dto.UserDto;
import org.wahlzeit_revisited.model.Case;
import org.wahlzeit_revisited.model.Photo;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.repository.Persistent;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Transforms entities to daos
 */
@Singleton
public class Transformer {

    /**
     * @methodtype transform
     */
    public UserDto transform(User user) {
        assertIsNotNull(user);
        assertValidPersistent(user);

        return new UserDto.Builder()
                .withId(user.getId())
                .withName(user.getName())
                .withEmail(user.getEmail())
                .build();
    }

    /**
     * @methodtype transform
     */
    public PhotoDto transform(Photo photo) {
        assertIsNotNull(photo);
        assertValidPersistent(photo);

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

    /**
     * @methodtype transform
     */
    public CaseDto transform(Case photoCase) {
        assertIsNotNull(photoCase);
        assertValidPersistent(photoCase);

        return new CaseDto.Builder()
                .withId(photoCase.getId())
                .withPhotoId(photoCase.getPhotoId())
                .withFlaggerId(photoCase.getFlaggerId())
                .withReason(photoCase.getReason())
                .withWasDecided(photoCase.wasDecided())
                .withExplanation(photoCase.getExplanation())
                .withCreatedOn(photoCase.getCreationTime())
                .withDecidedOn(photoCase.getDecisionTime())
                .build();
    }

    /**
     * @methodtype transform
     */
    public List<PhotoDto> transformPhotos(List<Photo> photos) {
        return photos.stream().map(this::transform).collect(Collectors.toList());
    }

    /**
     * @methodtype transform
     */
    public List<CaseDto> transformCases(List<Case> cases) {
        return cases.stream().map(this::transform).collect(Collectors.toList());
    }

    /**
     * @methodtype assert
     */
    private static void assertIsNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Object cannot be null");
        }
    }

    /**
     * @methodtype assert
     */
    private static void assertValidPersistent(Persistent persistent) {
        if (persistent.getId() == null) {
            throw new IllegalArgumentException("Photo needs an id");
        }
    }


}
