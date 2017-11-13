package org.wahlzeit.model;

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
	 */
	
	public  String getBuildingYear() {
		return BuildingYear;
	}
	
	/**
	 * @methodtype set
	 */

	public static final  void setBuildingYear(String buildingyear) {
		BuildingYear = buildingyear;
	}


}
