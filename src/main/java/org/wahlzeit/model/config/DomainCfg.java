/*
 *  Copyright
 *
 *  Classname: DomainCfg
 *  Author: Tango1266
 *  Version: 14.11.17 07:46
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

package org.wahlzeit.model.config;

import org.wahlzeit.model.gurkenDomain.GurkenPhoto;
import org.wahlzeit.model.gurkenDomain.GurkenPhotoFactory;
import org.wahlzeit.model.gurkenDomain.GurkenPhotoManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Single Access Point to the domain specif Logic. In order to change the entire domain of Wahlzeit,
 */
public class DomainCfg {

    public static GurkenPhotoManager PhotoManager;
    public static GurkenPhotoFactory PhotoFactory;

    /**
     * objects for the database registration of google objectify service
     */
    public static List<Class> getDomainObjectsForRegistration() {
        return getGurkenDomainObjects();
    }

    /**
     * to evidently show the existence to the reader
     */
    public static void initializeDomain() {
        initializeGurkenDomain();
        //defaultInitialization()
    }

    private static void initializeGurkenDomain() {
        PhotoManager = GurkenPhotoManager.getInstance();
        PhotoFactory = GurkenPhotoFactory.getInstance();
    }

    private static List<Class> getGurkenDomainObjects() {
        return new LinkedList<Class>(Arrays.asList(
                GurkenPhoto.class
        ));
    }

    /**
     * just to show the handling of different domain scenarios in the current architecture with Singletons.
     */
    private static void defaultInitialization() throws ExceptionInInitializerError {
        throw new ExceptionInInitializerError("Modification needed: Code needs to be uncommented and the types of fields need to be changed");
    /*
        PhotoManager = org.wahlzeit.model.PhotoManager.getInstance();
        PhotoFactory = org.wahlzeit.model.PhotoFactory.getInstance();
    */
    }
}
