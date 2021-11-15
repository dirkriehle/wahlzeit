package org.wahlzeit.model;

import java.sql.*;

import org.wahlzeit.services.SysLog;

public class LandscapePhotoFactory extends PhotoFactory {



    /**
	 * Hidden singleton instance; needs to be initialized from the outside.
	 */
	private static LandscapePhotoFactory instance = null;
	
	/**
	 * Public singleton access method.
	 */
	public static synchronized LandscapePhotoFactory getInstance() {
		if (instance == null) {
			SysLog.logSysInfo("setting generic PhotoFactory");
			setInstance(new LandscapePhotoFactory());
		}
		
		return instance;
	}

		
	/**
	 * Method to set the singleton instance of PhotoFactory.
	 */
	protected static synchronized void setInstance(LandscapePhotoFactory photoFactory) {
		if (instance != null) {
			throw new IllegalStateException("attempt to initialize LandscapePhotoFactory twice");
		}
		
		instance = photoFactory;
	}
	
	/**
	 *
	 */
	protected LandscapePhotoFactory() {
		// do nothing
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
	public LandscapePhoto createPhoto(Location location) {
		return new LandscapePhoto(location);
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
	public LandscapePhoto createPhoto(PhotoId id, Location location) {
		return new LandscapePhoto(id, location);
	}
	
	/**
	 * @methodtype factory
	 */
	public LandscapePhoto createPhoto(ResultSet rs) throws SQLException {
		return new LandscapePhoto(rs);
	}
}
