/*
 *  Copyright
 *
 *  Classname: Gurke
 *  Author: Tango1266
 *  Version: 14.01.18 11:45
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
import org.wahlzeit.utils.Pattern;
import org.wahlzeit.utils.PatternInstance;

@PatternInstance(
        pattern = Pattern.TypeObject.class,
        classRole = "Object",
        participants = {Gurke.class, GurkenType.class}
)
//TODO: Prepaire TypeObject hirarchy for objectify service (@ID etc)
/**
 * Represents a specific Gurke of a certain GurkenType
 */
public class Gurke {
    private Taste taste;
    private int size;

    //Metainformation
    private String strain;

    //attribute is required by uml diagram
    public GurkenManager manager = GurkenManager.getInstance();
    public GurkenType type;

    public Gurke(GurkenType type, Taste taste, int size) {
        setType(type);
        setTaste(taste);
        setSize(size);
        setStrain(type.getStrain());
    }

    public Gurke(GurkenType type) {
        setType(type);
        setStrain(type.getStrain());
    }

    public void setType(GurkenType type) {
        Assert.notNull(type, "GurkenType");
        this.type = type;
    }

    public GurkenType getType() {
        return type;
    }

    public void setSize(int size) {
        Assert.areValidDoubles(size);
        this.size = size;
    }

    public void setTaste(Taste taste) {
        Assert.notNull(taste, "Taste");
        this.taste = taste;
    }

    public int getSize() {
        return size;
    }

    public Taste getTaste() {
        return taste;
    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Gurke gurke = (Gurke) o;

        if (getSize() != gurke.getSize()) {
            return false;
        }
        if (getTaste() != gurke.getTaste()) {
            return false;
        }
        if (getStrain() != null ? !getStrain().equals(gurke.getStrain()) : gurke.getStrain() != null) {
            return false;
        }
        return getType() != null ? getType().equals(gurke.getType()) : gurke.getType() == null;
    }

    @Override
    public String toString() {
        return "Gurke{" +
                "taste=" + taste +
                ", size=" + size +
                ", type=" + type +
                '}';
    }

}
