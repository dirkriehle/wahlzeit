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

package org.wahlzeit.model;

import org.wahlzeit.database.repository.PersistentFactory;

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
     *
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
     *
     * @param owner photo owner
     * @param data  photo data
     * @param tags  photo tags
     * @return owned and tagged photo
     * @throws IOException invalid image data
     */
    public Photo createPhoto(User owner, byte[] data, Tags tags) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        Image image = ImageIO.read(is);
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        long ownerId = owner == null ? 0 : owner.getId();

        PhotoStatus status = PhotoStatus.VISIBLE;
        return new Photo(ownerId, status, data, tags, width, height);
    }

}
