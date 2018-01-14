/*
 *  Copyright
 *
 *  Classname: Pattern
 *  Author: Tango1266
 *  Version: 13.01.18 12:15
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

package org.wahlzeit.utils;

public abstract class Pattern {
    public String[] roles;
    public String description;

    public class TemplateMethod extends Pattern {
        public TemplateMethod() {
            description = "\"defines the program skeleton of an algorithm in an operation, deferring some steps to subclasses.[\" (wikipedia.org, 2018)";
            roles = new String[]{"Abstract Class", "Concrete Class"};
        }
    }

    public class ValueObject extends Pattern {
        public ValueObject() {
            description = "\"A value object is a small object that represents a simple entity whose equality is not based on identity.\" (wikipedia.de, 2018)";
            roles = new String[]{"Value Object"};
        }
    }

    public class Adapter extends Pattern {
        public Adapter() {
            description = "\"Adapter allows the interface of an existing class to be used as another interface.[1] It is often used to make existing classes work with others without modifying their source code.\" (wikipedia.org, 2018";
            roles = new String[]{"Adapter", "Concrete Adapter", "Adaptee"};
        }
    }

    public class NullObject extends Pattern {
        public NullObject() {
            description = "\"A Null Object is an object with no referenced value or with defined neutral (\"null\") behavior\" (wikipedia.org, 2018)";
            roles = new String[]{"Abstract Object, Null Object"};
        }
    }

    public class AbstractFactory extends Pattern {
        public AbstractFactory() {
            description = "\"The abstract factory pattern provides a way to encapsulate a group of individual factories that have a common theme without specifying their concrete classes\" (wikipedia.org, 2018)";
            roles = new String[]{"Abstract Factory, Concrete Factory"};
        }
    }

    public class Singleton extends Pattern {
        public Singleton() {
            description = "\"Singleton restricts the instantiation of a class to one object. \" (wikipedia.org, 2018)";
            roles = new String[]{"Singleton"};
        }
    }

    public class TypeObject {
        public TypeObject() {
            description = "\"Allow the flexible creation of new “classes” by creating a single class, each instance of which represents a different type of object. \" (http://gameprogrammingpatterns.com/type-object.html, 2018)";
            roles = new String[]{"Object", "Type Object"};
        }
    }
}
