package org.wahlzeit_revisited.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.Photo;
import org.wahlzeit_revisited.model.PhotoStatus;
import org.wahlzeit_revisited.repository.PhotoRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PhotoFlagService {

    @Inject
    PhotoRepository repository;
    @Inject
    Transformer transformer;

    public List<PhotoDto> getFlaggedPhotos() throws SQLException {
        List<Photo> photos = repository.findAll()
                .stream()
                .filter(p -> p.getStatus() == PhotoStatus.FLAGGED)
                .collect(Collectors.toList());

        List<PhotoDto> responseDto = transformer.transformPhotos(photos);
        return responseDto;
    }

    public PhotoDto getFlaggedPhoto(Long photoId) throws SQLException {
        Photo photo = findFlaggedPhoto(photoId);

        PhotoDto responseDto = transformer.transform(photo);
        return responseDto;
    }

    public byte[] getFlaggedPhotoData(Long photoId) throws SQLException {
        Photo photo = findFlaggedPhoto(photoId);

        return photo.getData();
    }

    public PhotoDto setPhotoStatus(Long photoId, String status) throws SQLException {
        PhotoStatus newStatus = PhotoStatus.getFromString(status);
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));

        photo.setStatus(newStatus);
        repository.update(photo);

        PhotoDto photoDto = transformer.transform(photo);
        return photoDto;
    }

    private Photo findFlaggedPhoto(long photoId) throws SQLException {
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));
        if (!photo.getStatus().isFlagged()) {
            throw new NotFoundException("Photo is not flagged");
        }
        return photo;
    }

}
