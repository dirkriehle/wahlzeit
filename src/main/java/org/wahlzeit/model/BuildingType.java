package org.wahlzeit.model;

import java.util.HashSet;
import java.util.Set;

public class BuildingType {	
	
	protected static  BuildingType superBuildingType = new BuildingType() ;   
    //apply the sub type requirement
	protected static   Set<BuildingType> subBuildingTypes = new HashSet<BuildingType>();
	
	private String Name;
    
    public BuildingType getSuperBuildingType() {
		return superBuildingType;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public BuildingType(String type) {
		Name = type;
	}
	public BuildingType() {
		// TODO Auto-generated constructor stub
	}

	public Building makeBuilding()
	{
		return new Building(this);
	}
	/* to add new sub type for an existing type ; first assign parent to this type 
	and then assign it to the subtype*/
	
	public void addSubBuildingTypes(BuildingType buildingType )  {
		buildingType.superBuildingType= this;
		this.subBuildingTypes.add(buildingType);		
	}
	
	public void removeSubBuildingTypes(BuildingType buildingType )  {
		this.subBuildingTypes.remove(buildingType);		
	}
	public boolean isSubType(BuildingType buildingType ) {
		return (buildingType.superBuildingType!=null);
		}

}
