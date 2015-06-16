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

package org.wahlzeit.tools;

import org.wahlzeit.main.ScriptMain;
import org.wahlzeit.model.GlobalsManager;
import org.wahlzeit.services.SysConfig;

import java.io.File;

/**
 * Sets up a fresh clean Wahlzeit Flowers application database.
 *
 * @author dirkriehle
 */
public class SetUpFlowers extends ScriptMain {

    /**
     *
     */
    public static void main(String[] argv) {
        new SetUpFlowers().run();
    }

    /**
     *
     */
    public void startUp(String rootDir) throws Exception {
        super.startUp(rootDir);
        GlobalsManager.getInstance().loadGlobals();
    }

    /**
     *
     */
    public void execute() throws Exception {
        String photoDir = SysConfig.getRootDirAsString() + File.separator + "config" + File.separator + "flowers";
        createUser("1337", "testuser", "info@wahlzeit.org", photoDir);
    }

}
