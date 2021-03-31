package org.wahlzeit_revisited.service;


import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.Photo;
import org.wahlzeit_revisited.model.PhotoFactory;
import org.wahlzeit_revisited.model.Tags;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.repository.PhotoRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;


public class PhotoService {

    @Inject
    public Transformer transformer;
    @Inject
    public PhotoRepository repository;
    @Inject
    public PhotoFactory factory;

    /*
     * business methods
     */

    public List<PhotoDto> getPhotos() throws SQLException {
        List<Photo> photoList = repository.findAll();

        List<PhotoDto> responseDto = transformer.transformPhotos(photoList);
        return responseDto;
    }

    public PhotoDto addPhoto(User user, byte[] photoBlob, Set<String> tags) throws SQLException, IOException {
        if (tags.size() > Tags.MAX_NO_TAGS) {
            throw new WebApplicationException("Too much tags max: " + Tags.MAX_NO_TAGS, Response.Status.CONFLICT);
        }

        Photo insertPhoto = factory.createPhoto(user.getId(), photoBlob, tags);
        insertPhoto = repository.insert(insertPhoto);

        PhotoDto responseDto = transformer.transform(insertPhoto);
        return responseDto;
    }

    public PhotoDto getPhoto(long photoId) throws SQLException {
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));

        PhotoDto responseDto = transformer.transform(photo);
        return responseDto;
    }

    public byte[] getPhotoData(long photoId) throws SQLException {
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));
        return photo.getData();
    }

    public List<PhotoDto> getUserPhotos(long userId) throws SQLException {
        List<Photo> photoList = repository.findForUser(userId);

        List<PhotoDto> responseDto = transformer.transformPhotos(photoList);
        return responseDto;
    }

    public List<PhotoDto> getTaggedPhotos(Set<String> unescapedTags) throws SQLException {
        Tags escapedTags = new Tags(unescapedTags);
        List<Photo> photoList = repository.findForTags(escapedTags.getTags());

        List<PhotoDto> responseDto = transformer.transformPhotos(photoList);
        return responseDto;
    }

    public PhotoDto removePhoto(User user, long photoId) throws SQLException {
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));
        if (!user.hasModeratorRights() && !photo.getOwnerId().equals(user.getId())) {
            throw new ForbiddenException("Photo does not belong to user");
        }

        Photo deletedPhoto = repository.delete(photo);

        PhotoDto responseDto = transformer.transform(deletedPhoto);
        return responseDto;
    }

}
