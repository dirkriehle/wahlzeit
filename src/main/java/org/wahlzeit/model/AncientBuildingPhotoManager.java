package org.wahlzeit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.wahlzeit.services.LogBuilder;

import com.google.appengine.api.images.Image;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;

public class AncientBuildingPhotoManager extends PhotoManager {

	private static final Logger log = Logger.getLogger(AncientBuildingPhotoManager.class.getName());

	public AncientBuildingPhotoManager() {
		super();		
	}
	
	static  AncientBuildingPhotoManager instance= new AncientBuildingPhotoManager();
	
	public static  AncientBuildingPhotoManager getInstance() {
		return instance;
	}
	

	@Override
	public AncientBuildingPhoto createPhoto(String filename, Image uploadedImage) throws Exception {
		PhotoId id = PhotoId.getNextId();
		AncientBuildingPhoto result = PhotoUtil.createPhoto(filename, id, uploadedImage);
		addPhoto(result);
		return result;
	}
	
	public static void initialize() {
		getInstance(); // drops result due to getInstance() side-effects
	}
	
	@Override
	public void init() {
		loadPhotos();
	}
	
	
	//

}
