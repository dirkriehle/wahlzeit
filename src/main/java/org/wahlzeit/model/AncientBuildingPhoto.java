package org.wahlzeit.model;

import static org.junit.Assert.assertNotNull;

import com.googlecode.objectify.annotation.Subclass;

@Subclass

public class AncientBuildingPhoto extends Photo {
	
	

	public static  String BuildingYear = "buildingyear";
	
	/**
	 * 
	 */
	public AncientBuildingPhoto() {
		super();
	}

	/**
	 * 
	 */
	
	public AncientBuildingPhoto(PhotoId myId) {
		super(myId);
	}

	/**
	 * @methodtype get
	 * @Pro check that the building year is not null
	 */
	
	public String getBuildingYear() {
		
		assertNotNull(BuildingYear );
		return BuildingYear;
	}
	
	/**
	 * @methodtype set
	 * @pre checj the building year is not null
	 */

	public static final  void setBuildingYear(String buildingyear) {
		assertNotNull(buildingyear);
		BuildingYear = buildingyear;
	}


}
