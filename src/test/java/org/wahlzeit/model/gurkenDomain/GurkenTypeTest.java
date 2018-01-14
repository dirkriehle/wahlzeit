/*
 *  Copyright
 *
 *  Classname: GurkenTypeTest
 *  Author: Tango1266
 *  Version: 14.01.18 11:25
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
import org.junit.Test;

import java.util.Iterator;

public class GurkenTypeTest extends GurkeTest {

    @Test
    public void gurkenManagerInGurkentType_andGurnkenManager_areEqual() {

        GurkenManager gurkenManagerInGurkenType = new GurkenType().manager;
        Assert.assertEquals(gurkenManager, gurkenManagerInGurkenType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setStrain_withNumbersAsType_ShouldThrowException() {
        new GurkenType().setStrain("1234fu");
    }

    @Test
    public void setStrain_withLiteralsAsType_ShouldNotThrowException() {
        try {
            new GurkenType().setStrain("fu");
        } catch (Exception exception) {
            Assert.fail("Exception was thrown whereas no exception was expected");
        }
    }

    @Test
    public void superType_ofgelbeStachelgurke_isStachelGurke_true() {
        Assert.assertEquals(stachelGurke, gelbeStachelGurke.getSuperType());
    }

    @Test
    public void superType_ofRoteStachelgurke_isStachelGurke_true() {
        Assert.assertEquals(stachelGurke, roteStachelGurke.getSuperType());
    }

    @Test
    public void superType_ofgelbeStachelgurke_isSalatGurke_false() {
        Assert.assertNotEquals(salatGurke, gelbeStachelGurke.getSuperType());
    }

    @Test
    public void superType_ofRoteStachelgurke_isGurke_false() {
        Assert.assertNotEquals(salatGurke, roteStachelGurke.getSuperType());
    }

    @Test
    public void subTypes_ofStachelGurke_areRote_andGelbeStachelgurke_true() {
        Iterator<GurkenType> gurkenTypeIterator = stachelGurke.getSubTypeIterator();
        int subTypes = 0;
        while (gurkenTypeIterator.hasNext()) {
            GurkenType nextSubType = gurkenTypeIterator.next();
            Assert.assertTrue(nextSubType == roteStachelGurke || nextSubType == gelbeStachelGurke);
            Assert.assertFalse(nextSubType == roteStachelGurke && nextSubType == gelbeStachelGurke);
            subTypes++;
        }
        Assert.assertEquals(2, subTypes);
    }

    @Test
    public void afterSetSupertype_ofRoteStachelgurke_toSalatGurke_superTypeIsSalatGurke_true() {
        roteStachelGurke.setSuperType(salatGurke);
        Assert.assertEquals(salatGurke, roteStachelGurke.getSuperType());
    }

    @Test
    public void createInstance_likeSalatGurke21_eualsSalatGurke21_true() {
        Gurke likeGurke21 = salatGurke.createInstance(TASTE, SIZE21);
        Gurke cloneGurke21 = salatGurke21.getType().createInstance(salatGurke21.getTaste(), salatGurke21.getSize());

        Assert.assertTrue(salatGurke21.equals(likeGurke21));
        Assert.assertTrue(salatGurke21.equals(cloneGurke21));
    }
}
