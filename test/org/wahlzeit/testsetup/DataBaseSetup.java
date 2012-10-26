package org.wahlzeit.testsetup;

import java.sql.Connection;
import java.sql.SQLException;
import org.wahlzeit.services.DatabaseConnection;
import org.wahlzeit.services.SysLog;
import org.wahlzeit.services.dbconfig.HyperSQLConfig;
import org.wahlzeit.tools.RunScript;

import junit.extensions.TestSetup;
import junit.framework.Test;

public class DataBaseSetup extends TestSetup	{
	public DataBaseSetup(Test test)	{
		super(test);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		DatabaseConnection.initialize(new HyperSQLConfig());

		if (noData())	{
			fillDB();
		}
	}

	protected boolean noData()	{
		try	{
			Connection conn = DatabaseConnection.openRdbmsConnection();
			conn.createStatement().execute("SELECT count(*) FROM users");
		} catch (SQLException ex)	{
			return true;
		}
		
		return false;
	}
	
	protected void fillDB()	{
		SysLog.logInfo("Filling database");
		
		//TODO: hsql does not like the script
		try	{
			new RunScript().run(new String[]{ "-S" });
		} catch (Exception ex)	{
			
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		DatabaseConnection.shutDown();
	}
}
