package org.wahlzeit.services.dbconfig;

import org.wahlzeit.services.SysConfig;

public class PostgreSQLConfig implements DataBaseConfig {

	@Override
	public String getDriver() {
		return SysConfig.getDbDriverAsString();
	}

	@Override
	public String getConnection() {
		return SysConfig.getDbConnectionAsString();
	}

	@Override
	public String getUser() {
		return SysConfig.getDbUserAsString();
	}

	@Override
	public String getPassword() {
		return SysConfig.getDbPasswordAsString();
	}

	@Override
	public void setUpServer() {
		// do nothing
	}

	@Override
	public void tearDownServer() {
		// do nothing
	}
}
