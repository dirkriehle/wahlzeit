package org.wahlzeit.model;

import java.sql.*;

public class LandscapePhotoManager extends PhotoManager {

    /**
	 * @Methodtype constructor
	 */
	public LandscapePhotoManager() {
		photoTagCollector = LandscapePhotoFactory.getInstance().createPhotoTagCollector();
	}

    /**
	 * 
	 */
	protected static final LandscapePhotoManager instance = new LandscapePhotoManager();
	
	/**
	 * 
	 */
	protected Photo createObject(ResultSet rset) throws SQLException {
		return LandscapePhotoFactory.getInstance().createPhoto(rset);
	}
}
