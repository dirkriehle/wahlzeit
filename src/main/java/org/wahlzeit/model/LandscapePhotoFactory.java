package org.wahlzeit.model;

import java.sql.*;

import org.wahlzeit.services.SysLog;

public class LandscapePhotoFactory extends PhotoFactory {



    /**
	 * Hidden singleton instance; needs to be initialized from the outside.
	 */
	private static LandscapePhotoFactory instance = null;
	
	
	/**
	 * @Methodtype constructor
	 */
	public LandscapePhotoFactory() {
		// TODO code?
        // private construktor ok wegen statischer factory methoden?
	}

	/**
	 * @methodtype factory
	 */
	public LandscapePhoto createPhoto() {
		return new LandscapePhoto();
	}
	
	/**
	 * @methodtype factory
	 */
	public LandscapePhoto createPhoto(PhotoId id) {
		return new LandscapePhoto(id);
	}
	
	/**
	 * @methodtype factory
	 */
	public LandscapePhoto createPhoto(ResultSet rs) throws SQLException {
		return new LandscapePhoto(rs);
	}
	/**
	 * Public singleton access method.
	 */
	public static synchronized PhotoFactory getInstance() {
		if (instance == null) {
			SysLog.logSysInfo("setting generic PhotoFactory");
			setInstance(new LandscapePhotoFactory());
		}
		
		return instance;
	}
}
