package org.wahlzeit.testsetup;

import org.wahlzeit.services.DatabaseConnection;
import org.wahlzeit.services.dbconfig.HyperSQLConfig;
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
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		DatabaseConnection.shutDown();
	}
}
