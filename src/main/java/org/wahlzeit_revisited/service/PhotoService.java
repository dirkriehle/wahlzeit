package org.wahlzeit_revisited.service;


import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.wahlzeit_revisited.dto.PhotoDto;
import org.wahlzeit_revisited.model.Photo;
import org.wahlzeit_revisited.model.PhotoFactory;
import org.wahlzeit_revisited.model.User;
import org.wahlzeit_revisited.repository.PhotoRepository;
import org.wahlzeit_revisited.utils.SysLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

        List<PhotoDto> responseDto = new ArrayList<>(photoList.size());
        for (Photo photo : photoList) {
            responseDto.add(transformer.transform(photo));
        }

        return responseDto;
    }

    public PhotoDto addPhoto(User user, byte[] photoBlob) throws SQLException {
        Photo insertPhoto = factory.createPhoto(user.getId());
        insertPhoto = repository.insert(insertPhoto);

        // write to disk
        String persistPath = transformer.transformToPersistPath(insertPhoto);
        File persistFile = new File(persistPath);
        assertUniqueFile(persistFile);

        try (FileOutputStream fileOutputStream = new FileOutputStream(persistFile)) {
            fileOutputStream.write(photoBlob);
        } catch (IOException e) {
            SysLog.logThrowable(e);
            // clean up orphans
            repository.delete(insertPhoto);
            if (persistFile.exists() && persistFile.delete()) {
                SysLog.logSysError(String.format("Deleted photo from disk: %s", persistPath));
            }
            throw new InternalServerErrorException("Photo couldn't get persisted");
        }
        SysLog.logSysInfo(String.format("Created new photo on disk: %s", persistPath));

        PhotoDto responseDto = transformer.transform(insertPhoto);
        return responseDto;
    }

    public PhotoDto getPhoto(long photoId) throws SQLException {
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));

        PhotoDto responseDto = transformer.transform(photo);
        return responseDto;
    }

    public List<PhotoDto> getUserPhotos(Long userId) throws SQLException {
        List<Photo> photoList = repository.findForUser(userId);

        List<PhotoDto> responseDto = new ArrayList<>(photoList.size());
        for (Photo photo : photoList) {
            responseDto.add(transformer.transform(photo));
        }
        return responseDto;
    }

    public PhotoDto removePhoto(User user, long photoId) throws SQLException {
        Photo photo = repository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));

        if (!user.hasModeratorRights() && !photo.getOwnerId().equals(user.getId())) {
            throw new ForbiddenException("Photo does not belong to user");
        }

        Photo deletedPhoto = repository.delete(photo);

        // remove from disk
        String persistPath = transformer.transformToPersistPath(deletedPhoto);
        File persistFile = new File(persistPath);
        if (persistFile.delete()) {
            SysLog.logSysInfo(String.format("Deleted photo from disk: %s", persistPath));
        } else {
            SysLog.logSysInfo(String.format("Failed deleting photo from disk: %s", persistPath));
        }

        PhotoDto responseDto = transformer.transform(deletedPhoto);
        return responseDto;
    }

    /*
     * Helper methods
     */

    private void assertUniqueFile(File file) {
        if (file.exists()) {
            throw new IllegalArgumentException("File with id %s already exists");
        }
    }
}
