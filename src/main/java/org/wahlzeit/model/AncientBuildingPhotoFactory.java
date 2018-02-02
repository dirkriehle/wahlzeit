package org.wahlzeit.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.logging.Logger;

import org.wahlzeit.services.LogBuilder;

public class AncientBuildingPhotoFactory  extends PhotoFactory {


	private static final Logger log = Logger.getLogger(AncientBuildingPhotoFactory.class.getName());
	public Building building = new Building();
	public AncientBuildingPhotoFactory() {
		super();
	}

	private static AncientBuildingPhotoFactory instance = null;

	@Override
	public AncientBuildingPhoto createPhoto() {
		return new AncientBuildingPhoto(building);

	}
	/**
	 * @pre check the photo d is not null 
	 */
	@Override
	public AncientBuildingPhoto createPhoto(PhotoId id) {
		
		assertNotNull(id);
		return new AncientBuildingPhoto(id,building);
	}


	public static AncientBuildingPhotoFactory getInstance() {
		if (instance == null) {
			log.config(LogBuilder.createSystemMessage().addAction("setting generic AncientBuildingPhotoFactory").toString());
			setInstance(new AncientBuildingPhotoFactory());
		}

		return instance;
	}
	
	/**
	 * Hidden singleton instance; needs to be initialized from the outside.
	 */
	public static void initialize() {
		getInstance(); // drops result due to getInstance() side-effects
	}
	

	public static void setInstance(AncientBuildingPhotoFactory instance) {
		AncientBuildingPhotoFactory.instance = instance;
	}
	@Override
	public AncientBuildingPhoto loadPhoto(PhotoId id) {
		   /* Photo result =
	                OfyService.ofy().load().type(Photo.class).ancestor(KeyFactory.createKey("Application", "Wahlzeit")).filter(Photo.ID, id).first().now();
	        for (PhotoSize size : PhotoSize.values()) {
	            GcsFilename gcsFilename = new GcsFilename("picturebucket", filename);


\
	        }*/
			return null;
		}
	

	

}
