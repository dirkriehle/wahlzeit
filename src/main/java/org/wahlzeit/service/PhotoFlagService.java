/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 * Copyright (c) 2021 by Aron Metzig
 *
 * This file is part of the Wahlzeit photo rating application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.wahlzeit.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.apache.log4j.Logger;
import org.wahlzeit.api.dto.PhotoDto;
import org.wahlzeit.database.repository.PhotoRepository;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoStatus;
import org.wahlzeit.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Business logic of photo flagging
 */
public class PhotoFlagService {

    protected static final Logger LOG = Logger.getLogger(PhotoFlagService.class);

    @Inject
    public
    PhotoRepository repository;
    @Inject
    public
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

        LOG.info(String.format("Fetched %s flagged photos", photos.size()));
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

        LOG.info(String.format("Fetched flagged photo %s", photo.getId()));
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

        LOG.info(String.format("Fetched flagged photo %s data", photo.getId()));
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

        LOG.info(String.format("Set flagged photo %s status %s", photo.getId(), status));
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
