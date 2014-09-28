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

package org.wahlzeit.apps;

import javax.servlet.*;

import org.wahlzeit.main.ServiceMain;
import org.wahlzeit.services.*;

/**
 * A simple ServletContextListener to startup and shutdown the Flowers application.
 * 
 * @author dirkriehle
 *
 */
public class GenericApp implements ServletContextListener {
	
	/**
	 * 
	 */
	public void contextInitialized(ServletContextEvent sce) {
		try {
			ServiceMain.getInstance().startUp(true);
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
		}
	}

	/**
	 * 
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			ServiceMain.getInstance().shutDown();
		} catch (Exception ex) {
			SysLog.logThrowable(ex);
		}
	}

}
