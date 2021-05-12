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

import org.wahlzeit.api.auth.AccessRights;
import org.wahlzeit.database.repository.PersistentFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

/*
 * UserFactory creates a new Photo, or instantiates a entity from a resultSet
 */
public class UserFactory implements PersistentFactory<User> {

    /*
     * PersistentFactory contract
     */

    @Override
    public User createPersistent(ResultSet resultSet) throws SQLException {
        return new User(resultSet);
    }

    /**
     * @return
     */
    public User createUser() {
        String name = Instant.now().toEpochMilli() + "NoUsername";
        String email = Instant.now().toEpochMilli() + "noEmail@fau.de";
        String password = "NoPassword";
        AccessRights accessRights = AccessRights.NONE;

        User user = new User(name, email, password);
        user.setRights(accessRights);
        return user;
    }

    public User createUser(String name, String email, String password, AccessRights rights) {
        User user = new User(name, email, password);
        user.setRights(rights);
        return user;
    }

}
