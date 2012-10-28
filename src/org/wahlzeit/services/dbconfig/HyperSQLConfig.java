package org.wahlzeit.services.dbconfig;

import org.hsqldb.server.Server;
import org.wahlzeit.services.SysLog;

public class HyperSQLConfig implements DataBaseConfig {
	protected final String dbName = "wahlzeit";
	protected Server server;
	
	@Override
	public String getDriver() {
		return "org.hsqldb.jdbcDriver";
	}

	@Override
	public String getConnection() {
		return "jdbc:hsqldb:hsql://localhost/" + dbName;
	}

	@Override
	public String getUser() {
		return "sa";
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public void setUpServer() {
		try	{
			startServer();
		} catch (Exception ex){
			SysLog.logThrowable(ex);
		}		
	}

	protected void startServer()	{
		server = new Server();
		
		server.setLogWriter(null);
		server.setSilent(true);
		
		server.setDatabaseName(0, dbName);
		server.setDatabasePath(0, "file:" + dbName + "db");
		
		SysLog.logInfo("Starting hsql server...");
		
		server.start();
		
		SysLog.logInfo("Hsql server started.");
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
