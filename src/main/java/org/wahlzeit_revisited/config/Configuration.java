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

package org.wahlzeit_revisited.config;

import java.io.File;
import java.io.IOException;

/**
 * An interface that manages a simple key/value store.
 * Clients can get and set individual values; they can also load properties files.
 * Key names are configured during initialization and can't be changed afterwards.
 * Hence, any access using an unknown key leads to an IllegalArgumentException.
 */
public interface Configuration {

    /**
     *
     */
    boolean hasKey(String key);

    /**
     *
     */
    String getValue(String key) throws IllegalArgumentException;

    /**
     *
     */
    void setValue(String key, String value) throws IllegalArgumentException;

    /**
     *
     */
    void loadProperties(File file) throws IOException;

}
