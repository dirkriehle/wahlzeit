/*
 *  Copyright
 *
 *  Classname: GurkenPhoto
 *  Author: Tango1266
 *  Version: 13.11.17 00:21
 *
 *  This file is part of the Wahlzeit photo rating application.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public
 *  License along with this program. If not, see
 *  <http://www.gnu.org/licenses/>
 */

package org.wahlzeit.model.gurkenDomain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Serialize;
import com.googlecode.objectify.annotation.Subclass;
import org.wahlzeit.model.Location;
import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoId;

/**
 * A gurkenphoto represents a user-provided (uploaded) photo of a cucumber.
 */
@Entity
@Subclass
public class GurkenPhoto extends Photo {

    @Serialize
    private Gurke gurkenInfo;

    /**
     * only for testing
     */
    public GurkenPhoto() {
        id = PhotoId.getNextId();
    }

    public GurkenPhoto(PhotoId myId, Gurke gurke) {
        super(myId);
        setGurkenInfo(gurke);
    }

    public GurkenPhoto(PhotoId photoId, Location location, Gurke gurkenInfo) {
        setID(photoId);
        setLocation(location);
        setGurkenInfo(gurkenInfo);
    }

    public Gurke getGurkenInfo() {
        return gurkenInfo;
    }

    public void setGurkenInfo(Gurke gurkenInfo) {
        this.gurkenInfo = gurkenInfo;
    }
}
