package org.wahlzeit.services.dbconfig;

public interface DataBaseConfig {
	
	public String getDriver();
	public String getConnection();	
	public String getUser();
	public String getPassword();
	
	public void setUpServer();
	public void tearDownServer();
}
