package org.wahlzeit.model;

public class Building {
	
	
	private BuildingType buildingType;
	
	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
	}

	public Building() {		
	}
	
	public Building (BuildingType type) {
		this.buildingType = type;		
	}
	
	public BuildingType getBuildingType() {
		return buildingType;
	}	

}