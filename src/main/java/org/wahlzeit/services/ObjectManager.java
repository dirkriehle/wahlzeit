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

package org.wahlzeit.services;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * An ObjectManager creates/reads/updates/deletes Persistent (objects) from Google Datastore. It is an abstract
 * superclass that relies an inheritance interface and the Persistent interface.
 *
 * @author dirkriehle
 */
public abstract class ObjectManager {

    /**
     * All objects are now saved under this root key. In case of multitenancy this may change to several keys.
     */
    public static final Key applicationRootKey = KeyFactory.createKey("Application", "Wahlzeit");

    private static final Logger log = Logger.getLogger(ObjectManager.class.getName());


    /**
     * Reads the first Entity with the given key in the Datastore
     */
    protected <E> E readObject(Class<E> type, Long id) throws IllegalArgumentException {
        assertIsNonNullArgument(type, "type");
        assertIsNonNullArgument(id, "id");

        log.config(LogBuilder.createSystemMessage().
                addMessage("Load Type " + type.toString() + " with ID " + id + " from datastore.").toString());
        return OfyService.ofy().load().type(type).id(id).now();
    }

    /**
     *
     */
    protected void assertIsNonNullArgument(Object arg, String label) {
        if (arg == null) {
            throw new IllegalArgumentException(label + " should not be null");
        }
    }

    /**
     * Reads the first Entity with the given key in the Datastore
     */
    protected <E> E readObject(Class<E> type, String id) throws IllegalArgumentException {
        assertIsNonNullArgument(type, "type");
        assertIsNonNullArgument(id, "id");

        log.config(LogBuilder.createSystemMessage().
                addMessage("Load Type " + type.toString() + " with ID " + id + " from datastore.").toString());
        return OfyService.ofy().load().type(type).id(id).now();
    }

    /**
     * Reads an Entity of the specified type where the wanted parameter has the given name, e.g. readObject(User.class,
     * "emailAddress", "name@provider.com").
     */
    protected <E> E readObject(Class<E> type, String parameterName, Object value) {
        assertIsNonNullArgument(type, "type");
        assertIsNonNullArgument(parameterName, "parameterName");
        assertIsNonNullArgument(value, "value");

        log.config(LogBuilder.createSystemMessage().
                addMessage("Load Type " + type.toString() + " with parameter " +
                        parameterName + " == " + value + " from datastore.").toString());

        return OfyService.ofy().load().type(type).ancestor(applicationRootKey).filter(parameterName, value).first().now();
    }

    /**
     * Reads all Entities of the specified type, e.g. readObject(User.class) to get a list of all clients
     */
    protected <E> void readObjects(Collection<E> result, Class<E> type) {
        assertIsNonNullArgument(result, "result");
        assertIsNonNullArgument(type, "type");

        log.config(LogBuilder.createSystemMessage().
                addParameter("Datastore: load all entities of type", type.getName()).toString());
        List<E> objects = OfyService.ofy().load().type(type).ancestor(applicationRootKey).list();
        log.config(LogBuilder.createSystemMessage().
                addParameter("Datastore: number of loaded objects", objects.size()).toString());
        result.addAll(objects);
    }

    /**
     * Reads all Entities of the specified type, where the given property matches the wanted value e.g.
     * readObject(User.class) to get a list of all clients
     */
    protected <E> void readObjects(Collection<E> result, Class<E> type, String propertyName, Object value) {
        assertIsNonNullArgument(result, "result");
        assertIsNonNullArgument(type, "type");
        assertIsNonNullArgument(propertyName, "propertyName");
        assertIsNonNullArgument(value, "value");

        log.info(LogBuilder.createSystemMessage().
                addMessage("Datastore: Load all Entities of type " + type.toString() + " where parameter "
                        + propertyName + " = " + value.toString() + " from datastore.").toString());
        List<E> objects = OfyService.ofy().load().type(type).
                ancestor(applicationRootKey).filter(propertyName, value).list();
        log.config(LogBuilder.createSystemMessage().
                addParameter("Datastore: number of loaded objects", objects.size()).toString());
        result.addAll(objects);
    }

    /**
     * Updates all entities of the given collection in the datastore.
     */
    protected void updateObjects(Collection<? extends Persistent> collection) {
        for (Persistent object : collection) {
            updateObject(object);
        }
    }

    /**
     * Updates the given entity in the datastore.
     */
    protected void updateObject(Persistent object) {
        writeObject(object);
    }

    /**
     * Writes the given entity to the datastore.
     */
    protected void writeObject(Persistent object) {
        assertIsNonNullArgument(object, "object");

        if (object.isDirty()) {
            log.info(LogBuilder.createSystemMessage().
                    addParameter("Datastore: Write object of type", object).toString());
            OfyService.ofy().save().entity(object).now();
            updateDependents(object);
            object.resetWriteCount();
        } else {
            log.info(LogBuilder.createSystemMessage().
                    addParameter("Datastore: No need to update object", object).toString());
        }
    }

    /**
     * Updates all dependencies of the object.
     */
    protected void updateDependents(Persistent object) {
        // overwrite if your object has additional dependencies
    }

    /**
     * Deletes the given entity from the datastore.
     */
    protected <E> void deleteObject(E object) {
        assertIsNonNullArgument(object, "object");

        log.config(LogBuilder.createSystemMessage().addParameter("Datastore: delete entity", object).toString());
        OfyService.ofy().delete().entity(object).now();
    }

    /**
     * Deletes all entities of the type that have a property with the specified value, e.g.
     * deleteObjects(PhotoCase.class, "wasDecided", true) to delete all cases that have been decided.
     */
    protected <E> void deleteObjects(Class<E> type, String propertyName, Object value) {
        assertIsNonNullArgument(type, "type");
        assertIsNonNullArgument(propertyName, "propertyName");
        assertIsNonNullArgument(value, "value");

        log.info(LogBuilder.createSystemMessage().
                addMessage("Datastore: delete entities of type " + type
                        + " where property " + propertyName + " == " + value).toString());
        List<com.googlecode.objectify.Key<E>> keys = OfyService.ofy().load().type(type).
                ancestor(applicationRootKey).filter(propertyName, value).keys().list();
        OfyService.ofy().delete().keys(keys);
    }

    /**
     *
     */
    protected void assertIsNonNullArgument(Object arg) {
        assertIsNonNullArgument(arg, "anonymous");
    }

}
