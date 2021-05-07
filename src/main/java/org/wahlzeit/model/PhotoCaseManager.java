/*
 * SPDX-FileCopyrightText: 2006-2009 Dirk Riehle <dirk@riehle.org> https://dirkriehle.com
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package org.wahlzeit.model;

import java.sql.*;
import java.util.*;

import org.wahlzeit.services.*;

/**
 * The photo case manager provides access to and manages persistent photo cases.
 */
public class PhotoCaseManager extends ObjectManager {
	
	/**
	 * 
	 */
	protected Map<CaseId, PhotoCase> openPhotoCases = new HashMap<CaseId, PhotoCase>();

	/**
	 * 
	 */
	protected static final PhotoCaseManager instance = new PhotoCaseManager();

	/**
	 * 
	 * @methodtype get
	 */
	public static final PhotoCaseManager getInstance() {
		return instance;
	}
	
	/**
	 * @methodtype constructor
	 * @methodproperty composed
	 */
	protected PhotoCaseManager() {
		initialize();
	}
	
	/**
	 * @methodtype initialization
	 * @methodproperty regular
	 */
	protected void initialize() {
		Collection<PhotoCase> opc = new LinkedList<PhotoCase>();
		loadOpenPhotoCases(opc);
		for (PhotoCase pc : opc) {
			openPhotoCases.put(pc.getId(), pc);
		}
	}
	
	/**
	 * 
	 * @methodtype factory
	 */
	protected PhotoCase createObject(ResultSet rset) throws SQLException {
		return new PhotoCase(rset);
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public PhotoCase getPhotoCase(int id) {
		PhotoCase result = openPhotoCases.get(id);
		if (result == null) {
			try {
				PreparedStatement stmt = getReadingStatement("SELECT * FROM cases WHERE id = ?");
				result = (PhotoCase) readObject(stmt, id);
			} catch (SQLException sex) {
				SysLog.logThrowable(sex);
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @methodtype command
	 */
	public void addPhotoCase(PhotoCase myCase) {
		openPhotoCases.put(myCase.getId(), myCase);
		try {
			PreparedStatement stmt1 = getReadingStatement("INSERT INTO cases(id) VALUES(?)");
			createObject(myCase, stmt1, myCase.getId().asInt());
			PreparedStatement stmt2 = getUpdatingStatement("SELECT * FROM cases WHERE id = ?");
			updateObject(myCase, stmt2);
			// @FIXME Main.saveGlobals();
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}
	
	/**
	 * 
	 * @methodtype command
	 */
	public void removePhotoCase(PhotoCase myCase) {
		openPhotoCases.remove(myCase.getId());
		try {
			PreparedStatement stmt = getUpdatingStatement("SELECT * FROM cases WHERE id = ?");
			updateObject(myCase, stmt);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}	
	
	/**
	 * 
	 * @methodtype command
	 */
	public void loadOpenPhotoCases(Collection<PhotoCase> result) {
		try {
			PreparedStatement stmt = getReadingStatement("SELECT * FROM cases WHERE was_decided = FALSE");
			readObjects(result, stmt);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
		
		SysLog.logSysInfo("loaded all open photo cases");
	}
	
	/**
	 * 
	 * @methodtype command
	 */
	public void savePhotoCases() {
		try {
			PreparedStatement stmt = getUpdatingStatement("SELECT * FROM cases WHERE id = ?");
			updateObjects(openPhotoCases.values(), stmt);
		} catch (SQLException sex) {
			SysLog.logThrowable(sex);
		}
	}
	
	/**
	 * 
	 * @methodtype get
	 */
	public PhotoCase[] getOpenPhotoCasesByAscendingAge() {
		PhotoCase[] resultArray = openPhotoCases.values().toArray(new PhotoCase[0]);
		Arrays.sort(resultArray, getPhotoCasesByAscendingAgeComparator());
		return resultArray;
	}
	
	/**
	 * @methodtype get
	 */
	public static Comparator<PhotoCase> getPhotoCasesByAscendingAgeComparator() {
		return new Comparator<PhotoCase>() {
			public int compare(PhotoCase pc1, PhotoCase pc2) {
				long ct1 = pc1.getCreationTime();
				long ct2 = pc2.getCreationTime();
				if (ct1 < ct2) {
					return 1;
				} else if (ct1 > ct2) {
					return -1;
				} else {
					return 0;
				}
			}
		};
	}
}
