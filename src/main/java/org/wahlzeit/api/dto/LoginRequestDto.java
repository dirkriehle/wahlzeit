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

package org.wahlzeit.api.dto;

/**
 * LoginRequestDto so outer world can log in
 */
public class LoginRequestDto {

    private String email;
    private String password;

    public LoginRequestDto() {
        // default constructor
    }

    /**
     * @methodtype get
     */
    public String getEmail() {
        return email;
    }

    /**
     * @methodtype set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @methodtype get
     */
    public String getPassword() {
        return password;
    }

    /**
     * @methodtype set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
