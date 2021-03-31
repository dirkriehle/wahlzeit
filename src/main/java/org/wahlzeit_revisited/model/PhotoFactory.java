package org.wahlzeit_revisited.model;

import org.wahlzeit_revisited.repository.PersistentFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class PhotoFactory implements PersistentFactory<Photo> {

    @Override
    public Photo createPersistent(ResultSet resultSet) throws SQLException {
        return new Photo(resultSet);
    }

    public Photo createPhoto(byte[] data) throws IOException {
        return createPhoto(data, Set.of());
    }

    public Photo createPhoto(byte[] data, Set<String> tags) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        Image image = ImageIO.read(is);
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        PhotoStatus status = PhotoStatus.VISIBLE;
        return new Photo(status, data, tags, width, height);
    }

    public Photo createPhoto(long ownerId, byte[] data, Set<String> tags) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        Image image = ImageIO.read(is);
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        PhotoStatus status = PhotoStatus.VISIBLE;
        return new Photo(ownerId, status, data, tags, width, height);
    }

}
