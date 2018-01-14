/*
 *  Copyright
 *
 *  Classname: GurkeTest
 *  Author: Tango1266
 *  Version: 14.01.18 15:24
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

public class GurkeTest {
    GurkenManager gurkenManager;

    protected final Taste TASTE = Taste.UNSPECIFIED;
    protected final int SIZE21 = 210;
    protected final int SIZE65 = 650;
    protected final int AVGSIZE = 150;

    protected GurkenType salatGurke;
    protected GurkenType stachelGurke;
    protected GurkenType gelbeStachelGurke;
    protected GurkenType roteStachelGurke;

    protected Gurke unspecificSalatGurke;
    protected Gurke unspecificStachelGurke;
    protected Gurke salatGurke21;
    protected Gurke salatGurke65;
    protected Gurke stachelGurkeGelb;
    protected Gurke stachelGurkeRot;

    @Before
    public void setUp() throws Exception {
        gurkenManager = GurkenManager.getInstance();
        salatGurke = new GurkenType("Salatgurke");
        stachelGurke = new GurkenType("Stachelgurke");

        unspecificSalatGurke = new Gurke(salatGurke);
        unspecificStachelGurke = new Gurke(stachelGurke);
        salatGurke21 = new Gurke(salatGurke, TASTE, SIZE21);
        salatGurke65 = new Gurke(salatGurke, TASTE, SIZE65);

        gelbeStachelGurke = new GurkenType("Gelbe Stachelgurke");
        roteStachelGurke = new GurkenType("Rote Stachelgurke");
        stachelGurke.addSubType(gelbeStachelGurke);
        stachelGurke.addSubType(roteStachelGurke);

        stachelGurkeGelb = new Gurke(gelbeStachelGurke, TASTE, AVGSIZE);
        stachelGurkeRot = new Gurke(roteStachelGurke, TASTE, AVGSIZE);
    }

    @Test
    public void getType_salatGurke21_isSalatgurke_true() {
        Assert.assertEquals(salatGurke, salatGurke21.getType());
    }

    @Test
    public void getType_salatGurke21_isStachelGurke_false() {
        Assert.assertNotEquals(stachelGurke, salatGurke21.getType());
    }

    @Test
    public void getSize_salatGurke21_isSize21_true() {
        Assert.assertEquals(SIZE21, salatGurke21.getSize());
    }

    @Test
    public void getSize_salatGurke21_isSize65_false() {
        Assert.assertNotEquals(SIZE65, salatGurke21.getSize());
    }

    @Test
    public void equals_salatGurke21_and65_isFalse() {
        Assert.assertFalse(salatGurke21.equals(salatGurke65));
    }

    @Test
    public void unspecifiedSalatGurke_Taste_and_size_uninitialized() {
        Assert.assertEquals(salatGurke, unspecificSalatGurke.getType());
        Assert.assertEquals(null, unspecificSalatGurke.getTaste());
        Assert.assertEquals(0, unspecificSalatGurke.getSize());
    }

    @Test
    public void unspecifiedStachelGurke_Taste_and_size_uninitialized() {
        Assert.assertEquals(stachelGurke, unspecificStachelGurke.getType());
        Assert.assertEquals(null, unspecificStachelGurke.getTaste());
        Assert.assertEquals(0, unspecificStachelGurke.getSize());
    }

    @Test
    public void equals_salatGurke21_andEquallyCreatedGurke21_isTrue() {
        Gurke likeGurke21 = new Gurke(salatGurke, TASTE, SIZE21);
        Gurke cloneGurke21 = new Gurke(salatGurke21.getType(), salatGurke21.getTaste(), salatGurke21.getSize());
        Assert.assertTrue(salatGurke21.equals(likeGurke21));
        Assert.assertTrue(salatGurke21.equals(cloneGurke21));
    }

    @Test
    public void equals_salatGurke21_andEquallyCreatedGurke65_isFalse() {
        Gurke likeGurke21 = new Gurke(salatGurke, TASTE, SIZE65);
        Gurke cloneGurke21 = new Gurke(salatGurke65.getType(), salatGurke65.getTaste(), salatGurke65.getSize());
        Assert.assertFalse(salatGurke21.equals(likeGurke21));
        Assert.assertFalse(salatGurke21.equals(cloneGurke21));
    }

}