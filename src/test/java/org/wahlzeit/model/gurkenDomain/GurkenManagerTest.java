/*
 *  Copyright
 *
 *  Classname: GurkenTypeTest
 *  Author: Tango1266
 *  Version: 13.01.18 18:53
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GurkenManagerTest {

    GurkenManager gurkenManager;
    GurkenType stachelGurke;
    GurkenType salatGurke;
    String stachelGurkeTypeName;
    String salatGurkeTypeName;
    Gurke saltGurke;
    GurkenType gurkenTypeHirarchy;

    @Before
    public void setUp() {
        stachelGurkeTypeName = "Stachel Gurke";
        salatGurkeTypeName = "Salat Gurke";
        gurkenManager = GurkenManager.getInstance();
        stachelGurke = gurkenManager.getType(stachelGurkeTypeName);
        salatGurke = gurkenManager.getType(salatGurkeTypeName);
        saltGurke = gurkenManager.getGurke(salatGurkeTypeName);
        gurkenTypeHirarchy = createTypeHirarchy();
    }

    @Test
    public void getType_ofSameTypes_areEqual() {
        Assert.assertEquals(stachelGurke, gurkenManager.getType(stachelGurkeTypeName));
        Assert.assertTrue(stachelGurke == gurkenManager.getType(stachelGurkeTypeName));
    }

    @Test
    public void getType_ofDifferentTypes_areNotEqual() {
        Assert.assertNotEquals(stachelGurke, salatGurke);
        Assert.assertFalse(stachelGurke == salatGurke);
    }

    @Test
    public void gurkenTypeHirarchy_hasInstance_GelbeSalatgurke_isTrue() {
        Assert.assertTrue(gurkenTypeHirarchy.hasInstance(gurkenManager.getGurke("Gurke")));
    }

    @Test
    public void gurkenTypeHirarchy_hasInstance_Wassermelone_isFalse() {
        Assert.assertFalse(gurkenTypeHirarchy.hasInstance(gurkenManager.getGurke("Wassermelone")));
    }

    @Test
    public void gurkenTypeHirarchy_hasInstance_Langstachelgurke_isTrue() {
        Assert.assertTrue(gurkenTypeHirarchy.hasInstance(gurkenManager.getGurke("Langstachelgurke")));
    }

    @Test
    public void gurkenTypeHirarchy_hasInstance_Salatgurke_isTrue() {
        Assert.assertTrue(gurkenTypeHirarchy.hasInstance(gurkenManager.getGurke("Salatgurke")));
    }

    @Test
    public void gurkenTypeHirarchy_hasInstance_RoteLangstachelgurke_isTrue() {
        Assert.assertTrue(gurkenTypeHirarchy.hasInstance(gurkenManager.getGurke("Rote Langstachelgurke")));
    }

    private GurkenType createTypeHirarchy() {
        // Level 0
        GurkenType gurke = gurkenManager.getType("Gurke");
        // Level 1
        GurkenType salatgurke = gurkenManager.getType("Salatgurke");
        GurkenType stachelGurke = gurkenManager.getType("Stachelgurke");
        gurke.addSubType(salatgurke);
        gurke.addSubType(stachelGurke);

        // Level 2
        GurkenType gelbeSalatGurke = gurkenManager.getType("Gelbe Salatgurke");
        GurkenType langStachelGurke = gurkenManager.getType("Langstachelgurke");
        salatgurke.addSubType(gelbeSalatGurke);
        stachelGurke.addSubType(langStachelGurke);

        // Level 3
        GurkenType gelbeLangStachelGurke = gurkenManager.getType("Gelbe Langstachelgurke");
        GurkenType roteLangStachelGurke = gurkenManager.getType("Rote Langstachelgurke");
        langStachelGurke.addSubType(gelbeLangStachelGurke);
        langStachelGurke.addSubType(roteLangStachelGurke);
        return gurke;
    }
}
