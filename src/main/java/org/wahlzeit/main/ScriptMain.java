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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Main class that serves to run scripts (and exit afterwards).
 *
 * @author dirkriehle
 */
public abstract class ScriptMain extends ModelMain {

    private static final Logger log = Logger.getLogger(ScriptMain.class.getName());

    /**
     *
     */
    public void run() {
        try {
            startUp("web");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Exception: ", ex);
        }

        try {
            shutDown();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Exception: ", ex);
        }
    }


    /**
     *
     */
    protected void handleArgv(String argv[]) {
        for (int i = 0; i < argv.length; i++) {
            i = handleArg(argv[i], i, argv);
        }
    }

    /**
     *
     */
    protected int handleArg(String arg, int i, String argv[]) {
        // do nothing
        return 0;
    }
}
