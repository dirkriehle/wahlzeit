package org.wahlzeit.model;

import java.sql.*;

public class LandscapePhotoManager extends PhotoManager {

    /**
	 * 
	 */
	protected static final LandscapePhotoManager instance = new LandscapePhotoManager();

    /**
	 * @Methodtype constructor
	 */
	public LandscapePhotoManager() {
		photoTagCollector = LandscapePhotoFactory.getInstance().createPhotoTagCollector();
	}
	
	/**
	 * 
	 */
	protected Photo createObject(ResultSet rset) throws SQLException {
		return LandscapePhotoFactory.getInstance().createPhoto(rset);
	}
}
