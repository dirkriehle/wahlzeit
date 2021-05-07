/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com, Aron Metzig
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

package org.wahlzeit_revisited.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * A Repository is a wrapper between the Database and Java POJOs
 * It provides CRUD operations on the entities
 * @param <T> to represent Persistent
 */
public interface Repository<T extends Persistent> {

    Optional<T> findById(Long id) throws SQLException;

    List<T> findAll() throws SQLException;

    T insert(T toInsert) throws SQLException;

    T update(T toUpdate) throws SQLException;

    T delete(T toDelete) throws SQLException;

}
