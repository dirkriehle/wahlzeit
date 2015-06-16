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

package org.wahlzeit.main;

import org.wahlzeit.services.SessionManager;
import org.wahlzeit.services.SysConfig;
import org.wahlzeit.services.SysSession;

/**
 * @author dirkriehle
 */
public abstract class AbstractMain {

    /**
     *
     */
    protected SysSession mainSession = null;

    /**
     *
     */
    protected AbstractMain() {
        // do nothing
    }

    /**
     *
     */
    protected void startUp(String rootDir) throws Exception {
        SysConfig.setInstance(createSysConfig(rootDir));

        mainSession = new SysSession("system");
        SessionManager.setThreadLocalSession(mainSession);
    }

    /**
     *
     */
    protected SysConfig createSysConfig(String rootDir) {
        return createDevSysConfig(rootDir);
    }

    /**
     *
     */
    protected SysConfig createProdSysConfig(String rootDir) {
        return new SysConfig(rootDir);
    }

    /**
     *
     */
    protected SysConfig createDevSysConfig(String rootDir) {
        return new SysConfig(rootDir);
    }

    /**
     *
     */
    protected void shutDown() throws Exception {
        SysConfig.dropInstance();
    }

}
