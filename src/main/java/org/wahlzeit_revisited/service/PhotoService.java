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

package org.wahlzeit_revisited.service;


import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.agent.AgentManager;
import org.wahlzeit_revisited.agent.NotifyAboutPraiseAgent;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.*;
import org.wahlzeit_revisited.repository.PhotoRepository;
import org.wahlzeit_revisited.repository.UserRepository;
import org.wahlzeit_revisited.utils.Directory;
import org.wahlzeit_revisited.utils.SysLog;
import org.wahlzeit_revisited.utils.WahlzeitConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Business logic of photos
 */
@Singleton
public class PhotoService {

    @Inject
    WahlzeitConfig config;

    @Inject
    UserRepository userRepository;

    @Inject
    public Transformer transformer;
    @Inject
    public PhotoRepository repository;
    @Inject
    public PhotoFactory factory;

    /**
     * Executed by Jakarta on the first execution of the application
     * Inserts all photos of the configured directory into the database
     *
     * @throws SQLException
     * @throws IOException
     */
    @PostConstruct
    public void setupInitialPhotos() throws SQLException, IOException {
        if (!repository.findAll().isEmpty()) {
            return;
        }

        int count = 0;
        Directory initialImageDir = config.getPhotosDir();
        File dir = new File(initialImageDir.asString());
        for (File currentFile : dir.listFiles()) {
            if (!currentFile.getName().endsWith(".txt")) {
                byte[] imageData = Files.readAllBytes(currentFile.toPath());
                addPhoto(null, imageData, Set.of());
                count++;
            }
        }
        SysLog.logSysInfo(String.format("Initialized wahlzeit with %s photos", count));
    }

    /**
     * Get all photos
     *
     * @return all available photos
     * @throws SQLException internal error
     */
    public List<PhotoDto> getPhotos() throws SQLException {
        List<Photo> photoList = repository.findAll();

        SysLog.logSysInfo(String.format("Fetched %s photos", photoList.size()));
        List<PhotoDto> responseDto = transformer.transformPhotos(photoList);
        return responseDto;
    }

    /**
     * Add a photo
     *
     * @param user          photo owner
     * @param photoBlob     photo data
     * @param unescapedTags photo tags
     * @return the created photoDto
     * @throws SQLException internal error
     * @throws IOException  invalid photo data
     */
    public PhotoDto addPhoto(User user, byte[] photoBlob, Set<String> unescapedTags) throws SQLException, IOException {
        if (unescapedTags.size() > Tags.MAX_NO_TAGS) {
            throw new WebApplicationException("Too much tags max: " + Tags.MAX_NO_TAGS, Response.Status.CONFLICT);
        }

        Tags tags = new Tags(unescapedTags);
        Photo insertPhoto = factory.createPhoto(user, photoBlob, tags);
        insertPhoto = repository.insert(insertPhoto);

        SysLog.logSysInfo(String.format("Added photo %s", insertPhoto.getId()));
        PhotoDto responseDto = transformer.transform(insertPhoto);
        return responseDto;
    }

    /**
     * Get a photo by id
     *
     * @param photoId id of photo
     * @return found PhotoDto
     * @throws SQLException internal error
     */
    public PhotoDto getPhoto(long photoId) throws SQLException {
        Photo photo = findVisiblePhoto(photoId);

        SysLog.logSysInfo(String.format("Fetched photo %s", photo.getId()));
        PhotoDto responseDto = transformer.transform(photo);
        return responseDto;
    }

    /**
     * Get a random Photo
     *
     * @return visible photo
     * @throws SQLException internal error
     */
    public PhotoDto getRandomPhoto() throws SQLException {
        Photo randomPhoto = repository.findRandomVisible();

        SysLog.logSysInfo(String.format("Fetched random photo %s", randomPhoto.getId()));
        PhotoDto responseDto = transformer.transform(randomPhoto);
        return responseDto;
    }

    /**
     * Get a photo data by id
     *
     * @param photoId id of photo
     * @return photo content
     * @throws SQLException internal error
     */
    public byte[] getPhotoData(long photoId) throws SQLException {
        Photo photo = findVisiblePhoto(photoId);
        SysLog.logSysInfo(String.format("Fetched photo %s data", photo.getId()));
        return photo.getData();
    }

    /**
     * Get photos by filter criteria
     *
     * @param userId        id of to filter user
     * @param unescapedTags list of to filter tags
     * @return found PhotoDtos
     * @throws SQLException internal error
     */
    public List<PhotoDto> getFilteredPhotos(Long userId, Set<String> unescapedTags) throws SQLException {
        Tags escapedTags = new Tags(unescapedTags);
        User user = userRepository.findById(userId).orElse(null);

        // Create a database filter, and further filter visible photos in the main memory
        PhotoFilter filter = new PhotoFilter(user, escapedTags);
        List<Photo> photoList = repository
                .findWithFilter(filter)
                .stream()
                .filter(Photo::isVisible)
                .collect(Collectors.toList());

        SysLog.logSysInfo(String.format("Fetched %s filtered photos", photoList.size()));
        List<PhotoDto> responseDto = transformer.transformPhotos(photoList);
        return responseDto;
    }

    /**
     * Removes a photo, if user permissions are sufficient
     *
     * @param user    initiator
     * @param photoId id of the photo to delete
     * @return deleted photoDto
     * @throws SQLException internal error
     */
    public PhotoDto removePhoto(User user, long photoId) throws SQLException {
        Photo photo = findVisiblePhoto(photoId);
        if (!user.hasModeratorRights() && !photo.getOwnerId().equals(user.getId())) {
            throw new ForbiddenException("Photo does not belong to user");
        }

        // the internet never forgets
        photo.setStatus(PhotoStatus.DELETED);
        repository.update(photo);

        SysLog.logSysInfo(String.format("Deleted photo %s", photo.getId()));
        PhotoDto responseDto = transformer.transform(photo);
        return responseDto;
    }

    /**
     * Adds a praise to a photo and notifies the owner
     *
     * @param photoId id of photo that got praised
     * @param ranking photo praise
     * @return the updated photoDto
     * @throws SQLException internal error
     */
    public PhotoDto praisePhoto(long photoId, long ranking) throws SQLException {
        if (ranking < 0 || ranking > 10) {
            throw new WebApplicationException("Invalid ranking number: " + ranking, Response.Status.CONFLICT);
        }

        Photo photo = findVisiblePhoto(photoId);
        photo.addToPraise(ranking);
        repository.update(photo);
        notifyPraise(photo);

        SysLog.logSysInfo(String.format("Praised photo %s to %s", photo.getId(), photo.getPraise()));
        PhotoDto responseDto = transformer.transform(photo);
        return responseDto;
    }

    /**
     * @methodtype get
     */
    private Photo findVisiblePhoto(long photoId) throws SQLException {
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));
        if (!photo.isVisible()) {
            throw new NotFoundException("Photo is not visible");
        }
        return photo;
    }

    /**
     * @methodtype command
     */
    private void notifyPraise(Photo photo) {
        NotifyAboutPraiseAgent praiseAgent = (NotifyAboutPraiseAgent) AgentManager
                .getInstance()
                .getAgent(NotifyAboutPraiseAgent.NAME);
        praiseAgent.addForNotify(photo);
    }

}
