package org.wahlzeit_revisited.model;

import org.wahlzeit_revisited.repository.PersistentFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * PhotoFactory creates a new Photo, or instantiates a entity from a resultSet
 */
public class PhotoFactory implements PersistentFactory<Photo> {

    /*
     * PersistentFactory contract
     */

    @Override
    public Photo createPersistent(ResultSet resultSet) throws SQLException {
        return new Photo(resultSet);
    }

    /**
     * Creates a plain photo from bytes
     */
    public Photo createPhoto(byte[] data) throws IOException {
        return createPhoto(data, new Tags());
    }

    /**
     * Creates a tagged photo from bytes
     * @param data photo data
     * @param tags photo tags
     * @return the tagged photo
     * @throws IOException invalid image data
     */
    public Photo createPhoto(byte[] data, Tags tags) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        Image image = ImageIO.read(is);
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        PhotoStatus status = PhotoStatus.VISIBLE;
        return new Photo(status, data, tags, width, height);
    }

    /**
     * Creates a owned and tagged photo from bytes
     * @param owner photo owner
     * @param data photo data
     * @param tags photo tags
     * @return owned and tagged photo
     * @throws IOException invalid image data
     */
    public Photo createPhoto(User owner, byte[] data, Tags tags) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        Image image = ImageIO.read(is);
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        long ownerId = owner.getId();

        PhotoStatus status = PhotoStatus.VISIBLE;
        return new Photo(ownerId, status, data, tags, width, height);
    }

}
