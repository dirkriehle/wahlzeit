package org.wahlzeit.services.dbconfig;

import org.hsqldb.server.Server;
import org.wahlzeit.services.SysConfig;
import org.wahlzeit.services.SysLog;

public class HyperSQLConfig implements DataBaseConfig {
	protected final String dbName = "wahlzeit";
	
	protected final String driver = "org.hsqldb.jdbcDriver";
	protected final String connection = "jdbc:hsqldb:hsql://localhost/" + dbName;
	protected Server server;
	
	@Override
	public String getDriver() {
		return driver;
	}

	@Override
	public String getConnection() {
		return connection;
	}

	@Override
	public String getUser() {
		return "sa";//SysConfig.getDbUserAsString();
	}

	@Override
	public String getPassword() {
		return "";//SysConfig.getDbPasswordAsString();
	}

	@Override
	public void setUpServer() {
		try	{
			server = new Server();
			
			server.setLogWriter(null);
			server.setSilent(true);
			
			server.setDatabaseName(0, dbName);
			server.setDatabasePath(0, "file:" + dbName + "db");
			
			SysLog.logInfo("Starting hsql server...");
			
			server.start();
			
			SysLog.logInfo("Hsql server started.");
		} catch (Exception ex){
			SysLog.logThrowable(ex);
		}		
	}

	@Override
	public void tearDownServer() {
		SysLog.logInfo("Stopping hsql server.");
		
		try	{
			if (server != null)	{
				server.stop();
			}			
		} catch (Exception ex){
			SysLog.logThrowable(ex);
		}
	}
}
