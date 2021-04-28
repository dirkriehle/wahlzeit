package org.wahlzeit_revisited.service;


import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.*;
import org.wahlzeit_revisited.repository.PhotoRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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

    public PhotoDto addPhoto(User user, byte[] photoBlob, Set<String> unescapedTags) throws SQLException, IOException {
        if (unescapedTags.size() > Tags.MAX_NO_TAGS) {
            throw new WebApplicationException("Too much tags max: " + Tags.MAX_NO_TAGS, Response.Status.CONFLICT);
        }

        Tags tags = new Tags(unescapedTags);
        Photo insertPhoto = factory.createPhoto(user.getId(), photoBlob, tags);
        insertPhoto = repository.insert(insertPhoto);

        PhotoDto responseDto = transformer.transform(insertPhoto);
        return responseDto;
    }

    public PhotoDto getPhoto(long photoId) throws SQLException {
        Photo photo = findVisiblePhoto(photoId);

        PhotoDto responseDto = transformer.transform(photo);
        return responseDto;
    }

    public byte[] getPhotoData(long photoId) throws SQLException {
        Photo photo = findVisiblePhoto(photoId);
        return photo.getData();
    }

    public List<PhotoDto> getFilteredPhotos(Long userId, Set<String> unescapedTags) throws SQLException {
        Tags escapedTags = new Tags(unescapedTags);

        // Create a database filter, and further filter visible photos in the main memory
        PhotoFilter filter = new PhotoFilter(userId, escapedTags);
        List<Photo> photoList = repository
                .findWithFilter(filter)
                .stream()
                .filter(Photo::isVisible)
                .collect(Collectors.toList());

        List<PhotoDto> responseDto = transformer.transformPhotos(photoList);
        return responseDto;
    }

    public PhotoDto removePhoto(User user, long photoId) throws SQLException {
        Photo photo = findVisiblePhoto(photoId);
        if (!user.hasModeratorRights() && !photo.getOwnerId().equals(user.getId())) {
            throw new ForbiddenException("Photo does not belong to user");
        }

        // the internet never forgets
        photo.setStatus(PhotoStatus.DELETED);
        repository.update(photo);

        PhotoDto responseDto = transformer.transform(photo);
        return responseDto;
    }

    public PhotoDto praisePhoto(long photoId, long ranking) throws SQLException {
        if (ranking < 0 || ranking > 10) {
            throw new WebApplicationException("Invalid ranking number: " + ranking, Response.Status.CONFLICT);
        }

        Photo photo = findVisiblePhoto(photoId);
        photo.addToPraise(ranking);
        repository.update(photo);

        PhotoDto responseDto = transformer.transform(photo);
        return responseDto;
    }

    public PhotoDto setPhotoStatus(Long photoId, String status) throws SQLException {
        PhotoStatus newStatus = PhotoStatus.getFromString(status);
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));

        photo.setStatus(newStatus);
        repository.update(photo);

        PhotoDto photoDto = transformer.transform(photo);
        return photoDto;
    }

    private Photo findVisiblePhoto(long photoId) throws SQLException {
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));
        if (!photo.isVisible()) {
            throw new NotFoundException("Photo is not visible");
        }
        return photo;
    }

}
