package org.wahlzeit_revisited.main;


import org.wahlzeit.services.SessionManager;
import org.wahlzeit_revisited.utils.SysLog;
import org.wahlzeit_revisited.utils.WahlzeitConfig;

import java.sql.SQLException;

public class DatabaseMain extends ModelMain {

    public DatabaseMain(WahlzeitConfig config) {
        super(config);
    }

    public void shutDown() {
        try {
            saveAll();
            SessionManager.dropThreadLocalSession();
            SysLog.logSysInfo("Shutting down database");
        } catch (SQLException sqlException) {
            SysLog.logThrowable(sqlException);
        }
    }
}
