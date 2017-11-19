/*
 *  Copyright
 *
 *  Classname: GurkenDomainTest
 *  Author: Tango1266
 *  Version: 13.11.17 00:16
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

import org.junit.ClassRule;
import org.junit.rules.RuleChain;
import org.wahlzeit.testEnvironmentProvider.LocalDatastoreServiceTestConfigProvider;
import org.wahlzeit.testEnvironmentProvider.RegisteredOfyEnvironmentProvider;

/**
 * Test Rules for the Gurken Domain classes using persistence
 */
public class GurkenDomainTest {
    @ClassRule
    public static RuleChain ruleChain = RuleChain
            .outerRule(new RegisteredOfyEnvironmentProvider())
            .around(new LocalDatastoreServiceTestConfigProvider());
}
