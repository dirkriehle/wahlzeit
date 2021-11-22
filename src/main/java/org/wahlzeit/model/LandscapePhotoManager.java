package org.wahlzeit.model;

import java.io.File;
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
	public static LandscapePhotoManager getInstance() {
		return instance;
	}
	
	/**
	 * 
	 */
	protected Photo createObject(ResultSet rset) throws SQLException {
		return LandscapePhotoFactory.getInstance().createPhoto(rset);
	}

	/**
	 * 
	 */
	public LandscapePhoto createPhoto(File file) throws Exception {
		PhotoId id = PhotoId.getNextId();
		LandscapePhoto result = (LandscapePhoto) PhotoUtil.createPhoto(file, id);
		addPhoto(result);
		return result;
	}
}
