package org.wahlzeit_revisited.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.Photo;
import org.wahlzeit_revisited.model.PhotoStatus;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.repository.PhotoRepository;
import org.wahlzeit_revisited.utils.SysLog;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Business logic of photo flagging
 */
public class PhotoFlagService {

    @Inject
    PhotoRepository repository;
    @Inject
    Transformer transformer;

    /**
     * Set of photos, which are flagged
     *
     * @return flagged photoDtos
     * @throws SQLException internal error
     */
    public List<PhotoDto> getFlaggedPhotos() throws SQLException {
        List<Photo> photos = repository.findAll()
                .stream()
                .filter(p -> p.getStatus() == PhotoStatus.FLAGGED)
                .collect(Collectors.toList());

        SysLog.logSysInfo(String.format("Fetched %s flagged photos", photos.size()));
        List<PhotoDto> responseDto = transformer.transformPhotos(photos);
        return responseDto;
    }

    /**
     * Get flagged Photo by id
     *
     * @param photoId id of flagged photo
     * @return flagged photo
     * @throws SQLException internal error
     */
    public PhotoDto getFlaggedPhoto(Long photoId) throws SQLException {
        Photo photo = findFlaggedPhoto(photoId);

        SysLog.logSysInfo(String.format("Fetched flagged photo %s", photo.getId()));
        PhotoDto responseDto = transformer.transform(photo);
        return responseDto;
    }

    /**
     * Returns the content of the flagged photo
     *
     * @param photoId id of flagged photo
     * @return flagged photo content
     * @throws SQLException internal error
     */
    public byte[] getFlaggedPhotoData(Long photoId) throws SQLException {
        Photo photo = findFlaggedPhoto(photoId);
        SysLog.logSysInfo(String.format("Fetched flagged photo %s data", photo.getId()));
        return photo.getData();
    }

    /**
     * Sets the status of a photo, if users permission are sufficient
     *
     * @param user    initiator
     * @param photoId id of the photo
     * @param status  new status
     * @return updated PhotoDto
     * @throws SQLException
     */
    public PhotoDto setPhotoStatus(User user, Long photoId, String status) throws SQLException {
        PhotoStatus newStatus = PhotoStatus.getFromString(status);
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));
        if (!user.hasModeratorRights() && !photo.getOwnerId().equals(user.getId())) {
            throw new ForbiddenException("Photo does not belong to user");
        }

        photo.setStatus(newStatus);
        repository.update(photo);

        SysLog.logSysInfo(String.format("Set flagged photo %s status %s", photo.getId(), status));
        PhotoDto photoDto = transformer.transform(photo);
        return photoDto;
    }

    /**
     * @methodtype get
     */
    private Photo findFlaggedPhoto(long photoId) throws SQLException {
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));
        if (!photo.getStatus().isFlagged()) {
            throw new NotFoundException("Photo is not flagged");
        }
        return photo;
    }

}
