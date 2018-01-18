package org.wahlzeit.model;
import java.util.HashMap;
import java.util.Map;

import org.wahlzeit.services.ObjectManager;

// this class to handle the creation of the building and buildig type

public class BuildingManager extends ObjectManager{

	
	private static BuildingManager instance = null;
	protected Map<String, BuildingType> buildingType = new HashMap<String, BuildingType>();
	public Map<String, BuildingType> getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(Map<String, BuildingType> buildingType) {
		this.buildingType = buildingType;
	}

	private BuildingManager(){
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized BuildingManager getInstance() {
		if (instance == null) {
			setInstance(new BuildingManager());
		}

		return instance;
	}
	protected static synchronized void setInstance(BuildingManager buildingManger) {
		if (instance != null) {
			throw new IllegalStateException("attempt to initalize buildingManger twice");
		}

		instance = buildingManger;
	}
	
	/*first assure that this type is not existing to decide either create
	 *  new type or return this type
	 */
	public BuildingType createBuildingType(String type) {
		
		if (buildingType.containsKey(type)){
			return buildingType.get(type);
		}
		buildingType.put(type, new BuildingType(type));
        return buildingType.get(type);			
	}
	public Building createBuilding(BuildingType type) {
		return new Building(type);
	}
	
}
