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

import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.PhotoManager;
import org.wahlzeit.model.gurkenDomain.GurkenPhoto;
import org.wahlzeit.model.gurkenDomain.GurkenPhotoFactory;
import org.wahlzeit.model.gurkenDomain.GurkenPhotoManager;
import org.wahlzeit.services.LogBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Single Access Point to the domain specif Logic. In order to change the entire domain of Wahlzeit,
 */
public class DomainCfg {

    public static final Logger log = Logger.getLogger(DomainCfg.class.getName());

    /**
     * objects for the database registration of google objectify service
     */
    public static List<Class> getDomainObjectsForRegistration() {
        return getGurkenDomainObjects();
    }

    /**
     * to evidently show the existence to the reader
     */
    public static void initializePhotoDomainModel() {
        initializeGurkenDomain();
    }

    public static Logger getLogger(Object instance) {
        return Logger.getLogger(instance.getClass().getName());
    }

    public static void logError(Object instance, Exception ex) {
        DomainCfg.getLogger(instance.getClass()).warning(LogBuilder.createSystemMessage().addException(ex.getMessage(), ex).toString());
    }

    private static void initializeGurkenDomain() {
        log.config(LogBuilder.createSystemMessage().addAction("init PhotoFactory").toString());
        PhotoFactory.setInstance(new GurkenPhotoFactory());

        log.config(LogBuilder.createSystemMessage().addAction("init PhotoManager").toString());
        PhotoManager.setInstance(new GurkenPhotoManager());
        PhotoManager.getInstance().init();
    }

    private static List<Class> getGurkenDomainObjects() {
        return new LinkedList<Class>(Arrays.asList(
                GurkenPhoto.class
        ));
    }
}
