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

package org.wahlzeit.database.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A Persistent object is an object that can be read from and written to some storage.
 */
public interface Persistent {

    /**
     *
     */
    Long getId();

    void setId(long id);

    /**
     *
     */
    void readFrom(ResultSet rset) throws SQLException;

    /**
     *
     */
    void writeOn(ResultSet rset) throws SQLException;

}
