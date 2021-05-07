/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.apps;

import java.io.File;
import javax.servlet.*;

import org.wahlzeit.main.ServiceMain;
import org.wahlzeit.services.*;

/**
 * A simple ServletContextListener to startup and shutdown the Flowers application.
 */
public class Wahlzeit implements ServletContextListener {
	
	/**
	 * 
	 */
	public void contextInitialized(ServletContextEvent sce) {
		try {
			ServletContext sc = sce.getServletContext();
			
			// configures log4j
			String contextPath = sc.getContextPath();
			System.setProperty("contextPath", contextPath);
			SysLog.logSysInfo("context-path", contextPath);
			
			// determines file system root path to resources
			File dummyFile = new File(sc.getRealPath("dummy.txt"));
			String rootDir = dummyFile.getParent();			
			SysLog.logSysInfo("root-directory", rootDir);

			ServiceMain.getInstance().startUp(false, rootDir);
		} catch (Exception ex) {
            SysLog.logThrowable(ex);
            throw new RuntimeException("End of story!", ex);
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
