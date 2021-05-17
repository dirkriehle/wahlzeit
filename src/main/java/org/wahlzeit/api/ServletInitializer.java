package org.wahlzeit.api;

import jakarta.inject.Inject;
import org.apache.log4j.Logger;
import org.wahlzeit.agent.AgentManager;
import org.wahlzeit.config.WahlzeitConfig;
import org.wahlzeit.main.DatabaseMain;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletInitializer implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ServletInitializer.class);

    @Inject
    AgentManager agentManager;
    @Inject
    WahlzeitConfig config;
    // Not injected
    DatabaseMain databaseMain;

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        LOG.info("Starting Java EE Container..");

        // Init database
        databaseMain = new DatabaseMain(config);
        try {
            databaseMain.startUp();
        } catch (Exception e) {
            LOG.fatal("Failed starting database connection", e);
        }

        // Setup agents
        agentManager.startAllThreads();
    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        LOG.info("Shutting down Java EE Container..");

        databaseMain.shutDown();
        agentManager.stopAllThreads();
    }

}
