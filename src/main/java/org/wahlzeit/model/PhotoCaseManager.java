/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
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

import org.wahlzeit.services.ObjectManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The photo case manager provides access to and manages persistent photo cases.
 *
 * @author dirkriehle
 */
public class PhotoCaseManager extends ObjectManager {

    /**
     *
     */
    protected static final PhotoCaseManager instance = new PhotoCaseManager();
    /**
     *
     */
    protected Map<CaseId, PhotoCase> openPhotoCases = new HashMap<CaseId, PhotoCase>();

    /**
     * @methodtype constructor
     * @methodproperty composed
     */
    protected PhotoCaseManager() {
        initialize();
    }

    /**
     * @methodtype initialization
     * @methodproperty regular
     */
    protected void initialize() {
        Collection<PhotoCase> opc = new LinkedList<PhotoCase>();
        loadOpenPhotoCases(opc);
        for (PhotoCase pc : opc) {
            openPhotoCases.put(pc.getId(), pc);
        }
    }

    /**
     * @methodtype command
     */
    public void loadOpenPhotoCases(Collection<PhotoCase> result) {
        readObjects(result, PhotoCase.class, PhotoCase.WAS_DECIDED, false);
    }

    /**
     * @methodtype get
     */
    public static final PhotoCaseManager getInstance() {
        return instance;
    }

    /**
     * @methodtype get
     */
    public PhotoCase getPhotoCase(PhotoId id) {
        PhotoCase result = openPhotoCases.get(id);
        if (result == null) {
            result = readObject(PhotoCase.class, PhotoCase.ID, id);
        }

        return result;
    }

    /**
     * @methodtype command
     */
    public void addPhotoCase(PhotoCase myCase) {
        openPhotoCases.put(myCase.getId(), myCase);
        if (myCase.isDirty()) {
            writeObject(myCase);
        }
        // @FIXME Main.saveGlobals();
    }

    /**
     * @methodtype command
     */
    public void removePhotoCase(PhotoCase myCase) {
        openPhotoCases.remove(myCase.getId());
        deleteObject(myCase);
    }

    /**
     * @methodtype command
     */
    public void savePhotoCases() {
        if (openPhotoCases != null && openPhotoCases.size() > 0) {
            updateObjects(openPhotoCases.values());
        }
    }

    /**
     * @methodtype get
     */
    public PhotoCase[] getOpenPhotoCasesByAscendingAge() {
        PhotoCase[] resultArray = openPhotoCases.values().toArray(new PhotoCase[0]);
        Arrays.sort(resultArray, getPhotoCasesByAscendingAgeComparator());
        return resultArray;
    }

    /**
     * @methodtype get
     */
    public static Comparator<PhotoCase> getPhotoCasesByAscendingAgeComparator() {
        return new Comparator<PhotoCase>() {
            public int compare(PhotoCase pc1, PhotoCase pc2) {
                long ct1 = pc1.getCreationTime();
                long ct2 = pc2.getCreationTime();
                if (ct1 < ct2) {
                    return 1;
                } else if (ct1 > ct2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
    }
}
