package org.wahlzeit_revisited.service;


import jakarta.inject.Inject;
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

    public Response getPhotos() throws SQLException {
        List<Photo> photoList = repository.findAll();

        List<PhotoDto> responseDto = new ArrayList<>(photoList.size());
        for (Photo photo : photoList) {
            responseDto.add(transformer.transform(photo));
        }
        return Response.ok(responseDto).build();
    }

    public Response addPhoto(User user, byte[] photoBlob) throws SQLException {
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
            return Response.serverError().build();
        }
        SysLog.logSysInfo(String.format("Created new photo on disk: %s", persistPath));

        PhotoDto responseDto = transformer.transform(insertPhoto);
        return Response.ok(responseDto).build();
    }

    public Response getPhoto(long photoId) throws SQLException {
        Optional<Photo> photoOpt = repository.findById(photoId);
        if (photoOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PhotoDto responseDto = transformer.transform(photoOpt.get());
        return Response.ok(responseDto).build();
    }

    public Response getUserPhotos(Long userId) throws SQLException {
        List<Photo> photoList = repository.findForUser(userId);

        List<PhotoDto> responseDto = new ArrayList<>(photoList.size());
        for (Photo photo : photoList) {
            responseDto.add(transformer.transform(photo));
        }
        return Response.ok(responseDto).build();
    }

    public Response removePhoto(User user, long photoId) throws SQLException {
        Optional<Photo> photoOpt = repository.findById(photoId);
        if (photoOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (!user.hasModeratorRights() && !photoOpt.get().getOwnerId().equals(user.getId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Photo deletedPhoto = repository.delete(photoOpt.get());

        // remove from disk
        String persistPath = transformer.transformToPersistPath(deletedPhoto);
        File persistFile = new File(persistPath);
        if (persistFile.delete()) {
            SysLog.logSysInfo(String.format("Deleted photo from disk: %s", persistPath));
        } else {
            SysLog.logSysInfo(String.format("Failed deleting photo from disk: %s", persistPath));
        }

        PhotoDto responseDto = transformer.transform(deletedPhoto);
        return Response.ok(responseDto).build();
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
