/*
 *  Copyright
 *
 *  Classname: GurkenManager
 *  Author: Tango1266
 *  Version: 14.01.18 11:19
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

package org.wahlzeit.model.gurkenDomain;

import org.wahlzeit.utils.Assert;

import java.util.HashMap;

public class GurkenManager {

    private static final GurkenManager instance = new GurkenManager();
    private HashMap<String, GurkenType> types = new HashMap<>();
    private HashMap<Integer, Gurke> gurken = new HashMap<>();

    protected GurkenType gurkenType;

    public GurkenManager() {
        gurkenType = new GurkenType();
    }

    public static GurkenManager getInstance() {
        return instance;
    }

    public Gurke getGurke(String type, Taste taste, int size) {
        GurkenType gt = getType(type);
        Gurke gurke = gt.createInstance(taste, size);

        int mapKey = gurke.hashCode();
        if (!gurken.containsKey(mapKey)) {
            gurken.put(mapKey, gurke);
        }
        return gurke;
    }

    public Gurke getGurke(String type) {
        return getGurke(type, Taste.UNSPECIFIED, 0);
    }

    public GurkenType getType(String gurkenTypeName) {
        assertIsValidTypeName(gurkenTypeName);
        if (!types.containsKey(gurkenTypeName)) {
            return createType(gurkenTypeName);
        }
        return types.get(gurkenTypeName);
    }

    private void assertIsValidTypeName(String gurkenTypeName) {
        Assert.notNull(gurkenTypeName, "GurkenType Name");
        Assert.stringNotEmpty(gurkenTypeName);
    }

    private GurkenType createType(String gurkenType) {
        GurkenType newGurkenType = new GurkenType(gurkenType);
        types.put(gurkenType, newGurkenType);
        return newGurkenType;
    }
}
