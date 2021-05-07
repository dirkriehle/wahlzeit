package org.wahlzeit_revisited.main;


import org.wahlzeit_revisited.database.SessionManager;
import org.wahlzeit_revisited.utils.SysLog;
import org.wahlzeit_revisited.utils.WahlzeitConfig;

public class DatabaseMain extends ModelMain {

    public DatabaseMain(WahlzeitConfig config) {
        super(config);
    }

    public void shutDown() {
        SessionManager.dropSession();
        SysLog.logSysInfo("Shutting down database");
    }
}
