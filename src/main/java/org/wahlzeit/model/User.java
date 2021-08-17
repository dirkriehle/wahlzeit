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

import org.wahlzeit.api.auth.AccessRights;
import org.wahlzeit.database.repository.Persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class User extends Client implements Persistent {

    private Long id;
    private String name;
    private String password;
    private Gender gender = Gender.UNDEFINED;
    private Locale language = Locale.GERMAN;

    private long creationTime = System.currentTimeMillis();

    /*
     * constructor
     */

    public User(ResultSet resultSet) throws SQLException {
        readFrom(resultSet);
    }

    User(String name, String email, String password) {
        this.name = name;
        this.emailAddress = EmailAddress.getFromString(email);
        this.password = password;
    }

    /*
     * Persistent contract
     */

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        id = rset.getLong("id");
        name = rset.getString("name");
        emailAddress = EmailAddress.getFromString(rset.getString("email_address"));
        password = rset.getString("password");
        rights = AccessRights.getFromInt(rset.getInt("rights"));
        creationTime = rset.getLong("creation_time");
        language = new Locale(rset.getString("language"));
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateLong("id", id);
        rset.updateString("name", name);
        rset.updateString("email_address", (emailAddress == null) ? "" : emailAddress.asString());
        rset.updateString("password", password);
        rset.updateInt("rights", rights.asInt());
        rset.updateLong("creation_time", creationTime);
        rset.updateString("language", language.toString());
    }

    /*
     * getter/setter
     */

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return emailAddress.asString();
    }

    public void setEmail(String email) {
        this.emailAddress = EmailAddress.getFromString(email);
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public Locale getLanguage() {
        return language;
    }

    public String getSiteUrlAsString() {
        return "http://wahlzeit.org/";
    }
}
